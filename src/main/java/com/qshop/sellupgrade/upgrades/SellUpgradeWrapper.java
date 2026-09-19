package com.qshop.sellupgrade.upgrades;

import com.qshop.api.CurrencyService;
import com.qshop.api.QShopAddonApi;
import com.qshop.sellbox.PriceQuote;
import com.qshop.sellbox.SellBoxPrices;
import com.qshop.sellupgrade.QShopSellUpgradeMod;
import com.qshop.sellupgrade.events.PickupContext;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import net.p3pp3rf1y.sophisticatedcore.api.IStorageWrapper;
import net.p3pp3rf1y.sophisticatedcore.init.ModCoreDataComponents;
import net.p3pp3rf1y.sophisticatedcore.settings.memory.MemorySettingsCategory;
import net.p3pp3rf1y.sophisticatedcore.upgrades.ContentsFilterLogic;
import net.p3pp3rf1y.sophisticatedcore.upgrades.IContentsFilteredUpgrade;
import net.p3pp3rf1y.sophisticatedcore.upgrades.IPickupResponseUpgrade;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeWrapperBase;

import java.util.function.Consumer;

public final class SellUpgradeWrapper extends UpgradeWrapperBase<SellUpgradeWrapper, SellUpgradeItem>
        implements IPickupResponseUpgrade, IContentsFilteredUpgrade {
    private static final double EPSILON = 1.0E-9D;

    private final ContentsFilterLogic filterLogic;

    public SellUpgradeWrapper(IStorageWrapper storageWrapper, ItemStack upgrade, Consumer<ItemStack> upgradeSaveHandler) {
        super(storageWrapper, upgrade, upgradeSaveHandler);
        int filterSlotCount = upgrade.getItem() instanceof SellUpgradeItem sellUpgradeItem
                ? sellUpgradeItem.getFilterSlotCount()
                : SellUpgradeItem.FILTER_SLOTS;
        filterLogic = new ContentsFilterLogic(upgrade, stack -> save(), filterSlotCount,
                storageWrapper::getInventoryHandler,
                storageWrapper.getSettingsHandler().getTypeCategory(MemorySettingsCategory.class),
                ModCoreDataComponents.FILTER_ATTRIBUTES);
    }

    @Override
    public int pickup(Level world, ItemResource resource, int amount, TransactionContext transaction) {
        if (!isEnabled() || world.isClientSide() || resource.isEmpty() || amount <= 0
                || !filterLogic.matchesFilter(resource)) {
            return 0;
        }

        ServerPlayer player = PickupContext.currentPlayer();
        if (player == null || player.level() != world) {
            return 0;
        }

        ItemStack stack = resource.toStack(amount);
        PriceQuote quote = SellBoxPrices.resolve(stack);
        if (quote == null || quote.currency() == null || quote.currency().isBlank()
                || !Double.isFinite(quote.price()) || quote.price() <= 0) {
            return 0;
        }

        double total = quote.price() * amount;
        if (!Double.isFinite(total) || total <= 0) {
            return 0;
        }

        CurrencyService currency = QShopAddonApi.currency();
        double before = currency.getBalance(player, quote.currency());
        double after = currency.deposit(player, quote.currency(), total,
                QShopSellUpgradeMod.AUTO_SELL_SOURCE, null);
        if (!Double.isFinite(after) || after + EPSILON < before + total) {
            return 0;
        }

        return amount;
    }

    @Override
    public ContentsFilterLogic getFilterLogic() {
        return filterLogic;
    }
}
