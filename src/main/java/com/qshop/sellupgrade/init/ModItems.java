package com.qshop.sellupgrade.init;

import com.qshop.sellupgrade.client.ClientSetup;
import com.qshop.sellupgrade.upgrades.AdvancedSellUpgradeItem;
import com.qshop.sellupgrade.upgrades.SellUpgradeItem;
import com.qshop.sellupgrade.upgrades.SellUpgradeWrapper;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.p3pp3rf1y.sophisticatedcore.common.gui.UpgradeContainerRegistry;
import net.p3pp3rf1y.sophisticatedcore.common.gui.UpgradeContainerType;
import net.p3pp3rf1y.sophisticatedcore.upgrades.ContentsFilteredUpgradeContainer;

import static com.qshop.sellupgrade.QShopSellUpgradeMod.MOD_ID;

public final class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MOD_ID);
    public static final DeferredHolder<Item, SellUpgradeItem> SELL_UPGRADE =
            ITEMS.register("sell_upgrade", SellUpgradeItem::new);
    public static final DeferredHolder<Item, AdvancedSellUpgradeItem> ADVANCED_SELL_UPGRADE =
            ITEMS.register("advanced_sell_upgrade", AdvancedSellUpgradeItem::new);

    public static final UpgradeContainerType<SellUpgradeWrapper, ContentsFilteredUpgradeContainer<SellUpgradeWrapper>> SELL_UPGRADE_CONTAINER_TYPE =
            new UpgradeContainerType<>(ContentsFilteredUpgradeContainer::new);
    public static final UpgradeContainerType<SellUpgradeWrapper, ContentsFilteredUpgradeContainer<SellUpgradeWrapper>>
            ADVANCED_SELL_UPGRADE_CONTAINER_TYPE = new UpgradeContainerType<>(ContentsFilteredUpgradeContainer::new);

    private ModItems() {
    }

    public static void registerContainers(RegisterEvent event) {
        if (!event.getRegistryKey().equals(Registries.MENU)) {
            return;
        }

        UpgradeContainerRegistry.register(SELL_UPGRADE.getId(), SELL_UPGRADE_CONTAINER_TYPE);
        UpgradeContainerRegistry.register(ADVANCED_SELL_UPGRADE.getId(), ADVANCED_SELL_UPGRADE_CONTAINER_TYPE);
        if (FMLEnvironment.getDist() == Dist.CLIENT) {
            ClientSetup.register();
        }
    }

    public static void buildCreativeTabContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab() != net.p3pp3rf1y.sophisticatedbackpacks.init.ModItems.CREATIVE_TAB.get()) {
            return;
        }

        event.accept(SELL_UPGRADE);
        event.accept(ADVANCED_SELL_UPGRADE);
    }
}
