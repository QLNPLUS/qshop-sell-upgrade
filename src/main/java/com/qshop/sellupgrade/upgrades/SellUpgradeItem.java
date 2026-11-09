package com.qshop.sellupgrade.upgrades;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.p3pp3rf1y.sophisticatedcore.upgrades.IUpgradeCountLimitConfig;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeItemBase;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeType;
import net.p3pp3rf1y.sophisticatedcore.upgrades.IUpgradeItem.UpgradeConflictDefinition;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class SellUpgradeItem extends UpgradeItemBase<SellUpgradeWrapper> {
    public static final int FILTER_SLOTS = 9;
    public static final UpgradeType<SellUpgradeWrapper> TYPE = new UpgradeType<>(SellUpgradeWrapper::new);
    private static final String BOUND_UUID_KEY = "qshop_sell_upgrade_bound_player_uuid";
    private static final String BOUND_NAME_KEY = "qshop_sell_upgrade_bound_player_name";

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

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!player.isShiftKeyDown()) {
            return InteractionResultHolder.pass(stack);
        }
        if (level.isClientSide) {
            return InteractionResultHolder.success(stack);
        }

        BoundPlayer bound = getBoundPlayer(stack);
        if (bound != null && bound.uuid().equals(player.getUUID())) {
            setBoundPlayer(stack, null, null);
            player.displayClientMessage(Component.translatable("message.qshop_sell_upgrade.unbound"), true);
        } else {
            String name = player.getGameProfile().getName();
            setBoundPlayer(stack, player.getUUID(), name);
            player.displayClientMessage(Component.translatable("message.qshop_sell_upgrade.bound", name), true);
        }
        return InteractionResultHolder.success(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        BoundPlayer bound = getBoundPlayer(stack);
        if (bound == null) {
            tooltip.add(Component.translatable("tooltip.qshop_sell_upgrade.unbound").withStyle(ChatFormatting.DARK_GRAY));
        } else {
            String name = bound.name().isBlank() ? bound.uuid().toString() : bound.name();
            tooltip.add(Component.translatable("tooltip.qshop_sell_upgrade.bound_player", name)
                    .withStyle(ChatFormatting.GRAY));
        }
    }

    public static @Nullable BoundPlayer getBoundPlayer(ItemStack stack) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        if (data == null) {
            return null;
        }
        CompoundTag tag = data.copyTag();
        if (!tag.contains(BOUND_UUID_KEY)) {
            return null;
        }
        try {
            UUID uuid = UUID.fromString(tag.getString(BOUND_UUID_KEY));
            return new BoundPlayer(uuid, tag.getString(BOUND_NAME_KEY));
        } catch (IllegalArgumentException exception) {
            return null;
        }
    }

    public static void setBoundPlayer(ItemStack stack, @Nullable UUID uuid, @Nullable String name) {
        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> {
            if (uuid == null) {
                tag.remove(BOUND_UUID_KEY);
                tag.remove(BOUND_NAME_KEY);
            } else {
                tag.putString(BOUND_UUID_KEY, uuid.toString());
                tag.putString(BOUND_NAME_KEY, name == null ? "" : name);
            }
        });
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        if (data != null && data.copyTag().isEmpty()) {
            stack.remove(DataComponents.CUSTOM_DATA);
        }
    }

    public record BoundPlayer(UUID uuid, String name) {
    }
}
