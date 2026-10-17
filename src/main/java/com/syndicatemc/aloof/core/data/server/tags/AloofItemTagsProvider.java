package com.syndicatemc.aloof.core.data.server.tags;

import com.syndicatemc.aloof.core.Aloof;
import com.syndicatemc.aloof.core.other.tags.AloofItemTags;
import com.syndicatemc.aloof.core.registry.AloofBlocks;
import com.teamabnormals.blueprint.core.data.server.tags.BlueprintItemTagsProvider;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

import static com.syndicatemc.aloof.core.registry.AloofItems.*;

public class AloofItemTagsProvider extends BlueprintItemTagsProvider {
    public AloofItemTagsProvider(PackOutput output, CompletableFuture<Provider> provider, CompletableFuture<TagsProvider.TagLookup<Block>> lookup, ExistingFileHelper helper) {
        super(Aloof.MOD_ID, output, provider, lookup, helper);
    }

    @Override
    public void addTags(Provider provider) {
        this.tag(AloofItemTags.SEEDS_LOOFAH).add(LOOFAH_SEEDS.get());
        this.tag(AloofItemTags.SEEDS_SILPHIUM).add(SILPHIUM_SEEDS.get());

        this.tag(Tags.Items.SEEDS).add(LOOFAH_SEEDS.get(), SILPHIUM_SEEDS.get());
        this.tag(Tags.Items.ANIMAL_FOODS).add(LOOFAH_SEEDS.get(), SILPHIUM_SEEDS.get());
        this.tag(ItemTags.CHICKEN_FOOD).add(LOOFAH_SEEDS.get(), SILPHIUM_SEEDS.get());
        this.tag(ItemTags.PARROT_FOOD).add(LOOFAH_SEEDS.get(), SILPHIUM_SEEDS.get());
        this.tag(ItemTags.SNIFFER_FOOD).add(SILPHIUM_SEEDS.get());

        this.tag(Tags.Items.CROPS).add(AloofBlocks.LOOFAH_GOURD.asItem(), SILPHIUM_FLOWER.get());

        this.tag(AloofItemTags.STORAGE_BLOCKS_SILPHIUM).add(AloofBlocks.SILPHIUM_BUNDLE.asItem());
    }
}
