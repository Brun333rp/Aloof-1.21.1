package com.syndicatemc.aloof.common.item;

import com.syndicatemc.aloof.common.entity.MinecartHealingBomb;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MinecartItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

public class HealingBombMinecartItem extends MinecartItem {
    private static final DispenseItemBehavior DISPENSE_ITEM_BEHAVIOR = new DefaultDispenseItemBehavior() {
        private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();

        public ItemStack execute(BlockSource source, ItemStack stack) {
            Direction direction = source.state().getValue(DispenserBlock.FACING);
            ServerLevel serverLevel = source.level();
            Vec3 center = source.center();
            double d0 = center.x() + direction.getStepX() * 1.125F;
            double d1 = Math.floor(center.y()) + direction.getStepY();
            double d2 = center.z() + direction.getStepZ() * 1.125F;
            BlockPos pos = source.pos().relative(direction);
            BlockState state = serverLevel.getBlockState(pos);
            RailShape shape = state.getBlock() instanceof BaseRailBlock ? ((BaseRailBlock)state.getBlock()).getRailDirection(state, serverLevel, pos, null) : RailShape.NORTH_SOUTH;
            double d3;
            if (state.is(BlockTags.RAILS)) {
                if (shape.isAscending()) {
                    d3 = 0.6;
                } else {
                    d3 = 0.1;
                }
            } else {
                if (!state.isAir() || !serverLevel.getBlockState(pos.below()).is(BlockTags.RAILS)) {
                    return this.defaultDispenseItemBehavior.dispense(source, stack);
                }

                BlockState state1 = serverLevel.getBlockState(pos.below());
                RailShape shape1 = state1.getBlock() instanceof BaseRailBlock ? ((BaseRailBlock)state1.getBlock()).getRailDirection(state1, serverLevel, pos.below(), null) : RailShape.NORTH_SOUTH;
                if (direction != Direction.DOWN && shape1.isAscending()) {
                    d3 = -0.4;
                } else {
                    d3 = -0.9;
                }
            }

            AbstractMinecart minecart = HealingBombMinecartItem.createMinecart(serverLevel, d0, d1 + d3, d2, stack, null);
            serverLevel.addFreshEntity(minecart);
            stack.shrink(1);
            return stack;
        }

        protected void playSound(BlockSource source) {
            source.level().levelEvent(1000, source.pos(), 0);
        }
    };

    public HealingBombMinecartItem(Item.Properties properties) {
        super(AbstractMinecart.Type.TNT, properties);
        DispenserBlock.registerBehavior(this, DISPENSE_ITEM_BEHAVIOR);
    }

    public static AbstractMinecart createMinecart(ServerLevel level, double x, double y, double z, ItemStack stack, @javax.annotation.Nullable Player player) {
        AbstractMinecart minecart = new MinecartHealingBomb(level, x, y, z);
        EntityType.createDefaultStackConfig(level, stack, player).accept(minecart);
        return minecart;
    }

    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        if (!state.is(BlockTags.RAILS)) {
            return InteractionResult.FAIL;
        } else {
            ItemStack stack = context.getItemInHand();
            if (level instanceof ServerLevel serverLevel) {
                RailShape shape = state.getBlock() instanceof BaseRailBlock railBlock ? railBlock.getRailDirection(state, level, pos, null) : RailShape.NORTH_SOUTH;
                double d0 = 0.0F;
                if (shape.isAscending()) {
                    d0 = 0.5F;
                }

                AbstractMinecart minecart = HealingBombMinecartItem.createMinecart(serverLevel, pos.getX() + 0.5F, pos.getY() + 0.0625F + d0, pos.getZ() + 0.5F, stack, context.getPlayer());
                serverLevel.addFreshEntity(minecart);
                serverLevel.gameEvent(GameEvent.ENTITY_PLACE, pos, GameEvent.Context.of(context.getPlayer(), serverLevel.getBlockState(pos.below())));
            }

            stack.shrink(1);
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
    }
}
