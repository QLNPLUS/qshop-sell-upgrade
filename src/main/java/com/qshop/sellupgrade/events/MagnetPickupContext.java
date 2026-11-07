package com.qshop.sellupgrade.events;

import net.minecraft.server.level.ServerPlayer;

/**
 * Carries the player performing a magnet upgrade tick. The 1.20.1 magnet implementation does not pass the player down to
 * its item insertion method, so the mixin stores it while the upgrade processes nearby item entities.
 */
public final class MagnetPickupContext {
    private static final ThreadLocal<ServerPlayer> CURRENT_PLAYER = new ThreadLocal<>();

    private MagnetPickupContext() {
    }

    public static void set(ServerPlayer player) {
        CURRENT_PLAYER.set(player);
    }

    public static ServerPlayer currentPlayer() {
        return CURRENT_PLAYER.get();
    }

    public static void clear() {
        CURRENT_PLAYER.remove();
    }
}
