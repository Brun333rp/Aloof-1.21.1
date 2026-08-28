package com.syndicatemc.aloof.core.data.server;

import com.syndicatemc.aloof.core.registry.AloofBlocks;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

import java.util.concurrent.CompletableFuture;

import static com.syndicatemc.aloof.core.registry.AloofBlocks.*;
import static com.syndicatemc.aloof.core.registry.AloofItems.*;

public class AloofDataMapProvider extends DataMapProvider {
    public AloofDataMapProvider(PackOutput output, CompletableFuture<Provider> provider) {
        super(output, provider);
    }

    @Override
    protected void gather(Provider provider) {
        this.builder(NeoForgeDataMaps.COMPOSTABLES)
                .add(LOOFAH_GOURD.getId(), new Compostable(0.65F), false)
                .add(LOOFAH_SPONGE.getId(), new Compostable(0.5F), false)
                .add(WET_LOOFAH_SPONGE.getId(), new Compostable(0.5F), false)
                .add(LOOFAH_SEEDS, new Compostable(0.3F), false)

                .add(SILPHIUM_FLOWER, new Compostable(0.65F), false)
                .add(SILPHIUM_SEEDS, new Compostable(0.3F), false)
                .add(SILPHIUM_BUNDLE.getId(), new Compostable(0.85F), false)

                .add(TORCHFLOWER.getId(), new Compostable(0.85F), false)
                .add(TORCHFLOWER_SEEDS, new Compostable(0.3F), false)

                .add(PITCHER_ROOT.getId(), new Compostable(0.65F), false)
                .add(PITCHER_ROOT_BUNDLE.getId(), new Compostable(0.85F), false);
    }
}
