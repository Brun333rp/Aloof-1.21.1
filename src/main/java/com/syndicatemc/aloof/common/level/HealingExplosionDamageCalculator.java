package com.syndicatemc.aloof.common.level;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.block.state.BlockState;

public class HealingExplosionDamageCalculator extends ExplosionDamageCalculator {
    @Override
    public boolean shouldBlockExplode(Explosion explosion, BlockGetter reader, BlockPos pos, BlockState state, float power) {
        return false;
    }

    @Override
    public boolean shouldDamageEntity(Explosion explosion, Entity entity) {
        return false;
    }

    @Override
    public float getKnockbackMultiplier(Entity entity) {
        return 0.25F;
    }
}
