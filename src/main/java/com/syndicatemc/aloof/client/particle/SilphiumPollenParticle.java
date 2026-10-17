package com.syndicatemc.aloof.client.particle;

import com.syndicatemc.aloof.core.other.AloofUtils;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class SilphiumPollenParticle extends SimpleAnimatedParticle {
    private float rotSpeed;

    public SilphiumPollenParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprites) {
        super(level, x, y, z, sprites, 0.05F);
        this.xd = xSpeed;
        this.yd = ySpeed;
        this.zd = zSpeed;
        this.quadSize *= 1F;
        this.lifetime = 60 + this.random.nextInt(60);
        this.rotSpeed = (float)Math.toRadians(this.random.nextBoolean() ? -30.0F : 30.0F);
        this.setFadeColor(16772528);
        this.setSpriteFromAge(sprites);
    }

    public void move(double x, double y, double z) {
        this.setBoundingBox(this.getBoundingBox().move(x, y, z));
        this.setLocationFromBoundingbox();
    }

    public int getLightColor(float partialTick) {
        BlockPos blockpos = BlockPos.containing(this.x, this.y, this.z);
        return this.level.hasChunkAt(blockpos) ? LevelRenderer.getLightColor(this.level, blockpos) : 0;
    }

    public void tick() {
        super.tick();
        this.setSpriteFromAge(this.sprites);
        if (this.age > this.lifetime / 2) {
            this.setAlpha(1.0F - (this.age - (float)(this.lifetime / 2)) / this.lifetime);
            this.rCol += (this.fadeR - this.rCol) * 0.2F;
            this.gCol += (this.fadeG - this.gCol) * 0.2F;
            this.bCol += (this.fadeB - this.bCol) * 0.2F;
        }
        if (!this.removed) {
            this.oRoll = this.roll;
            this.roll += this.rotSpeed / 20.0F;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            RandomSource random = level.random;
            double dx = AloofUtils.getVariance(random, 0.03125F);
            double dy = AloofUtils.getVariance(random, 0.0125F);
            double dz = AloofUtils.getVariance(random, 0.03125F);
            return new SilphiumPollenParticle(level, x, y, z, xSpeed + dx, ySpeed + dy, zSpeed + dz, this.sprites);
        }
    }
}
