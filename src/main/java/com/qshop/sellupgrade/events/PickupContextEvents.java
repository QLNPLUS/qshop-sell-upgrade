package com.qshop.sellupgrade.events;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;

public final class PickupContextEvents {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void beforePickup(ItemEntityPickupEvent.Pre event) {
        if (event.getPlayer() instanceof ServerPlayer player) {
            PickupContext.set(player);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void afterPickup(ItemEntityPickupEvent.Post event) {
        PickupContext.clear();
    }
}
