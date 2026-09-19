package com.qshop.sellupgrade.upgrades;

public final class AdvancedSellUpgradeItem extends SellUpgradeItem {
    public static final int FILTER_SLOTS = 16;

    @Override
    public int getFilterSlotCount() {
        return FILTER_SLOTS;
    }
}
