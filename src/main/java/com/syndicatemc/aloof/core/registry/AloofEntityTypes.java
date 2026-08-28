package com.syndicatemc.aloof.core.registry;

import com.syndicatemc.aloof.common.entity.HealingBomb;
import com.syndicatemc.aloof.common.entity.MinecartHealingBomb;
import com.syndicatemc.aloof.core.Aloof;
import com.teamabnormals.blueprint.core.util.registry.EntitySubRegistryHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;

public class AloofEntityTypes {
    public static final EntitySubRegistryHelper ENTITY_TYPES = Aloof.REGISTRY_HELPER.getEntitySubHelper();

    public static final DeferredHolder<EntityType<?>, EntityType<HealingBomb>> HEALING_BOMB = ENTITY_TYPES.createEntity("healing_bomb", HealingBomb::new, MobCategory.MISC, builder ->
            builder.fireImmune().sized(0.98F, 0.98F).eyeHeight(0.15F).clientTrackingRange(10).updateInterval(10));
    public static final DeferredHolder<EntityType<?>, EntityType<MinecartHealingBomb>> HEALING_BOMB_MINECART = ENTITY_TYPES.createEntity("healing_bomb_minecart", MinecartHealingBomb::new, MobCategory.MISC, builder ->
            builder.sized(0.98F, 0.7F).passengerAttachments(0.1875F).clientTrackingRange(8));
}