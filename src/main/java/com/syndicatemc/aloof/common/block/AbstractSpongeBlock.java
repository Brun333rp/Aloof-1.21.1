package com.syndicatemc.aloof.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;

public abstract class AbstractSpongeBlock extends Block {
    private static final Direction[] ALL_DIRECTIONS = Direction.values();

    public AbstractSpongeBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    protected int getMaxDepth() {
        return 6;
    }

    protected int getMaxCount() {
        return 64;
    }

    protected boolean isWaterSponge() {
        return true;
    }

    protected TagKey<Fluid> getFluid() {
        return FluidTags.WATER;
    }

    protected BlockState getSaturatedBlock() {
        return Blocks.WET_SPONGE.defaultBlockState();
    }

    protected SoundEvent getSound() {
        return SoundEvents.SPONGE_ABSORB;
    }

    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!oldState.is(state.getBlock())) {
            this.tryAbsorbFluid(level, pos);
        }
    }

    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        this.tryAbsorbFluid(level, pos);
        super.neighborChanged(state, level, pos, block, fromPos, isMoving);
    }

    protected void tryAbsorbFluid(Level level, BlockPos pos) {
        if (this.removeWaterBreadthFirstSearch(level, pos)) {
            level.setBlock(pos, getSaturatedBlock(), 2);
            level.playSound(null, pos, getSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    private boolean removeWaterBreadthFirstSearch(Level level, BlockPos pos) {
        BlockState spongeState = level.getBlockState(pos);
        return BlockPos.breadthFirstTraversal(pos, getMaxDepth(), getMaxCount() + 1, (otherPos, consumer) -> {
            for(Direction direction : ALL_DIRECTIONS) {
                consumer.accept(otherPos.relative(direction));
            }
        }, (otherPos) -> {
            if (otherPos.equals(pos)) {
                return true;
            } else {
                BlockState state = level.getBlockState(otherPos);
                FluidState fluidState = level.getFluidState(otherPos);
                if (this.isWaterSponge() ? !spongeState.canBeHydrated(level, pos, fluidState, otherPos) : !fluidState.is(getFluid())) {
                    return false;
                } else {
                    Block block = state.getBlock();
                    if (block instanceof BucketPickup bucketPickup) {
                        if (!bucketPickup.pickupBlock(null, level, otherPos, state).isEmpty()) {
                            return true;
                        }
                    }

                    if (state.getBlock() instanceof LiquidBlock) {
                        level.setBlock(otherPos, Blocks.AIR.defaultBlockState(), 3);
                    } else {
                        if (!state.is(Blocks.KELP) && !state.is(Blocks.KELP_PLANT) && !state.is(Blocks.SEAGRASS) && !state.is(Blocks.TALL_SEAGRASS)) {
                            return false;
                        }

                        BlockEntity blockentity = state.hasBlockEntity() ? level.getBlockEntity(otherPos) : null;
                        dropResources(state, level, otherPos, blockentity);
                        level.setBlock(otherPos, Blocks.AIR.defaultBlockState(), 3);
                    }

                    return true;
                }
            }
        }) > 1;
    }
}
