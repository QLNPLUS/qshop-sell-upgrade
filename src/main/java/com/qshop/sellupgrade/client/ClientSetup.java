package com.qshop.sellupgrade.client;

import com.qshop.sellupgrade.init.ModItems;
import com.qshop.sellupgrade.upgrades.SellUpgradeWrapper;
import net.p3pp3rf1y.sophisticatedcore.client.gui.UpgradeGuiManager;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.Position;
import net.p3pp3rf1y.sophisticatedcore.upgrades.ContentsFilteredUpgradeContainer;

public final class ClientSetup {
    private ClientSetup() {
    }

    public static void register() {
        UpgradeGuiManager.registerTab(ModItems.SELL_UPGRADE_CONTAINER_TYPE,
                (ContentsFilteredUpgradeContainer<SellUpgradeWrapper> container, Position position,
                 net.p3pp3rf1y.sophisticatedcore.client.gui.StorageScreenBase<?> screen) ->
                        new SellUpgradeTab(container, position, screen));
    }
}
