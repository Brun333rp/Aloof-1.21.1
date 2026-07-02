package com.syndicatemc.aloof.common.block;

import com.syndicatemc.aloof.common.entity.HealingBomb;
import com.syndicatemc.aloof.core.registry.AloofSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import javax.annotation.Nullable;

public class HealingBombBlock extends TntBlock {
    public HealingBombBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(UNSTABLE, false));
    }

    public void onCaughtFire(BlockState state, Level level, BlockPos pos, @Nullable Direction face, @Nullable LivingEntity igniter) {
        if (!level.isClientSide) {
            createBomb(level, pos, igniter);
            level.playSound(null, pos.getX() + 0.5F, pos.getY(), pos.getZ() + 0.5F, AloofSoundEvents.HEALING_FUSE, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.gameEvent(igniter, GameEvent.PRIME_FUSE, pos);
        }
    }

    public void wasExploded(Level level, BlockPos pos, Explosion explosion) {
        if (!level.isClientSide) {
            createBomb(level, pos, explosion.getIndirectSourceEntity());
        }
    }

    private void createBomb(Level level, BlockPos pos, @Nullable LivingEntity igniter) {
        HealingBomb bomb = new HealingBomb(level, pos.getX() + 0.5F, pos.getY(), pos.getZ() + 0.5F, igniter);
        bomb.setFuse(20 + level.random.nextInt(20));
        level.addFreshEntity(bomb);
    }
}