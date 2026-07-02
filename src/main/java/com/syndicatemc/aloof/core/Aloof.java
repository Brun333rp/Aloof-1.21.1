package com.syndicatemc.aloof.core;

import com.syndicatemc.aloof.core.data.client.AloofBlockStateProvider;
import com.syndicatemc.aloof.core.data.client.AloofItemModelProvider;
import com.syndicatemc.aloof.core.data.client.AloofSpriteSourceProvider;
import com.syndicatemc.aloof.core.data.server.*;
import com.syndicatemc.aloof.core.data.server.modifiers.AloofAdvancementModifierProvider;
import com.syndicatemc.aloof.core.data.server.modifiers.AloofDataRemolderProvider;
import com.syndicatemc.aloof.core.data.server.tags.AloofBlockTagsProvider;
import com.syndicatemc.aloof.core.data.server.tags.AloofEntityTypeTagsProvider;
import com.syndicatemc.aloof.core.data.server.tags.AloofItemTagsProvider;
import com.syndicatemc.aloof.core.other.AloofClientCompat;
import com.syndicatemc.aloof.core.other.AloofCompat;
import com.syndicatemc.aloof.core.registry.*;
import com.teamabnormals.blueprint.core.util.registry.BlockSubRegistryHelper;
import com.teamabnormals.blueprint.core.util.registry.RegistryHelper;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.concurrent.CompletableFuture;

@Mod(Aloof.MOD_ID)
public class Aloof {
    public static final String MOD_ID = "aloof";
    public static final RegistryHelper REGISTRY_HELPER = RegistryHelper.create(MOD_ID, helper -> helper.putSubHelper(Registries.BLOCK, new BlockSubRegistryHelper(helper)));
    public static final Logger LOGGER = LogManager.getLogger();

    public Aloof(IEventBus bus, ModContainer container) {
        AloofBlocks.BLOCKS.register(bus);
        AloofItems.ITEMS.register(bus);
        AloofEntityTypes.ENTITY_TYPES.register(bus);
        AloofParticleTypes.PARTICLE_TYPES.register(bus);
        AloofSoundEvents.SOUND_EVENTS.register(bus);
        AloofCriteriaTriggers.TRIGGERS.register(bus);

        bus.addListener(this::commonSetup);
        bus.addListener(this::clientSetup);
        bus.addListener(this::dataSetup);

        //container.registerConfig(ModConfig.Type.COMMON, AloofConfig.COMMON_SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(AloofCompat::registerCompat);
    }

    private void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(AloofClientCompat::registerCompat);
    }

    private void dataSetup(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();
        ExistingFileHelper helper = event.getExistingFileHelper();
        //AloofDataPackProvider datapack = new AloofDataPackProvider(output, provider);

        boolean server = event.includeServer();
        AloofBlockTagsProvider blockTags = new AloofBlockTagsProvider(output, provider, helper);
        generator.addProvider(server, blockTags);
        generator.addProvider(server, new AloofItemTagsProvider(output, provider, blockTags.contentsGetter(), helper));
        generator.addProvider(server, new AloofEntityTypeTagsProvider(output, provider, helper));
        generator.addProvider(server, new AloofLootTableProvider(output, provider));
        generator.addProvider(server, new AloofRecipeProvider(output, provider));
        generator.addProvider(server, new AloofDataRemolderProvider(output, provider));
        generator.addProvider(server, new AloofDataMapProvider(output, provider));
        generator.addProvider(server, AloofAdvancementProvider.create(output, provider, helper));
        generator.addProvider(server, new AloofAdvancementModifierProvider(output, provider));

        boolean client = event.includeClient();
        generator.addProvider(client, new AloofBlockStateProvider(output, helper));
        generator.addProvider(client, new AloofItemModelProvider(output, helper));
        generator.addProvider(client, new AloofSpriteSourceProvider(output, provider, helper));
    }

    public static ResourceLocation location(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
