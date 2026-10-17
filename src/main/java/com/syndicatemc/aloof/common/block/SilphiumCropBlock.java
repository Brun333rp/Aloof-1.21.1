package com.syndicatemc.aloof.common.block;

import com.mojang.serialization.MapCodec;
import com.syndicatemc.aloof.core.data.server.tags.AloofBlockTagsProvider;
import com.syndicatemc.aloof.core.other.AloofUtils;
import com.syndicatemc.aloof.core.other.tags.AloofBlockTags;
import com.syndicatemc.aloof.core.registry.AloofItems;
import com.syndicatemc.aloof.core.registry.AloofParticleTypes;
import com.teamabnormals.blueprint.common.network.particle.SpawnParticlesPayload;
import com.teamabnormals.blueprint.core.util.NetworkUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.ItemAbilities;

import java.util.ArrayList;
import java.util.List;

public class SilphiumCropBlock extends BushBlock implements BonemealableBlock {
    public static final MapCodec<SilphiumCropBlock> CODEC = simpleCodec(SilphiumCropBlock::new);
    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 9);
    public static final IntegerProperty SECTION = IntegerProperty.create("section", 1, 4);

    private static final VoxelShape[] SHAPE_SECTION_1 = new VoxelShape[]{
            Block.box(5.0D, -1.0D, 5.0D, 11.0D, 4.0D, 11.0D),
            Block.box(5.0D, -1.0D, 5.0D, 11.0D, 10.0D, 11.0D),
            Block.box(4.0D, -1.0D, 4.0D, 12.0D, 16.0D, 12.0D)
    };
    private static final VoxelShape[] SHAPE_SECTION_2 = new VoxelShape[]{
            Block.box(4.0D, 0.0D, 4.0D, 12.0D, 6.0D, 12.0D),
            Block.box(4.0D, 0.0D, 4.0D, 12.0D, 12.0D, 12.0D),
            Block.box(4.0D, 0.0D, 4.0D, 12.0D, 16.0D, 12.0D)
    };
    private static final VoxelShape[] SHAPE_SECTION_3 = new VoxelShape[]{
            Block.box(4.0D, 0.0D, 4.0D, 12.0D, 6.0D, 12.0D),
            Block.box(4.0D, 0.0D, 4.0D, 12.0D, 14.0D, 12.0D),
            Block.box(4.0D, 0.0D, 4.0D, 12.0D, 16.0D, 12.0D)
    };
    private static final VoxelShape[] SHAPE_SECTION_4 = new VoxelShape[]{
            Block.box(4.0D, 0.0D, 4.0D, 12.0D, 7.0D, 12.0D),
            Block.box(4.0D, 0.0D, 4.0D, 12.0D, 9.0D, 12.0D)
    };

    public MapCodec<SilphiumCropBlock> codec() {
        return CODEC;
    }

    public SilphiumCropBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0).setValue(SECTION, 1));
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        return new ItemStack(AloofItems.SILPHIUM_SEEDS.get());
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return state.getValue(SECTION) == 1;
    }

    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int age = state.getValue(AGE);
        if (level.getRawBrightness(pos.above(), 0) >= 3 && CommonHooks.canCropGrow(level, pos, state, random.nextInt(age >= 6 ? 6 : 4) == 0)) {
            this.grow(level, pos, state);
            CommonHooks.fireCropGrowPost(level, pos, state);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(state));
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE, SECTION);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        int section = state.getValue(SECTION);
        int age = state.getValue(AGE);
        Vec3 offset = state.getOffset(level, pos);
        VoxelShape shape = SHAPE_SECTION_1[0];

        if (section == 1) shape = SHAPE_SECTION_1[Math.min(age, 2)];
        else if (section == 2) shape = SHAPE_SECTION_2[Math.min(age - 3, 2)];
        else if (section == 3) shape = SHAPE_SECTION_3[Math.min(age - 5, 2)];
        else if (section == 4) shape = SHAPE_SECTION_4[Math.min(age - 8, 1)];

        return shape.move(offset.x, offset.y, offset.z);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState belowState = level.getBlockState(pos.below());
        BlockState aboveState = level.getBlockState(pos.above());
        int age = state.getValue(AGE);
        int section = state.getValue(SECTION);
        boolean canSurvive = false;

        if (section == 1) {
            if (belowState.is(AloofBlockTags.SILPHIUM_PLANTABLE_ON)) canSurvive = true;
            if (age > 2 && !aboveState.is(this)) canSurvive = false;
        }
        if (section == 2) {
            if (belowState.is(this)) canSurvive = true;
            if (age > 4 && !aboveState.is(this)) canSurvive = false;
        }
        if (section == 3) {
            if (belowState.is(this)) canSurvive = true;
            if (age > 7 && !aboveState.is(this)) canSurvive = false;
        }
        if (section == 4) {
            if (belowState.is(this)) canSurvive = true;
        }

        return canSurvive;
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        int age = state.getValue(AGE);
        int section = state.getValue(SECTION);
        boolean canBonemeal = false;
        BlockState aboveState = level.getBlockState(pos.above());
        BlockState aboveState1 = level.getBlockState(pos.above(2));
        BlockState aboveState2 = level.getBlockState(pos.above(3));

        if (section == 1 && age != 9) {
            canBonemeal = true;
            if (age >= 2) canBonemeal = canProgress(aboveState);
            if (age >= 4) canBonemeal = canProgress(aboveState1);
            if (age >= 7) canBonemeal = canProgress(aboveState2);
        }
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
        int section = state.getValue(SECTION);
        int newAge = Math.min(9, age + 1);
        BlockState aboveState = level.getBlockState(pos.above());
        BlockState aboveState1 = level.getBlockState(pos.above(2));
        BlockState aboveState2 = level.getBlockState(pos.above(3));

        if (age != 9) {
            if (section == 1) {
                if (age <= 1) level.setBlock(pos, state.setValue(AGE, newAge).setValue(SECTION, 1), 2);
                else if (canProgress(aboveState)) {
                    level.setBlock(pos, state.setValue(AGE, newAge).setValue(SECTION, 1), 2);
                    if (age <= 3) level.setBlock(pos.above(), state.setValue(AGE, newAge).setValue(SECTION, 2), 2);
                    else if (canProgress(aboveState1)) {
                        level.setBlock(pos.above(), state.setValue(AGE, newAge).setValue(SECTION, 2), 2);
                        if (age <= 6) level.setBlock(pos.above(2), state.setValue(AGE, newAge).setValue(SECTION, 3), 2);
                        else if (canProgress(aboveState2)) {
                            level.setBlock(pos.above(2), state.setValue(AGE, newAge).setValue(SECTION, 3), 2);
                            level.setBlock(pos.above(3), state.setValue(AGE, newAge).setValue(SECTION, 4), 2);
                        }
                    }
                }
            }
        }
    }

    private boolean canProgress(BlockState state) {
        return state.canBeReplaced() || state.getBlock() == this;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        int section = state.getValue(SECTION);
        int age = state.getValue(AGE);
        BlockState belowState = level.getBlockState(pos.below());
        BlockState belowState1 = level.getBlockState(pos.below(2));
        BlockState belowState2 = level.getBlockState(pos.below(3));
        Vec3 offset = state.getOffset(level, pos);
        RandomSource random = level.random;

        if (stack.canPerformAction(ItemAbilities.SHEARS_HARVEST) && age == 9 && section == 4) {
            level.playSound(null, pos, SoundEvents.SHEEP_SHEAR, SoundSource.BLOCKS, 1.0F, 1.1F + AloofUtils.getVariance(level.random, 0.1F));
            level.setBlock(pos.below(3), belowState2.setValue(AGE, 6), 2);
            level.setBlock(pos.below(2), belowState1.setValue(AGE, 6), 2);
            level.setBlock(pos.below(), belowState.setValue(AGE, 6), 2);
            level.destroyBlock(pos, true);
            stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
            level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
            if (level instanceof ServerLevel serverLevel) {
                List<SpawnParticlesPayload.ParticleInstance> pollenParticles = new ArrayList<>();
                for (int l = 0; l < 10; ++l) {
                    double x = pos.getX() + 0.5D + AloofUtils.getVariance(random, 0.2F) + offset.x;
                    double y = pos.getY() + 0.25D + AloofUtils.getVariance(random, 0.2F);
                    double z = pos.getZ() + 0.5D + AloofUtils.getVariance(random, 0.2F) + offset.z;
                    double dx = AloofUtils.getVariance(random, 0.1F);
                    double dy = AloofUtils.getVariance(random, 0.1F);
                    double dz = AloofUtils.getVariance(random, 0.1F);
                    pollenParticles.add(new SpawnParticlesPayload.ParticleInstance(x, y, z, dx, dy, dz));
                }
                NetworkUtil.spawnParticle(serverLevel, AloofParticleTypes.SILPHIUM_POLLEN.get(), pollenParticles);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        } else {
            return super.useItemOn(stack, state, level, pos, player, hand, result);
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        Vec3 offset = state.getOffset(level, pos);
        if (state.getValue(AGE).equals(9) && state.getValue(SECTION).equals(4)) {
            double x = pos.getX() + 0.5D + AloofUtils.getVariance(random, 0.2F) + offset.x;
            double y = pos.getY() + 0.25D + AloofUtils.getVariance(random, 0.2F);
            double z = pos.getZ() + 0.5D + AloofUtils.getVariance(random, 0.2F) + offset.z;
            level.addParticle(AloofParticleTypes.SILPHIUM_POLLEN.get(), x, y, z, 0.0D, 0.0D, 0.0D);
        }
    }
}
