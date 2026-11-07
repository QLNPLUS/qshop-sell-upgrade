package com.qshop.sellupgrade.mixin;

import com.qshop.sellupgrade.api.StorageWrapperAccessor;
import net.p3pp3rf1y.sophisticatedcore.api.IStorageWrapper;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeWrapperBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(UpgradeWrapperBase.class)
public abstract class UpgradeWrapperBaseMixin implements StorageWrapperAccessor {
    @Shadow(remap = false)
    protected IStorageWrapper storageWrapper;

    @Override
    public IStorageWrapper qshopSellUpgrade$getStorageWrapper() {
        return storageWrapper;
    }
}
