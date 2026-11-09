package com.qshop.sellupgrade.mixin;

import com.qshop.sellupgrade.events.PickupContext;
import com.qshop.sellupgrade.upgrades.SellUpgradeItem;
import com.qshop.sellupgrade.upgrades.SellUpgradeWrapper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeHandler;
import net.p3pp3rf1y.sophisticatedcore.util.InventoryHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * The pickup response upgrades are iterated in upgrade-slot order. A Magnet Upgrade placed before the Sell Upgrade
 * would otherwise insert the item first, so the Sell Upgrade never sees it. Run the Sell Upgrade in the same storage
 * before the pickup chain starts, making the result independent of upgrade order.
 */
@Mixin(InventoryHelper.class)
public abstract class InventoryHelperMixin {
    @Inject(
            method = "runPickupOnPickupResponseUpgrades(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/player/Player;Lnet/p3pp3rf1y/sophisticatedcore/upgrades/UpgradeHandler;Lnet/minecraft/world/item/ItemStack;Z)Lnet/minecraft/world/item/ItemStack;",
            at = @At("HEAD"), cancellable = true, remap = false)
    private static void qshopSellUpgrade$sellBeforePickupUpgrades(Level level, Player player,
            UpgradeHandler upgradeHandler, ItemStack stack, boolean simulate,
            CallbackInfoReturnable<ItemStack> cir) {
        ServerPlayer serverPlayer = player instanceof ServerPlayer currentPlayer
                ? currentPlayer
                : PickupContext.currentPlayer();
        if (qshopSellUpgrade$trySell(serverPlayer, upgradeHandler, stack, simulate)) {
            cir.setReturnValue(ItemStack.EMPTY);
        }
    }

    @Inject(
            method = "runPickupOnPickupResponseUpgrades(Lnet/minecraft/world/level/Level;Lnet/p3pp3rf1y/sophisticatedcore/upgrades/UpgradeHandler;Lnet/minecraft/world/item/ItemStack;Z)Lnet/minecraft/world/item/ItemStack;",
            at = @At("HEAD"), cancellable = true, remap = false)
    private static void qshopSellUpgrade$sellBeforePickupUpgradesWithoutPlayer(Level level,
            UpgradeHandler upgradeHandler, ItemStack stack, boolean simulate,
            CallbackInfoReturnable<ItemStack> cir) {
        if (qshopSellUpgrade$trySell(PickupContext.currentPlayer(), upgradeHandler, stack, simulate)) {
            cir.setReturnValue(ItemStack.EMPTY);
        }
    }

    private static boolean qshopSellUpgrade$trySell(ServerPlayer player,
            UpgradeHandler upgradeHandler, ItemStack stack, boolean simulate) {
        if (stack.isEmpty() || stack.getMaxStackSize() <= 0) {
            return false;
        }

        for (SellUpgradeWrapper sellUpgrade : upgradeHandler.getTypeWrappers(SellUpgradeItem.TYPE)) {
            if (sellUpgrade.trySell(stack, player, simulate).isEmpty()) {
                return true;
            }
        }
        return false;
    }
}
