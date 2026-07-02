package com.syndicatemc.aloof.common.block;

import com.syndicatemc.aloof.core.registry.AloofBlocks;
import com.syndicatemc.aloof.core.registry.AloofItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.common.ItemAbilities;

import javax.annotation.Nullable;

public class LoofahGourdBlock extends Block {
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

    public LoofahGourdBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(HALF, DoubleBlockHalf.LOWER));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HALF);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState belowState = context.getLevel().getBlockState(context.getClickedPos().below());
        return this.checkHalf(this.defaultBlockState(), belowState.is(this) && belowState.getValue(HALF) == DoubleBlockHalf.LOWER);
    }

    public BlockState checkHalf(BlockState state, boolean bool) {
        return state.setValue(HALF, bool ? DoubleBlockHalf.UPPER : DoubleBlockHalf.LOWER);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        if (!stack.canPerformAction(ItemAbilities.AXE_STRIP)) {
            return super.useItemOn(stack, state, level, pos, player, hand, result);
        } else if (level.isClientSide) {
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        } else {
            Direction direction = result.getDirection();
            Direction seedDir = direction.getAxis() == Direction.Axis.Y ? player.getDirection().getOpposite() : direction;
            level.playSound(null, pos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.setBlock(pos, AloofBlocks.LOOFAH_SPONGE.get().defaultBlockState(), 11);
            if (level.getRandom().nextFloat() < 0.4) {
                ItemEntity seed = new ItemEntity(level, pos.getX() + 0.5F + seedDir.getStepX() * 0.65, pos.getY() + 0.1, pos.getZ() + 0.5F + seedDir.getStepZ() * 0.65, new ItemStack(AloofItems.LOOFAH_SEEDS.get()));
                seed.setDeltaMovement(0.05 * seedDir.getStepX() + level.random.nextDouble() * 0.02, 0.05, 0.05 * seedDir.getStepZ() + level.random.nextDouble() * 0.02);
                level.addFreshEntity(seed);
            }
            stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
            level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }
    }
}
