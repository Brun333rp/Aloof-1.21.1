package com.syndicatemc.aloof.core.other;

import com.syndicatemc.aloof.common.entity.living.HarvestChild;
import com.syndicatemc.aloof.core.Aloof;
import com.syndicatemc.aloof.core.registry.AloofBlocks;
import com.syndicatemc.aloof.core.registry.AloofEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockDropsEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

@EventBusSubscriber(modid = Aloof.MOD_ID)
public class AloofEvents {
    @SubscribeEvent
    public static void blockInteractionEvent(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);
        Player player = event.getEntity();
        ItemStack useItem = event.getItemStack();
        InteractionHand hand = event.getHand();

        if (state.is(Blocks.TORCHFLOWER)) {
            if (useItem.canPerformAction(ItemAbilities.FIRESTARTER_LIGHT)) {
                event.setCanceled(true);
                RandomSource random = level.getRandom();
                level.setBlock(pos, AloofBlocks.LIT_TORCHFLOWER.get().defaultBlockState(), Block.UPDATE_ALL_IMMEDIATE);
                if (useItem.getItem() instanceof FlintAndSteelItem) {
                    useItem.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
                    level.playSound(player, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
                } else {
                    if (!player.isCreative()) useItem.shrink(1);
                    level.playSound(null, pos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 1.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
                }
                player.swing(hand);
            }
        }
    }

    @SubscribeEvent
    public static void blockBreakEvent(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        LevelAccessor accessor = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = accessor.getBlockState(pos);
        if (
            accessor instanceof Level level &&
            state.getBlock() instanceof PitcherCropBlock block &&
            state.getValue(PitcherCropBlock.AGE).equals(4) &&
            accessor.getRandom().nextFloat() < 0.01F &&
            !player.isCreative()
        ) {
            boolean isLower = state.getValue(DoublePlantBlock.HALF).equals(DoubleBlockHalf.LOWER);
            int mod = isLower ? 0 : -1;
            event.setCanceled(true);
            level.destroyBlock(pos.offset(0, mod, 0), false);
            HarvestChild child = AloofEntityTypes.HARVEST_CHILD.get().create(level);
            child.setPos(pos.getX() + 0.5F, pos.getY() + 0.05F + mod, pos.getZ() + 0.5F);
            accessor.addFreshEntity(child);
            child.addDeltaMovement(new Vec3(
                    AloofUtils.getVariance(level.random, 0.2F),
                    0.5F,
                    AloofUtils.getVariance(level.random, 0.2F)
            ));
            child.lookAt(player, 100000F, 100000F);
            ItemEntity itemEntity = new ItemEntity(level, pos.getX() + 0.5F, pos.getY() + 0.5F + mod, pos.getZ() + 0.5F, new ItemStack(Items.PITCHER_PLANT));
            accessor.addFreshEntity(itemEntity);
        }
    }
}
