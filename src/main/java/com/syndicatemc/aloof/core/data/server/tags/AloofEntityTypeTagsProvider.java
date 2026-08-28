package com.syndicatemc.aloof.core.data.server.tags;

import com.syndicatemc.aloof.core.Aloof;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

import static com.syndicatemc.aloof.core.other.tags.AloofEntityTypeTags.HEALING_BLACKLIST;

public class AloofEntityTypeTagsProvider extends EntityTypeTagsProvider {
    public AloofEntityTypeTagsProvider(PackOutput output, CompletableFuture<Provider> provider, ExistingFileHelper helper) {
        super(output, provider, Aloof.MOD_ID, helper);
    }

    @Override
    public void addTags(Provider provider) {
        this.tag(HEALING_BLACKLIST)
                .add(
                        EntityType.ENDERMAN, EntityType.ENDERMITE, EntityType.ENDER_DRAGON, EntityType.SHULKER,
                        EntityType.SILVERFISH, EntityType.SPIDER, EntityType.CAVE_SPIDER,
                        EntityType.CREEPER,
                        EntityType.GUARDIAN, EntityType.ELDER_GUARDIAN,
                        EntityType.BLAZE, EntityType.HOGLIN, EntityType.PIGLIN_BRUTE, EntityType.GHAST,
                        EntityType.BREEZE,
                        EntityType.POLAR_BEAR,
                        EntityType.IRON_GOLEM, EntityType.SNOW_GOLEM
                )
                .addTags(
                        EntityTypeTags.ILLAGER, EntityTypeTags.UNDEAD, EntityTypeTags.RAIDERS
                );
    }
}
