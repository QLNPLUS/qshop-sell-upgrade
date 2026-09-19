package com.qshop.sellupgrade;

import com.qshop.sellupgrade.events.PickupContextEvents;
import com.qshop.sellupgrade.init.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(QShopSellUpgradeMod.MOD_ID)
public final class QShopSellUpgradeMod {
    public static final String MOD_ID = "qshop_sell_upgrade";
    public static final ResourceLocation AUTO_SELL_SOURCE =
            ResourceLocation.fromNamespaceAndPath(MOD_ID, "auto_sell");

    public QShopSellUpgradeMod() {
        var modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.ITEMS.register(modEventBus);
        modEventBus.addListener(ModItems::registerContainers);
        modEventBus.addListener(ModItems::buildCreativeTabContents);
        MinecraftForge.EVENT_BUS.register(new PickupContextEvents());
    }
}
