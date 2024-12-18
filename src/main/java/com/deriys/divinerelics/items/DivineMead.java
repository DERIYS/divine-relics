package com.deriys.divinerelics.items;

import com.deriys.divinerelics.entities.entity.ThorEntity;
import com.deriys.divinerelics.init.DREntitiyTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.phys.Vec3;

public class DivineMead extends Item {
    public DivineMead(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        Player player = context.getPlayer();
        Level level = context.getLevel();
        Block clickedBlock = level.getBlockState(context.getClickedPos()).getBlock();
        if (!level.isClientSide && level.getDifficulty() != Difficulty.PEACEFUL && player != null && player.getMainHandItem() == stack && context.getClickedFace() == Direction.UP && clickedBlock != Blocks.LAVA) {
            ThorEntity thor = new ThorEntity(DREntitiyTypes.THOR.get(), level);
            if (clickedBlock instanceof SlabBlock || clickedBlock instanceof SnowLayerBlock) {
                thor.setPos(Vec3.atCenterOf(context.getClickedPos()).add(0D, 10.1D, 0D));
            } else {
                thor.setPos(Vec3.atCenterOf(context.getClickedPos()).add(0D, 11D, 0D));
            }
            thor.setSummoningComplete(false);
            level.addFreshEntity(thor);
            if (!player.isCreative()) {
                player.getInventory().removeItem(stack);
            }
        } else if (level.getDifficulty() == Difficulty.PEACEFUL && !level.isClientSide && player != null) {
            player.sendSystemMessage(Component.literal("The air shimmers, but something seems to prevent Thor from emerging. Maybe try altering the realm's conditions for his arrival...").withStyle(ChatFormatting.DARK_RED));
        }
        return InteractionResult.SUCCESS;
    }
}
