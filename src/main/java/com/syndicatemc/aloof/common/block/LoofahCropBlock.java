package com.syndicatemc.aloof.common.block;

import com.mojang.serialization.MapCodec;
import com.syndicatemc.aloof.core.other.tags.AloofBlockTags;
import com.syndicatemc.aloof.core.registry.AloofBlocks;
import com.syndicatemc.aloof.core.registry.AloofItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.CommonHooks;

public class LoofahCropBlock extends BushBlock implements BonemealableBlock {
    public static final MapCodec<LoofahCropBlock> CODEC = simpleCodec(LoofahCropBlock::new);
    public static final IntegerProperty AGE = BlockStateProperties.AGE_7;
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

    private static final VoxelShape[] SHAPE_LOWER = new VoxelShape[]{
            Block.box(5.0D, 0.0D, 5.0D, 11.0D, 8.0D, 11.0D),
            Block.box(4.0D, 0.0D, 4.0D, 12.0D, 10.0D, 12.0D),
            Block.box(3.0D, 0.0D, 3.0D, 13.0D, 14.0D, 13.0D),
            Block.box(3.0D, 0.0D, 3.0D, 13.0D, 16.0D, 13.0D),
    };
    private static final VoxelShape[] SHAPE_UPPER = new VoxelShape[]{
            Block.box(3.0D, 0.0D, 3.0D, 13.0D, 4.0D, 13.0D),
            Block.box(3.0D, 0.0D, 3.0D, 13.0D, 10.0D, 13.0D),
            Block.box(3.0D, 0.0D, 3.0D, 13.0D, 9.0D, 13.0D),
            Block.box(3.0D, 0.0D, 3.0D, 13.0D, 8.0D, 13.0D),
    };

    public MapCodec<LoofahCropBlock> codec() {
        return CODEC;
    }

    public LoofahCropBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0).setValue(HALF, DoubleBlockHalf.LOWER));
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        return new ItemStack(AloofItems.LOOFAH_SEEDS.get());
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return state.getValue(HALF) != DoubleBlockHalf.UPPER;
    }

    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.getRawBrightness(pos.above(), 0) >= 3 && CommonHooks.canCropGrow(level, pos, state, random.nextInt(5) == 0)) {
            this.grow(level, pos, state);
            CommonHooks.fireCropGrowPost(level, pos, state);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(state));
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE, HALF);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Vec3 offset = state.getOffset(level, pos);
        VoxelShape shape = state.getValue(HALF) == DoubleBlockHalf.LOWER ? SHAPE_LOWER[Math.min(state.getValue(AGE), 3)] : SHAPE_UPPER[state.getValue(AGE) - 4];

        return shape.move(offset.x, offset.y, offset.z);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState belowState = level.getBlockState(pos.below());
        BlockState aboveState = level.getBlockState(pos.above());
        int age = state.getValue(AGE);
        DoubleBlockHalf half = state.getValue(HALF);
        boolean canSurvive = false;
        if (half == DoubleBlockHalf.LOWER) {
            if (age <= 3) canSurvive = belowState.is(AloofBlockTags.LOOFAH_PLANTABLE_ON);
            if (age > 3) canSurvive = belowState.is(AloofBlockTags.LOOFAH_PLANTABLE_ON) && aboveState.is(AloofBlocks.LOOFAH_CROP);
        }
        if (half == DoubleBlockHalf.UPPER) canSurvive = belowState.is(AloofBlocks.LOOFAH_CROP);
        return canSurvive;
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        int age = state.getValue(AGE);
        DoubleBlockHalf half = state.getValue(HALF);
        boolean canBonemeal = false;
        BlockState belowState = level.getBlockState(pos.below());
        BlockState aboveState = level.getBlockState(pos.above());

        if (half == DoubleBlockHalf.LOWER) {
            if (age != 3) canBonemeal = true;
            else if (aboveState.canBeReplaced()) canBonemeal = true;
        }
        else canBonemeal = true;
        return canBonemeal;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        this.grow(level, pos, state);
    }

    private void grow(ServerLevel level, BlockPos pos, BlockState state) {
        int age = state.getValue(AGE);
        DoubleBlockHalf half = state.getValue(HALF);
        int newAge = Math.min(7, age + 1);
        BlockState aboveState = level.getBlockState(pos.above());

        if (half == DoubleBlockHalf.LOWER) {
            if (age < 7 && age > 2 && (aboveState.canBeReplaced() || aboveState.is(this))) {
                level.setBlock(pos.above(), state.setValue(AGE, newAge).setValue(HALF, DoubleBlockHalf.UPPER), 2);
                level.setBlock(pos, state.setValue(AGE, newAge).setValue(HALF, DoubleBlockHalf.LOWER), 2);
            }
            if (age <= 2) level.setBlock(pos, state.setValue(AGE, newAge).setValue(HALF, DoubleBlockHalf.LOWER), 2);
            if (age == 7) {
                level.setBlock(pos, AloofBlocks.LOOFAH_GOURD.get().defaultBlockState().setValue(HALF, DoubleBlockHalf.LOWER), 2);
                level.setBlock(pos.above(), AloofBlocks.LOOFAH_GOURD.get().defaultBlockState().setValue(HALF, DoubleBlockHalf.UPPER), 2);
            }
        }
        if (half == DoubleBlockHalf.UPPER) {
            if (age < 7) {
                level.setBlock(pos.below(), state.setValue(AGE, newAge).setValue(HALF, DoubleBlockHalf.LOWER), 2);
                level.setBlock(pos, state.setValue(AGE, newAge).setValue(HALF, DoubleBlockHalf.UPPER), 2);
            } else {
                level.setBlock(pos.below(), AloofBlocks.LOOFAH_GOURD.get().defaultBlockState().setValue(HALF, DoubleBlockHalf.LOWER), 2);
                level.setBlock(pos, AloofBlocks.LOOFAH_GOURD.get().defaultBlockState().setValue(HALF, DoubleBlockHalf.UPPER), 2);
            }
        }
    }
}
