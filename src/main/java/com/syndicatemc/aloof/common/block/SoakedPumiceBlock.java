package com.syndicatemc.aloof.common.block;

import com.syndicatemc.aloof.core.registry.AloofBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WetSpongeBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

public class SoakedPumiceBlock extends WetSpongeBlock {
    public SoakedPumiceBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        checkIfShouldSolidify(level, pos, state, oldState.getFluidState());
    }

    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        checkIfShouldSolidify(level, pos, state, level.getFluidState(neighborPos));
    }

    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        Direction direction = Direction.getRandom(random);
        if (direction != Direction.UP) {
            BlockPos relativePos = pos.relative(direction);
            BlockState relativeState = level.getBlockState(relativePos);
            if (!state.canOcclude() || !relativeState.isFaceSturdy(level, relativePos, direction.getOpposite())) {
                double posX = pos.getX();
                double posY = pos.getY();
                double posZ = pos.getZ();
                if (direction == Direction.DOWN) {
                    posY -= 0.05;
                    posX += random.nextDouble();
                    posZ += random.nextDouble();
                } else {
                    posY += random.nextDouble() * 0.8;
                    if (direction.getAxis() == Direction.Axis.X) {
                        posZ += random.nextDouble();
                        if (direction == Direction.EAST) {
                            ++posX;
                        } else {
                            posX += 0.05;
                        }
                    } else {
                        posX += random.nextDouble();
                        if (direction == Direction.SOUTH) {
                            ++posZ;
                        } else {
                            posZ += 0.05;
                        }
                    }
                }
                level.addParticle(ParticleTypes.DRIPPING_LAVA, posX, posY, posZ, 0.0F, 0.0F, 0.0F);
            }
        }
    }

    private void checkIfShouldSolidify(Level level, BlockPos pos, BlockState state, FluidState fluidState) {
        if (shouldSolidify(level, pos, state, fluidState)) {
            level.setBlock(pos, AloofBlocks.SMOOTH_PUMICE.get().defaultBlockState(), 11);
            level.levelEvent(2009, pos, 0);
            level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, (1.0F + level.getRandom().nextFloat() * 0.2F) * 0.7F);
        }
    }

    private static boolean shouldSolidify(Level level, BlockPos pos, BlockState state, FluidState fluidState) {
        return state.canBeHydrated(level, pos, fluidState, pos) || touchesLiquid(level, pos, state);
    }

    private static boolean touchesLiquid(Level level, BlockPos pos, BlockState state) {
        boolean flag = false;
        BlockPos.MutableBlockPos mutable = pos.mutable();
        for (Direction direction : Direction.values()) {
            BlockState blockState = level.getBlockState(mutable);
            if (direction != Direction.DOWN || state.canBeHydrated(level, pos, blockState.getFluidState(), mutable)) {
                mutable.setWithOffset(pos, direction);
                blockState = level.getBlockState(mutable);
                if (state.canBeHydrated(level, pos, blockState.getFluidState(), mutable) && !blockState.isFaceSturdy(level, pos, direction.getOpposite())) {
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }
}
