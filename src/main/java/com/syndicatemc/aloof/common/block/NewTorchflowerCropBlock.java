package com.syndicatemc.aloof.common.block;

import com.mojang.serialization.MapCodec;
import com.syndicatemc.aloof.core.other.tags.AloofBlockTags;
import com.syndicatemc.aloof.core.registry.AloofBlocks;
import com.syndicatemc.aloof.core.registry.AloofItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

import static com.syndicatemc.aloof.common.block.NewTorchflowerBlock.SOUL;

public class NewTorchflowerCropBlock extends CropBlock {
    public static final MapCodec<NewTorchflowerCropBlock> CODEC = simpleCodec(NewTorchflowerCropBlock::new);
    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
            Block.box(6.0F, -1.0F, 6.0F, 10.0F, 4.0F, 10.0F),
            Block.box(5.0F, -1.0F, 5.0F, 11.0F, 4.0F, 11.0F),
            Block.box(5.0F, -1.0F, 5.0F, 11.0F, 5.0F, 11.0F),
            Block.box(4.0F, -1.0F, 4.0F, 12.0F, 7.0F, 12.0F)
    };

    public MapCodec<NewTorchflowerCropBlock> codec() {
        return CODEC;
    }

    public NewTorchflowerCropBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0).setValue(SOUL, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE, SOUL);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Vec3 offset = state.getOffset(level, pos);

        return SHAPE_BY_AGE[state.getValue(AGE)].move(offset.x, offset.y, offset.z);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState belowState = context.getLevel().getBlockState(context.getClickedPos().below());
        return this.defaultBlockState().setValue(SOUL, belowState.is(BlockTags.SOUL_FIRE_BASE_BLOCKS));
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState belowState = level.getBlockState(pos.below());
        return belowState.is(AloofBlockTags.TORCHFLOWER_PLANTABLE_ON);
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return AloofItems.TORCHFLOWER_SEEDS;
    }

    @Override
    public BlockState getStateForAge(int age) {
        return age == 4 ? AloofBlocks.TORCHFLOWER.get().defaultBlockState() : super.getStateForAge(age);
    }

    @Override
    public void growCrops(Level level, BlockPos pos, BlockState state) {
        int age = state.getValue(AGE) + 1;

        level.setBlock(pos, age == 4 ? AloofBlocks.TORCHFLOWER.get().defaultBlockState() : state.setValue(AGE, age), 2);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (random.nextInt(3) != 0) {
            super.randomTick(state, level, pos, random);
        }
    }

    @Override
    protected IntegerProperty getAgeProperty() {
        return AGE;
    }

    @Override
    public int getMaxAge() {
        return 4;
    }

    @Override
    protected int getBonemealAgeIncrease(Level level) {
        return 1;
    }
}
