package com.qshop.sellupgrade.upgrades;

import com.qshop.api.CurrencyService;
import com.qshop.api.QShopAddonApi;
import com.qshop.sellbox.PriceQuote;
import com.qshop.sellupgrade.QShopSellUpgradeMod;
import com.qshop.sellupgrade.compat.ConfluenceCurrencyCombatCompat;
import com.qshop.sellupgrade.events.MagnetPickupContext;
import com.qshop.sellupgrade.events.PickupContext;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.server.ServerLifecycleHooks;
import net.p3pp3rf1y.sophisticatedcore.api.IStorageWrapper;
import net.p3pp3rf1y.sophisticatedcore.inventory.InventoryHandler;
import net.p3pp3rf1y.sophisticatedcore.settings.memory.MemorySettingsCategory;
import net.p3pp3rf1y.sophisticatedcore.upgrades.ContentsFilterLogic;
import net.p3pp3rf1y.sophisticatedcore.upgrades.IContentsFilteredUpgrade;
import net.p3pp3rf1y.sophisticatedcore.upgrades.IInsertResponseUpgrade;
import net.p3pp3rf1y.sophisticatedcore.upgrades.IPickupResponseUpgrade;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeWrapperBase;

import javax.annotation.Nullable;
import java.util.UUID;
import java.util.function.Consumer;

public final class SellUpgradeWrapper extends UpgradeWrapperBase<SellUpgradeWrapper, SellUpgradeItem>
        implements IPickupResponseUpgrade, IInsertResponseUpgrade, IContentsFilteredUpgrade {
    private static final double EPSILON = 1.0E-9D;

    private final ContentsFilterLogic filterLogic;

    public SellUpgradeWrapper(IStorageWrapper storageWrapper, ItemStack upgrade, Consumer<ItemStack> upgradeSaveHandler) {
        super(storageWrapper, upgrade, upgradeSaveHandler);
        int filterSlotCount = upgrade.getItem() instanceof SellUpgradeItem sellUpgradeItem
                ? sellUpgradeItem.getFilterSlotCount()
                : SellUpgradeItem.FILTER_SLOTS;
        filterLogic = new ContentsFilterLogic(upgrade, stack -> save(), filterSlotCount,
                storageWrapper::getInventoryHandler,
                storageWrapper.getSettingsHandler().getTypeCategory(MemorySettingsCategory.class));
    }

    @Override
    public ItemStack pickup(Level world, ItemStack stack, boolean simulate) {
        if (world.isClientSide) {
            return stack;
        }
        return trySell(stack, PickupContext.currentPlayer(), simulate);
    }

    @Override
    public ItemStack onBeforeInsert(InventoryHandler inventoryHandler, ItemStack stack, boolean simulate) {
        return trySell(stack, PickupContext.currentPlayer(), simulate);
    }

    @Override
    public ItemStack onBeforeInsert(InventoryHandler inventoryHandler, int slot, ItemStack stack, boolean simulate) {
        return trySell(stack, PickupContext.currentPlayer(), simulate);
    }

    /**
     * Attempts to sell the given stack. The recipient is resolved in this order: the player bound to this upgrade,
     * the supplied fallback player, the current pickup context, then the current magnet context.
     *
     * @return {@link ItemStack#EMPTY} when the stack was sold, otherwise the original stack
     */
    public ItemStack trySell(ItemStack stack, @Nullable ServerPlayer fallbackPlayer, boolean simulate) {
        if (!isEnabled() || stack.isEmpty() || stack.getMaxStackSize() <= 0
                || !filterLogic.matchesFilter(stack)) {
            return stack;
        }

        SellTarget target = resolveTarget(fallbackPlayer);
        if (target == null) {
            return stack;
        }

        PriceQuote quote = ConfluenceCurrencyCombatCompat.resolve(stack);
        if (quote == null || quote.currency() == null || quote.currency().isBlank()
                || !Double.isFinite(quote.price()) || quote.price() <= 0) {
            return stack;
        }

        double total = quote.price() * stack.getCount();
        if (!Double.isFinite(total) || total <= 0) {
            return stack;
        }

        if (simulate) {
            return ItemStack.EMPTY;
        }

        CurrencyService currency = QShopAddonApi.currency();
        double before = currency.getBalance(target.server(), target.playerId(), quote.currency());
        double after = currency.deposit(target.server(), target.playerId(), quote.currency(), total,
                QShopSellUpgradeMod.AUTO_SELL_SOURCE, null);
        if (!Double.isFinite(after) || after + EPSILON < before + total) {
            return stack;
        }

        return ItemStack.EMPTY;
    }

    private @Nullable SellTarget resolveTarget(@Nullable ServerPlayer fallbackPlayer) {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null || !server.isSameThread()) {
            return null;
        }

        SellUpgradeItem.BoundPlayer bound = SellUpgradeItem.getBoundPlayer(upgrade);
        if (bound != null) {
            return new SellTarget(server, bound.uuid());
        }

        ServerPlayer player = fallbackPlayer;
        if (player == null) {
            player = PickupContext.currentPlayer();
        }
        if (player == null) {
            player = MagnetPickupContext.currentPlayer();
        }
        return player == null ? null : new SellTarget(server, player.getUUID());
    }

    @Override
    public ContentsFilterLogic getFilterLogic() {
        return filterLogic;
    }

    private record SellTarget(MinecraftServer server, UUID playerId) {
    }
}
