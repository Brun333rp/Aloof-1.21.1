package com.syndicatemc.aloof.core.other;

import com.syndicatemc.aloof.core.registry.AloofBlocks;
import com.syndicatemc.aloof.core.registry.AloofItems;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.neoforge.registries.DeferredBlock;

import static com.syndicatemc.aloof.core.registry.AloofBlocks.*;

@SuppressWarnings("removal")
public class AloofClientCompat {
    public static void registerCompat() {
        AloofBlocks.setupTabEditors();
        AloofItems.setupTabEditors();
        registerRenderLayers();
        registerItemProperties();
    }
    public static void registerRenderLayers() {
        for (DeferredBlock<?> block : new DeferredBlock[]{
                LOOFAH_CROP, SILPHIUM_CROP, TORCHFLOWER, TORCHFLOWER_CROP, POTTED_TORCHFLOWER, PITCHER_ROOT
        }) {
            ItemBlockRenderTypes.setRenderLayer(block.get(), RenderType.cutout());
        }
    }

    public static void registerItemProperties() {

    }
}
