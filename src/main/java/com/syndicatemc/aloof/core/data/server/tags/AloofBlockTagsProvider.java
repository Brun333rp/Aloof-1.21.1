package com.syndicatemc.aloof.core.data.server.tags;

import com.syndicatemc.aloof.core.Aloof;
import com.syndicatemc.aloof.core.other.AloofConstants;
import com.syndicatemc.aloof.core.other.tags.AloofBlockTags;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.mehvahdjukaar.supplementaries.reg.ModTags;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

import static com.syndicatemc.aloof.core.registry.AloofBlocks.*;

public class AloofBlockTagsProvider extends BlockTagsProvider {
    public AloofBlockTagsProvider(PackOutput output, CompletableFuture<Provider> provider, ExistingFileHelper helper) {
        super(output, provider, Aloof.MOD_ID, helper);
    }

    @Override
    public void addTags(Provider provider) {
        this.tag(BlockTags.MINEABLE_WITH_HOE).add(
                LOOFAH_SPONGE.get(), WET_LOOFAH_SPONGE.get(), SILPHIUM_BUNDLE.get()
        );
        this.tag(BlockTags.MINEABLE_WITH_AXE).add(
                LOOFAH_GOURD.get(), PITCHER_ROOT.get(), PITCHER_ROOT_BUNDLE.get()
        );
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
                PUMICE.get(), SOAKED_PUMICE.get(),
                SMOOTH_PUMICE.get(), POLISHED_PUMICE.get(),
                PUMICE_BRICKS.get()
        );
        this.tag(BlockTags.SWORD_EFFICIENT).add(
                LOOFAH_CROP.get(), LOOFAH_GOURD.get(),
                SILPHIUM_CROP.get(),
                TORCHFLOWER_CROP.get(), TORCHFLOWER.get()
            );
        this.tag(BlockTags.NEEDS_STONE_TOOL).add(
                PUMICE.get(), SOAKED_PUMICE.get(),
                SMOOTH_PUMICE.get(), POLISHED_PUMICE.get(),
                PUMICE_BRICKS.get()
        );

        this.tag(BlockTags.CROPS).add(LOOFAH_CROP.get(), SILPHIUM_CROP.get(), TORCHFLOWER_CROP.get());
        this.tag(BlockTags.MAINTAINS_FARMLAND).add(SILPHIUM_CROP.get(), TORCHFLOWER_CROP.get(), TORCHFLOWER.get());
        this.tag(BlockTags.BEE_GROWABLES).add(LOOFAH_CROP.get(), SILPHIUM_CROP.get(), TORCHFLOWER_CROP.get());
        this.tag(BlockTags.SMALL_FLOWERS).add(TORCHFLOWER.get());

        this.tag(AloofBlockTags.FARMLAND)
                .add(Blocks.FARMLAND)
                .addOptional(ResourceLocation.fromNamespaceAndPath(AloofConstants.FARMERS_DELIGHT, "rich_soil_farmland"))
                .addOptional(ResourceLocation.fromNamespaceAndPath(AloofConstants.MY_NETHERS_DELIGHT, "resurgent_soil_farmland"));

        this.tag(AloofBlockTags.LOOFAH_PLANTABLE_ON)
                .add(Blocks.MOSS_BLOCK)
                .addOptional(ResourceLocation.fromNamespaceAndPath(AloofConstants.FARMERS_DELIGHT, "rich_soil"));
        this.tag(AloofBlockTags.SILPHIUM_PLANTABLE_ON)
                .add(Blocks.FARMLAND)
                .addOptional(ResourceLocation.fromNamespaceAndPath(AloofConstants.FARMERS_DELIGHT, "rich_soil_farmland"))
                .addOptional(ResourceLocation.fromNamespaceAndPath(AloofConstants.SUPPLEMENTARIES, "planter"));
        this.tag(AloofBlockTags.TORCHFLOWER_PLANTABLE_ON)
                .addTags(AloofBlockTags.FARMLAND)
                .addOptional(ResourceLocation.fromNamespaceAndPath(AloofConstants.SUPPLEMENTARIES, "planter"));
        this.tag(AloofBlockTags.TORCHFLOWER_PLACEABLE_ON)
                .addTags(BlockTags.DIRT)
                .addTags(AloofBlockTags.TORCHFLOWER_PLANTABLE_ON)
                .addTags(BlockTags.SOUL_FIRE_BASE_BLOCKS);

        this.tag(AloofBlockTags.STORAGE_BLOCKS_SILPHIUM).add(SILPHIUM_BUNDLE.get());

        this.tag(ModTags.PLANTER_OFFSET_BLACKLIST).add(SILPHIUM_CROP.get());
    }
}
