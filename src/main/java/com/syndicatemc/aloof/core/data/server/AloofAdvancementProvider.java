package com.syndicatemc.aloof.core.data.server;

import com.syndicatemc.aloof.core.Aloof;
import com.syndicatemc.aloof.core.registry.AloofCriteriaTriggers;
import com.syndicatemc.aloof.core.registry.AloofItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.AdvancementProvider.AdvancementGenerator;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class AloofAdvancementProvider implements AdvancementGenerator {
    public static AdvancementProvider create(PackOutput output, CompletableFuture<Provider> provider, ExistingFileHelper helper) {
        return new AdvancementProvider(output, provider, helper, List.of(new AloofAdvancementProvider()));
    }

    @Override
    public void generate(Provider provider, Consumer<AdvancementHolder> consumer, ExistingFileHelper helper) {
        createAdvancement("use_salve_on_warden", "adventure", ResourceLocation.withDefaultNamespace("adventure/avoid_vibration"), AloofItems.HEALING_SALVE.get(), AdvancementType.CHALLENGE, true, true, true)
                .addCriterion("use_salve_on_warden", AloofCriteriaTriggers.useSalveOnWarden())
                .save(consumer, Aloof.MOD_ID + ":adventure/use_salve_on_warden");
    }

    private static Advancement.Builder createAdvancement(String name, String category, AdvancementHolder parent, ItemLike icon, AdvancementType frame, boolean showToast, boolean announceToChat, boolean hidden) {
        return Advancement.Builder.advancement().parent(parent).display(icon,
                Component.translatable("advancements." + Aloof.MOD_ID + "." + category + "." + name + ".title"),
                Component.translatable("advancements." + Aloof.MOD_ID + "." + category + "." + name + ".description"),
                null, frame, showToast, announceToChat, hidden);
    }

    private static Advancement.Builder createAdvancement(String name, String category, ResourceLocation parent, ItemLike icon, AdvancementType frame, boolean showToast, boolean announceToChat, boolean hidden) {
        return createAdvancement(name, category, Advancement.Builder.advancement().build(parent), icon, frame, showToast, announceToChat, hidden);
    }
}
