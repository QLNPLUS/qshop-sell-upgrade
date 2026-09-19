package com.qshop.sellupgrade.upgrades;

import net.p3pp3rf1y.sophisticatedcore.upgrades.IUpgradeCountLimitConfig;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeItemBase;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeType;
import net.p3pp3rf1y.sophisticatedcore.upgrades.IUpgradeItem.UpgradeConflictDefinition;

import javax.annotation.Nullable;
import java.util.List;

public class SellUpgradeItem extends UpgradeItemBase<SellUpgradeWrapper> {
    public static final int FILTER_SLOTS = 9;
    public static final UpgradeType<SellUpgradeWrapper> TYPE = new UpgradeType<>(SellUpgradeWrapper::new);

    private static final IUpgradeCountLimitConfig LIMIT_CONFIG = new IUpgradeCountLimitConfig() {
        @Override
        public int getMaxUpgradesPerStorage(String storageType, @Nullable net.minecraft.resources.ResourceLocation upgradeRegistryName) {
            return 1;
        }

        @Override
        public int getMaxUpgradesInGroupPerStorage(String storageType, net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeGroup upgradeGroup) {
            return Integer.MAX_VALUE;
        }
    };

    public SellUpgradeItem() {
        super(LIMIT_CONFIG);
    }

    public int getFilterSlotCount() {
        return FILTER_SLOTS;
    }

    @Override
    public UpgradeType<SellUpgradeWrapper> getType() {
        return TYPE;
    }

    @Override
    public List<UpgradeConflictDefinition> getUpgradeConflicts() {
        return List.of();
    }
}
