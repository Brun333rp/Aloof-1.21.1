package com.syndicatemc.aloof.common.entity;

import com.syndicatemc.aloof.common.level.HealingExplosionDamageCalculator;
import com.syndicatemc.aloof.core.other.AloofUtils;
import com.syndicatemc.aloof.core.other.tags.AloofEntityTypeTags;
import com.syndicatemc.aloof.core.registry.AloofEntityTypes;
import com.syndicatemc.aloof.core.registry.AloofParticleTypes;
import com.syndicatemc.aloof.core.registry.AloofSoundEvents;
import com.teamabnormals.blueprint.common.network.particle.SpawnParticlesPayload;
import com.teamabnormals.blueprint.core.util.NetworkUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;
import java.util.List;

public class HealingBomb extends PrimedTnt {
    @Nullable
    private LivingEntity owner;

    public HealingBomb(EntityType<? extends PrimedTnt> entityType, Level level) {
        super(entityType, level);
    }

    public HealingBomb(Level level, double x, double y, double z, @Nullable LivingEntity owner) {
        this(AloofEntityTypes.HEALING_BOMB.get(), level);
        this.setPos(x, y, z);
        double d0 = level.random.nextDouble() * Math.PI * 2F;
        this.setDeltaMovement(-Math.sin(d0) * 0.02, 0.2F, -Math.cos(d0) * 0.02);
        this.setFuse(20);
        this.xo = x;
        this.yo = y;
        this.zo = z;
        this.owner = owner;
    }

    @Override
    protected void explode() {
        this.level().explode(
                this, null, new HealingExplosionDamageCalculator(),
                this.getX(), this.getY(0.1D), this.getZ(),
                4.0F,
                false, Level.ExplosionInteraction.NONE,
                AloofParticleTypes.HEALING_EXPLOSION_EMITTER.get(), AloofParticleTypes.HEALING_EXPLOSION_EMITTER.get(),
                AloofSoundEvents.HEALING_EXPLOSION
        );
        for (LivingEntity entity : this.level().getEntitiesOfClass(LivingEntity.class, new AABB(this.getOnPos(1.0F)).inflate(4.0D, 4.0D, 4.0))) {
            if (!entity.getType().is(AloofEntityTypeTags.HEALING_BLACKLIST) && this.level() instanceof ServerLevel serverLevel) {
                entity.heal(10.0F + entity.getMaxHealth() * 0.25F);
                NetworkUtil.spawnParticle(serverLevel, AloofParticleTypes.SILPHIUM_HEART.get(), List.of(
                        new SpawnParticlesPayload.ParticleInstance(
                                entity.getX() + AloofUtils.getVariance(random, 0.1F),
                                entity.getY() + entity.getBbHeight() + 0.05F + AloofUtils.getVariance(random, 0.05F),
                                entity.getZ() + AloofUtils.getVariance(random, 0.1F),
                                0.0D, 0.0D, 0.0D
                        ))
                );
            }
        }
    }

    @Override
    public void tick() {
        this.handlePortal();
        this.applyGravity();
        this.move(MoverType.SELF, this.getDeltaMovement());
        this.setDeltaMovement(this.getDeltaMovement().scale(0.98));
        if (this.onGround()) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.7, -0.5F, 0.7));
        }

        int i = this.getFuse() - 1;
        this.setFuse(i);
        if (i <= 0) {
            this.discard();
            if (!this.level().isClientSide) {
                this.explode();
            }
        } else {
            this.updateInWaterStateAndDoFluidPushing();
            if (this.level().isClientSide) {
                this.level().addParticle(ParticleTypes.SMOKE, this.getX(), this.getY() + 0.5F, this.getZ(), 0.0F, 0.0F, 0.0F);
                if (this.random.nextBoolean()) this.level().addParticle(
                        AloofParticleTypes.SILPHIUM_HEART.get(),
                        this.getX() + AloofUtils.getVariance(this.random, 1.0F),
                        this.getY() + 0.5F + AloofUtils.getVariance(this.random, 0.5F),
                        this.getZ() + AloofUtils.getVariance(this.random, 1.0F),
                        0.0F, 0.0F, 0.0F
                );
            }
        }

    }
}