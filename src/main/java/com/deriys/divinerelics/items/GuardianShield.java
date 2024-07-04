package com.deriys.divinerelics.items;

import com.deriys.divinerelics.init.DRSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.deriys.divinerelics.items.DraupnirSpear.RAND;

public class GuardianShield extends ShieldItem {


    public static final int EFFECTIVE_BLOCK_DELAY = 5;
    public static final float MINIMUM_DURABILITY_DAMAGE = 3.0F;
    public static final String TAG_BASE_COLOR = "Base";

    public GuardianShield(Properties p_41383_) {
        super(p_41383_);
        DispenserBlock.registerBehavior(this, ArmorItem.DISPENSE_ITEM_BEHAVIOR);
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        return ToolActions.DEFAULT_SHIELD_ACTIONS.contains(toolAction) || super.canPerformAction(stack, toolAction);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack p_43105_) {
        return UseAnim.BLOCK;
    }

    @Override
    public int getUseDuration(ItemStack p_43107_) {
        return 72000;
    }

    public InteractionResultHolder<ItemStack> use(Level p_43099_, Player player, InteractionHand p_43101_) {
        ItemStack itemstack = player.getItemInHand(p_43101_);
        player.startUsingItem(p_43101_);
        if (player.isUsingItem() && player.getUseItem() == itemstack) {
            player.getLevel().playSound(null, player.getOnPos(), DRSounds.GUARDIAN_SHIELD_OPEN.get(), SoundSource.PLAYERS, 1.0f, RAND.nextFloat() * 0.1F + 0.95F);
        }
        return InteractionResultHolder.consume(itemstack);
    }

    @Override
    public boolean isValidRepairItem(ItemStack p_43091_, ItemStack p_43092_) {
        return p_43092_.is(ItemTags.PLANKS) || super.isValidRepairItem(p_43091_, p_43092_);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            components.add(Component.literal("The legendary Kratos's shield, provides unyielding protection against the fiercest of foes. With precise timing, it can parry incoming attacks, turning the enemy’s strength against them."));
        } else {
            components.add(Component.literal("Press SHIFT for more info").withStyle(ChatFormatting.YELLOW));
        }
    }
}
