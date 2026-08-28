package com.syndicatemc.aloof.common.entity;

import com.syndicatemc.aloof.common.level.HealingExplosionDamageCalculator;
import com.syndicatemc.aloof.core.other.AloofUtils;
import com.syndicatemc.aloof.core.other.tags.AloofEntityTypeTags;
import com.syndicatemc.aloof.core.registry.*;
import com.teamabnormals.blueprint.common.network.particle.SpawnParticlesPayload;
import com.teamabnormals.blueprint.core.util.NetworkUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;
import java.util.List;

public class MinecartHealingBomb extends AbstractMinecart {
    private int fuse = -1;

    public MinecartHealingBomb(EntityType<? extends MinecartHealingBomb> entityType, Level level) {
        super(entityType, level);
    }

    public MinecartHealingBomb(Level level, double x, double y, double z) {
        super(AloofEntityTypes.HEALING_BOMB_MINECART.get(), level, x, y, z);
    }

    public AbstractMinecart.Type getMinecartType() {
        return Type.TNT;
    }

    public BlockState getDefaultDisplayBlockState() {
        return AloofBlocks.HEALING_BOMB.get().defaultBlockState();
    }

    public void tick() {
        super.tick();
        if (this.fuse > 0) {
            --this.fuse;
            this.level().addParticle(ParticleTypes.SMOKE, this.getX(), this.getY() + 0.5F, this.getZ(), 0.0F, 0.0F, 0.0F);
            if (this.random.nextBoolean()) this.level().addParticle(
                    AloofParticleTypes.SILPHIUM_HEART.get(),
                    this.getX() + AloofUtils.getVariance(this.random, 1.0F),
                    this.getY() + 0.5F + AloofUtils.getVariance(this.random, 0.5F),
                    this.getZ() + AloofUtils.getVariance(this.random, 1.0F),
                    0.0F, 0.0F, 0.0F
            );
        } else if (this.fuse == 0) {
            this.explode(this.getDeltaMovement().horizontalDistanceSqr());
        }

        if (this.horizontalCollision) {
            double d0 = this.getDeltaMovement().horizontalDistanceSqr();
            if (d0 >= 0.01F) {
                this.explode(d0);
            }
        }

    }

    public boolean hurt(DamageSource source, float amount) {
        Entity entity = source.getDirectEntity();
        if (entity instanceof AbstractArrow arrow) {
            if (arrow.isOnFire()) {
                DamageSource damageSource = this.damageSources().explosion(this, source.getEntity());
                this.explode(damageSource, arrow.getDeltaMovement().lengthSqr());
            }
        }

        return super.hurt(source, amount);
    }

    public void destroy(DamageSource source) {
        double d0 = this.getDeltaMovement().horizontalDistanceSqr();
        if (!damageSourceIgnitesTnt(source) && !(d0 >= 0.01F)) {
            this.destroy(this.getDropItem());
        } else if (this.fuse < 0) {
            this.primeFuse();
            this.fuse = this.random.nextInt(20) + this.random.nextInt(20);
        }
    }

    protected Item getDropItem() {
        return AloofItems.HEALING_BOMB_MINECART.get();
    }

    protected void explode(double radiusModifier) {
        this.explode(null, radiusModifier);
    }

    protected void explode(@Nullable DamageSource damageSource, double radiusModifier) {
        if (!this.level().isClientSide) {
            double d0 = Math.sqrt(radiusModifier);
            if (d0 > 5.0F) {
                d0 = 5.0F;
            }

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
            this.discard();
        }
    }

    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        if (fallDistance >= 3.0F) {
            float f = fallDistance / 10.0F;
            this.explode((f * f));
        }

        return super.causeFallDamage(fallDistance, multiplier, source);
    }

    public void activateMinecart(int x, int y, int z, boolean receivingPower) {
        if (receivingPower && this.fuse < 0) {
            this.primeFuse();
        }
    }

    public void handleEntityEvent(byte id) {
        if (id == 10) {
            this.primeFuse();
        } else {
            super.handleEntityEvent(id);
        }
    }

    public void primeFuse() {
        this.fuse = 30;
        if (!this.level().isClientSide) {
            this.level().broadcastEntityEvent(this, (byte)10);
            if (!this.isSilent()) {
                this.level().playSound(null, this.getX(), this.getY(), this.getZ(), AloofSoundEvents.HEALING_FUSE, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
        }
    }

    public int getFuse() {
        return this.fuse;
    }

    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("HealingBombFuse", 99)) {
            this.fuse = compound.getInt("HealingBombFuse");
        }
    }

    protected void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("HealingBombFuse", this.fuse);
    }

    private static boolean damageSourceIgnitesTnt(DamageSource source) {
        return source.is(DamageTypeTags.IS_FIRE) || source.is(DamageTypeTags.IS_EXPLOSION);
    }
}
