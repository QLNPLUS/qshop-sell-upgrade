package com.qshop.sellupgrade.mixin;

import com.qshop.sellupgrade.api.StorageWrapperAccessor;
import com.qshop.sellupgrade.upgrades.SellUpgradeItem;
import com.qshop.sellupgrade.upgrades.SellUpgradeWrapper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.p3pp3rf1y.sophisticatedcore.upgrades.magnet.MagnetUpgradeWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * The magnet upgrade inserts nearby items directly into its storage and therefore never calls
 * {@link net.p3pp3rf1y.sophisticatedcore.upgrades.IPickupResponseUpgrade}s. Give the sell upgrade a chance to handle
 * those items before the magnet stores them.
 */
@Mixin(MagnetUpgradeWrapper.class)
public abstract class MagnetUpgradeWrapperMixin {
    @Inject(method = "tryToInsertItem", at = @At("HEAD"), cancellable = true, remap = false)
    private void qshopSellUpgrade$sellMagnetPickup(Player player, ItemEntity itemEntity,
            CallbackInfoReturnable<Boolean> cir) {
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return;
        }

        ItemStack stack = itemEntity.getItem();
        if (stack.isEmpty()) {
            return;
        }

        Level level = itemEntity.level();
        if (level.isClientSide) {
            return;
        }

        for (SellUpgradeWrapper sellUpgrade : ((StorageWrapperAccessor) this).qshopSellUpgrade$getStorageWrapper()
                .getUpgradeHandler().getTypeWrappers(SellUpgradeItem.TYPE)) {
            ItemStack remaining = sellUpgrade.trySell(stack, serverPlayer, false);
            if (!remaining.isEmpty()) {
                continue;
            }

            itemEntity.setItem(ItemStack.EMPTY);
            itemEntity.discard();
            serverPlayer.awardStat(Stats.ITEM_PICKED_UP.get(stack.getItem()), stack.getCount());
            cir.setReturnValue(true);
            return;
        }
    }
}
