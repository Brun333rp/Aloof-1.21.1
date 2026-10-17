package com.syndicatemc.aloof.core.data.server.modifiers;

import com.syndicatemc.aloof.core.Aloof;
import com.syndicatemc.aloof.core.registry.AloofBlocks;
import com.teamabnormals.blueprint.common.advancement.modification.AdvancementModifierProvider;
import com.teamabnormals.blueprint.common.advancement.modification.modifiers.CriteriaModifier;
import net.minecraft.advancements.critereon.ItemUsedOnLocationTrigger;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;

public class AloofAdvancementModifierProvider extends AdvancementModifierProvider {
    public AloofAdvancementModifierProvider(PackOutput output, CompletableFuture<Provider> provider) {
        super(Aloof.MOD_ID, output, provider);
    }

    @Override
    protected void registerEntries(Provider provider) {
        String plantSnifferSeed = "husbandry/plant_any_sniffer_seed";
        this.entry(plantSnifferSeed).selects(plantSnifferSeed).addModifier(CriteriaModifier.builder(this.modId)
                .addCriterion("loofah_crop", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(AloofBlocks.LOOFAH_CROP.get()))
                .addCriterion("silphium_crop", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(AloofBlocks.SILPHIUM_CROP.get()))
                .addIndexedRequirements(0, false, "loofah_crop", "silphium_crop").build());

        String plantSeed = "husbandry/plant_seed";
        this.entry(plantSeed).selects(plantSeed).addModifier(CriteriaModifier.builder(this.modId)
                .addCriterion("loofah_crop", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(AloofBlocks.LOOFAH_CROP.get()))
                .addCriterion("silphium_crop", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(AloofBlocks.SILPHIUM_CROP.get()))
                .addIndexedRequirements(0, false, "loofah_crop", "silphium_crop").build());
    }
}
