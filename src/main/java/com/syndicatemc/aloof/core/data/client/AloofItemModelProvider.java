package com.syndicatemc.aloof.core.data.client;

import com.syndicatemc.aloof.core.Aloof;
import com.teamabnormals.blueprint.core.data.client.BlueprintItemModelProvider;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredItem;

import static com.syndicatemc.aloof.core.registry.AloofItems.*;

public class AloofItemModelProvider extends BlueprintItemModelProvider {
    public AloofItemModelProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, Aloof.MOD_ID, helper);
    }

    @Override
    protected void registerModels() {
        for (DeferredItem<?> item : new DeferredItem[]{
                LOOFAH_SEEDS, SILPHIUM_SEEDS,
                SILPHIUM_FLOWER,

                TORCHFLOWER_SEEDS,

                HEALING_SALVE, HEALING_BOMB_MINECART
        }) {
            this.basicItem(item.get());
        }
    }
}
