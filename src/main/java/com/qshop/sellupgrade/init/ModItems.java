package com.qshop.sellupgrade.init;

import com.qshop.sellupgrade.client.ClientSetup;
import com.qshop.sellupgrade.upgrades.AdvancedSellUpgradeItem;
import com.qshop.sellupgrade.upgrades.SellUpgradeItem;
import com.qshop.sellupgrade.upgrades.SellUpgradeWrapper;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;
import net.p3pp3rf1y.sophisticatedcore.common.gui.UpgradeContainerRegistry;
import net.p3pp3rf1y.sophisticatedcore.common.gui.UpgradeContainerType;
import net.p3pp3rf1y.sophisticatedcore.upgrades.ContentsFilteredUpgradeContainer;

import static com.qshop.sellupgrade.QShopSellUpgradeMod.MOD_ID;

public final class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    public static final RegistryObject<SellUpgradeItem> SELL_UPGRADE = ITEMS.register("sell_upgrade", SellUpgradeItem::new);
    public static final RegistryObject<AdvancedSellUpgradeItem> ADVANCED_SELL_UPGRADE =
            ITEMS.register("advanced_sell_upgrade", AdvancedSellUpgradeItem::new);

    public static final UpgradeContainerType<SellUpgradeWrapper, ContentsFilteredUpgradeContainer<SellUpgradeWrapper>> SELL_UPGRADE_CONTAINER_TYPE =
            new UpgradeContainerType<>(ContentsFilteredUpgradeContainer::new);
    public static final UpgradeContainerType<SellUpgradeWrapper, ContentsFilteredUpgradeContainer<SellUpgradeWrapper>>
            ADVANCED_SELL_UPGRADE_CONTAINER_TYPE = new UpgradeContainerType<>(ContentsFilteredUpgradeContainer::new);

    private ModItems() {
    }

    public static void registerContainers(RegisterEvent event) {
        if (!event.getRegistryKey().equals(ForgeRegistries.Keys.MENU_TYPES)) {
            return;
        }

        UpgradeContainerRegistry.register(SELL_UPGRADE.getId(), SELL_UPGRADE_CONTAINER_TYPE);
        UpgradeContainerRegistry.register(ADVANCED_SELL_UPGRADE.getId(), ADVANCED_SELL_UPGRADE_CONTAINER_TYPE);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> ClientSetup::register);
    }

    public static void buildCreativeTabContents(BuildCreativeModeTabContentsEvent event) {
        if (!event.getTabKey().equals(net.p3pp3rf1y.sophisticatedbackpacks.init.ModItems.CREATIVE_TAB.getKey())) {
            return;
        }

        event.accept(SELL_UPGRADE);
        event.accept(ADVANCED_SELL_UPGRADE);
    }
}
