package com.qshop.sellupgrade.events;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public final class PickupContextEvents {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void beforePickup(EntityItemPickupEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            PickupContext.set(player);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void afterPickup(EntityItemPickupEvent event) {
        PickupContext.clear();
    }
}
