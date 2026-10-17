package com.syndicatemc.aloof.core.registry;

import com.syndicatemc.aloof.client.particle.HealingExplosionParticleEmitter;
import com.syndicatemc.aloof.client.particle.SilphiumPollenParticle;
import com.syndicatemc.aloof.core.Aloof;
import net.minecraft.client.particle.HeartParticle;
import net.minecraft.client.particle.HugeExplosionParticle;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber(modid = Aloof.MOD_ID, value = Dist.CLIENT)
public class AloofParticleTypes {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(Registries.PARTICLE_TYPE, Aloof.MOD_ID);

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SILPHIUM_HEART = registerSimpleParticleType(true, "silphium_heart");
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> HEALING_EXPLOSION = registerSimpleParticleType(true, "healing_explosion");
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> HEALING_EXPLOSION_EMITTER = registerSimpleParticleType(true, "healing_explosion_emitter");
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SILPHIUM_POLLEN = registerSimpleParticleType(true, "silphium_pollen");

    private static DeferredHolder<ParticleType<?>, SimpleParticleType> registerSimpleParticleType(boolean alwaysShow, String name) {
        return PARTICLE_TYPES.register(name, () -> new SimpleParticleType(alwaysShow));
    }

    @SubscribeEvent
    public static void registerParticleTypes(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(SILPHIUM_HEART.get(), HeartParticle.Provider::new);
        event.registerSpriteSet(HEALING_EXPLOSION.get(), HugeExplosionParticle.Provider::new);
        event.registerSpecial(HEALING_EXPLOSION_EMITTER.get(), new HealingExplosionParticleEmitter.Provider());
        event.registerSpriteSet(SILPHIUM_POLLEN.get(), SilphiumPollenParticle.Provider::new);
    }
}
