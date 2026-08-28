package com.syndicatemc.aloof.client.particle;

import com.syndicatemc.aloof.core.other.AloofUtils;
import com.syndicatemc.aloof.core.registry.AloofParticleTypes;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

public class HealingExplosionParticleEmitter extends NoRenderParticle {
    private HealingExplosionParticleEmitter(ClientLevel world, double x, double y, double z, double radius) {
        super(world, x, y, z, 0.0F, 0.0F, 0.0F);
    }

    public void tick() {
        RandomSource random = this.random;
        for (int i = 0; i < 6; ++i) {
            double d0 = this.x + AloofUtils.getVariance(random, 4.0F);
            double d1 = this.y + AloofUtils.getVariance(random, 4.0F);
            double d2 = this.z + AloofUtils.getVariance(random, 4.0F);
            double d3 = this.x + AloofUtils.getVariance(random, 4.0F);
            double d4 = this.y + AloofUtils.getVariance(random, 4.0F);
            double d5 = this.z + AloofUtils.getVariance(random, 4.0F);
            this.level.addParticle(random.nextBoolean() ? AloofParticleTypes.HEALING_EXPLOSION.get() : ParticleTypes.EXPLOSION, d0, d1, d2, this.age / 4.0F, 0.0F, 0.0F);
            if (random.nextBoolean()) this.level.addParticle(AloofParticleTypes.SILPHIUM_HEART.get(), d3, d4, d5, 0.0F, 0.0F, 0.0F);
        }
        ++this.age;
        if (this.age >= 8) {
            this.remove();
        }
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double radius, double ySpeed, double zSpeed) {
            return new HealingExplosionParticleEmitter(worldIn, x, y, z, radius);
        }
    }
}
