package com.qshop.sellupgrade.client;

import com.qshop.sellupgrade.upgrades.SellUpgradeWrapper;
import net.minecraft.network.chat.Component;
import net.p3pp3rf1y.sophisticatedcore.client.gui.StorageScreenBase;
import net.p3pp3rf1y.sophisticatedcore.client.gui.UpgradeSettingsTab;
import net.p3pp3rf1y.sophisticatedcore.client.gui.controls.ButtonDefinition;
import net.p3pp3rf1y.sophisticatedcore.client.gui.controls.ButtonDefinitions;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.Dimension;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.Position;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.TranslationHelper;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.UV;
import net.p3pp3rf1y.sophisticatedcore.upgrades.ContentsFilterControl;
import net.p3pp3rf1y.sophisticatedcore.upgrades.ContentsFilterType;
import net.p3pp3rf1y.sophisticatedcore.upgrades.ContentsFilteredUpgradeContainer;

import java.util.Map;

import static net.p3pp3rf1y.sophisticatedcore.client.gui.utils.GuiHelper.getButtonStateData;

final class SellUpgradeTab extends UpgradeSettingsTab<ContentsFilteredUpgradeContainer<SellUpgradeWrapper>> {
    private static final ButtonDefinition.Toggle<ContentsFilterType> FILTER_TYPE_BUTTON =
            ButtonDefinitions.createToggleButtonDefinition(Map.of(
                    ContentsFilterType.ALLOW,
                    getButtonStateData(new UV(0, 0), TranslationHelper.INSTANCE.translUpgradeButton("allow"),
                            Dimension.SQUARE_16, new Position(1, 1)),
                    ContentsFilterType.BLOCK,
                    getButtonStateData(new UV(16, 0), TranslationHelper.INSTANCE.translUpgradeButton("block"),
                            Dimension.SQUARE_16, new Position(1, 1)),
                    ContentsFilterType.STORAGE,
                    getButtonStateData(new UV(80, 16), TranslationHelper.INSTANCE.translUpgradeButton("match_backpack_contents"),
                            Dimension.SQUARE_16, new Position(1, 1))));

    private final ContentsFilterControl filterLogicControl;

    SellUpgradeTab(ContentsFilteredUpgradeContainer<SellUpgradeWrapper> container, Position position,
                   StorageScreenBase<?> screen) {
        super(container, position, screen,
                Component.translatable("gui.qshop_sell_upgrade.upgrades.sell"),
                Component.translatable("gui.qshop_sell_upgrade.upgrades.sell.tooltip"));
        filterLogicControl = addHideableChild(new ContentsFilterControl.Basic(screen,
                new Position(x + 3, y + 24), getContainer().getFilterLogicContainer(), 3, FILTER_TYPE_BUTTON));
    }

    @Override
    protected void moveSlotsToTab() {
        filterLogicControl.moveSlotsToView();
    }
}
