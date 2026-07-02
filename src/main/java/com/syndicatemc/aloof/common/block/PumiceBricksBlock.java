package com.syndicatemc.aloof.common.block;

import com.syndicatemc.aloof.core.registry.AloofBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import javax.annotation.Nullable;
import java.util.List;

public class PumiceBricksBlock extends Block {
    public static final IntegerProperty SATURATED = IntegerProperty.create("saturated", 0, 3);
    List<BlockPos> OFFSET = List.of(
            new BlockPos(+1, 0, 0),
            new BlockPos(-1, 0, 0),
            new BlockPos(0, +1, 0),
            new BlockPos(0, -1, 0),
            new BlockPos(0, 0, +1),
            new BlockPos(0, 0, -1)
    );

    public PumiceBricksBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(SATURATED, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SATURATED);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(SATURATED, getLavaLevel(context.getLevel(), context.getClickedPos()));
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);
        level.scheduleTick(pos, this, level.random.nextInt(10));
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean isMoving) {
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, isMoving);
        level.scheduleTick(pos, this, level.random.nextInt(10));
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int currentLevel = state.getValue(SATURATED);
        int nextLevel = getLavaLevel(level, pos);

        if (currentLevel != nextLevel) {
            level.setBlock(pos, state.setValue(SATURATED, getLavaLevel(level, pos)), 11);
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(SATURATED) > random.nextInt(3)) {
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
    }

    private int getLavaLevel(Level level, BlockPos pos) {
        var touching = OFFSET.stream()
                .map(pos::offset)
                .mapToInt(touchingPos -> {
                    BlockState touchingState = level.getBlockState(touchingPos);
                    if (level.getFluidState(touchingPos).is(FluidTags.LAVA) || touchingState.is(AloofBlocks.SOAKED_PUMICE)) return 3;
                    if (touchingState.is(Blocks.MAGMA_BLOCK)) return 2;
                    if (touchingState.getBlock() instanceof PumiceBricksBlock) return Math.max(touchingState.getValue(SATURATED) - 1, 0);
                    return 0;
                })
                .max();
        return touching.orElse(0);
    }
}
