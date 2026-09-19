package com.qshop.sellupgrade.events;

import net.minecraft.server.level.ServerPlayer;

public final class PickupContext {
    private static final ThreadLocal<ServerPlayer> CURRENT_PLAYER = new ThreadLocal<>();

    private PickupContext() {
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
