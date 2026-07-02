package com.syndicatemc.aloof.core.registry;

import com.syndicatemc.aloof.core.Aloof;
import com.teamabnormals.blueprint.core.util.registry.SoundSubRegistryHelper;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;

public class AloofSoundEvents {
    public static final SoundSubRegistryHelper SOUND_EVENTS = Aloof.REGISTRY_HELPER.getSoundSubHelper();

    public static final DeferredHolder<SoundEvent, SoundEvent> HEALING_SALVE_USE = SOUND_EVENTS.createSoundEvent("item.healing_salve.use");
    public static final DeferredHolder<SoundEvent, SoundEvent> HEALING_SALVE_CONSUME = SOUND_EVENTS.createSoundEvent("item.healing_salve.consume");
    public static final DeferredHolder<SoundEvent, SoundEvent> HEALING_EXPLOSION = SOUND_EVENTS.createSoundEvent("block.healing_bomb.explode");
    public static final DeferredHolder<SoundEvent, SoundEvent> HEALING_FUSE = SOUND_EVENTS.createSoundEvent("block.healing_bomb.fuse");
}
