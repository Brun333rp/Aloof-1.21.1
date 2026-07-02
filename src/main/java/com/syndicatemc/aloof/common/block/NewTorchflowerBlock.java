package com.syndicatemc.aloof.common.block;

import com.syndicatemc.aloof.core.other.AloofUtils;
import com.syndicatemc.aloof.core.other.tags.AloofBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.ItemAbilities;

import javax.annotation.Nullable;

public class NewTorchflowerBlock extends FlowerBlock {
    protected static final VoxelShape SHAPE = Block.box(4.0F, 0.0F, 4.0F, 12.0F, 14.0F, 12.0F);
    protected static final VoxelShape SHAPE_FARMLAND = Block.box(4.0F, -1.0F, 4.0F, 12.0F, 13.0F, 12.0F);
    public static final BooleanProperty ON_FARMLAND = BooleanProperty.create("on_farmland");
    public static final BooleanProperty SOUL = BooleanProperty.create("soul");
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public NewTorchflowerBlock(BlockBehaviour.Properties properties) {
        super(MobEffects.NIGHT_VISION, 5.0F, properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(ON_FARMLAND, false).setValue(SOUL, false).setValue(LIT, false));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Vec3 offset = state.getOffset(level, pos);
        VoxelShape shape = state.getValue(ON_FARMLAND) ? SHAPE_FARMLAND : SHAPE;

        return shape.move(offset.x, offset.y, offset.z);
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        BlockState belowState = level.getBlockState(pos.below());

        level.setBlock(pos, state.setValue(ON_FARMLAND, belowState.is(AloofBlockTags.FARMLAND)).setValue(SOUL, belowState.is(BlockTags.SOUL_FIRE_BASE_BLOCKS)), Block.UPDATE_ALL_IMMEDIATE);
        super.onPlace(state, level, pos, oldState, isMoving);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState belowState = context.getLevel().getBlockState(context.getClickedPos().below());

        return this.defaultBlockState().setValue(ON_FARMLAND, belowState.is(AloofBlockTags.FARMLAND)).setValue(SOUL, belowState.is(BlockTags.SOUL_FIRE_BASE_BLOCKS));
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean isMoving) {
        BlockState belowState = level.getBlockState(pos.below());

        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, isMoving);
        level.setBlock(pos, state.setValue(ON_FARMLAND, belowState.is(AloofBlockTags.FARMLAND)).setValue(SOUL, belowState.is(BlockTags.SOUL_FIRE_BASE_BLOCKS)), Block.UPDATE_ALL_IMMEDIATE);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState belowState = level.getBlockState(pos.below());

        return belowState.is(AloofBlockTags.TORCHFLOWER_PLACEABLE_ON);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ON_FARMLAND, SOUL, LIT);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        boolean onFarmland = state.getValue(ON_FARMLAND);
        boolean isSoul = state.getValue(SOUL);
        boolean isLit = state.getValue(LIT);
        Vec3 offset = state.getOffset(level, pos);

        if (random.nextFloat() > 0.3F && isLit) {
            double d0 = pos.getX() + 0.5F + AloofUtils.getVariance(random, 0.15F) + offset.x;
            double d1 = pos.getY() + 0.75 + AloofUtils.getVariance(random, 0.2F) + (onFarmland ? -0.0625F : 0F);
            double d2 = pos.getZ() + 0.5F + AloofUtils.getVariance(random, 0.15F) + offset.z;
            level.addParticle(isSoul ? ParticleTypes.SOUL_FIRE_FLAME : ParticleTypes.FLAME, d0, d1, d2, 0.0F, 0.0F, 0.0F);
        }
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (stack.canPerformAction(ItemAbilities.FIRESTARTER_LIGHT) && !state.getValue(LIT)) {
            RandomSource random = level.getRandom();
            level.setBlock(pos, state.setValue(LIT, true), 11);
            if (stack.getItem() instanceof FlintAndSteelItem) {
                stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
                level.playSound(player, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
            } else {
                if (!player.isCreative()) stack.shrink(1);
                level.playSound(null, pos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 1.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
            }
            level.scheduleTick(pos, state.getBlock(), 5);
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        } else {
            return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
        }
    }
}
