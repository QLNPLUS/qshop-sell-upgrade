package com.qshop.sellupgrade;

import com.qshop.sellupgrade.events.PickupContextEvents;
import com.qshop.sellupgrade.init.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;

@Mod(QShopSellUpgradeMod.MOD_ID)
public final class QShopSellUpgradeMod {
    public static final String MOD_ID = "qshop_sell_upgrade";
    public static final ResourceLocation AUTO_SELL_SOURCE =
            ResourceLocation.fromNamespaceAndPath(MOD_ID, "auto_sell");

    public QShopSellUpgradeMod(IEventBus modEventBus, ModContainer modContainer) {
        ModItems.ITEMS.register(modEventBus);
        modEventBus.addListener(ModItems::registerContainers);
        NeoForge.EVENT_BUS.register(new PickupContextEvents());
    }
}
