package com.syndicatemc.aloof.common.block;

import com.teamabnormals.blueprint.common.block.BlueprintDirectionalBlock;
import com.teamabnormals.blueprint.common.network.particle.SpawnParticlesPayload;
import com.teamabnormals.blueprint.core.util.NetworkUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

public class PitcherRootBlock extends BlueprintDirectionalBlock {
    public static final IntegerProperty EATEN = IntegerProperty.create("eaten", 0, 3);
    private static final VoxelShape[] SHAPE_UP = new VoxelShape[]{
            Block.box(3.0D, 0.0D, 3.0D, 13.0D, 12.0D, 13.0D),
            Block.box(3.0D, 0.0D, 3.0D, 13.0D, 9.0D, 13.0D),
            Block.box(3.0D, 0.0D, 3.0D, 13.0D, 6.0D, 13.0D),
            Block.box(3.0D, 0.0D, 3.0D, 13.0D, 3.0D, 13.0D)
    };
    private static final VoxelShape[] SHAPE_NORTH = new VoxelShape[]{
            Block.box(3.0D, 0.0D, 2.0D, 13.0D, 10.0D, 14.0D),
            Block.box(3.0D, 0.0D, 5.0D, 13.0D, 10.0D, 14.0D),
            Block.box(3.0D, 0.0D, 8.0D, 13.0D, 10.0D, 14.0D),
            Block.box(3.0D, 0.0D, 11.0D, 13.0D, 10.0D, 14.0D)
    };
    private static final VoxelShape[] SHAPE_SOUTH = new VoxelShape[]{
            Block.box(3.0D, 0.0D, 2.0D, 13.0D, 10.0D, 14.0D),
            Block.box(3.0D, 0.0D, 2.0D, 13.0D, 10.0D, 11.0D),
            Block.box(3.0D, 0.0D, 2.0D, 13.0D, 10.0D, 8.0D),
            Block.box(3.0D, 0.0D, 2.0D, 13.0D, 10.0D, 5.0D)
    };
    private static final VoxelShape[] SHAPE_EAST = new VoxelShape[]{
            Block.box(2.0D, 0.0D, 3.0D, 14.0D, 10.0D, 13.0D),
            Block.box(2.0D, 0.0D, 3.0D, 11.0D, 10.0D, 13.0D),
            Block.box(2.0D, 0.0D, 3.0D, 8.0D, 10.0D, 13.0D),
            Block.box(2.0D, 0.0D, 3.0D, 5.0D, 10.0D, 13.0D)
    };
    private static final VoxelShape[] SHAPE_WEST = new VoxelShape[]{
            Block.box(2.0D, 0.0D, 3.0D, 14.0D, 10.0D, 13.0D),
            Block.box(5.0D, 0.0D, 3.0D, 14.0D, 10.0D, 13.0D),
            Block.box(8.0D, 0.0D, 3.0D, 14.0D, 10.0D, 13.0D),
            Block.box(11.0D, 0.0D, 3.0D, 14.0D, 10.0D, 13.0D)
    };

    public PitcherRootBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.UP).setValue(EATEN, 0));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction facing = state.getValue(FACING);
        int eaten = state.getValue(EATEN);
        VoxelShape shape;

        shape = switch (facing) {
            case NORTH -> SHAPE_NORTH[eaten];
            case SOUTH -> SHAPE_SOUTH[eaten];
            case EAST -> SHAPE_EAST[eaten];
            case WEST -> SHAPE_WEST[eaten];
            default -> SHAPE_UP[eaten];
        };

        return shape;
    }

    @Override
    protected float getShadeBrightness(BlockState state, BlockGetter getter, BlockPos pos) {
        return 1.0F;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
        return true;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, EATEN);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return Block.canSupportCenter(level, pos.relative(Direction.DOWN), Direction.UP);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.getPlayer() == null || context.getPlayer().isSecondaryUseActive() ? context.getNearestLookingDirection().getOpposite() : context.getClickedFace();

        return this.defaultBlockState().setValue(FACING, direction == Direction.DOWN ? Direction.UP : direction);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide) {
            if (eat(level, pos, state, player).consumesAction()) {
                return InteractionResult.SUCCESS;
            }

            if (player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
                return InteractionResult.CONSUME;
            }
        }

        return eat(level, pos, state, player);
    }

    protected static InteractionResult eat(LevelAccessor level, BlockPos pos, BlockState state, Player player) {
        if (!player.canEat(false)) {
            return InteractionResult.PASS;
        } else {
            player.getFoodData().eat(4, 0.2F);
            level.playSound(null, pos, SoundEvents.GENERIC_EAT, SoundSource.PLAYERS, 0.6F, 0.8F + level.getRandom().nextFloat() * 0.5F);
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 400, 0));
            int i = state.getValue(EATEN);
            level.gameEvent(player, GameEvent.EAT, pos);
            if (i < 3) {
                level.setBlock(pos, state.setValue(EATEN, i + 1), 3);
            } else {
                level.removeBlock(pos, false);
                level.gameEvent(player, GameEvent.BLOCK_DESTROY, pos);
            }

            return InteractionResult.SUCCESS;
        }
    }
}
