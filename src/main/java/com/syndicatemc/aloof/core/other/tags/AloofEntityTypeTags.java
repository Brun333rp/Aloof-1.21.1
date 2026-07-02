package com.syndicatemc.aloof.core.other.tags;

import com.syndicatemc.aloof.core.Aloof;
import com.teamabnormals.blueprint.core.util.TagUtil;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class AloofEntityTypeTags {
    public static final TagKey<EntityType<?>> HEALING_BLACKLIST = modTag("healing_blacklist");

    private static TagKey<EntityType<?>> modTag(String key) {
        return TagUtil.entityTypeTag(Aloof.MOD_ID, key);
    }
}
