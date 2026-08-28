package com.syndicatemc.aloof.core.other;

import com.syndicatemc.aloof.client.renderer.HealingBombMinecartRenderer;
import com.syndicatemc.aloof.client.renderer.HealingBombRenderer;
import com.syndicatemc.aloof.core.Aloof;
import com.syndicatemc.aloof.core.registry.AloofEntityTypes;
import net.minecraft.client.model.MinecartModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = Aloof.MOD_ID, value = Dist.CLIENT)
public class AloofModelLayers {
    public static final ModelLayerLocation HEALING_BOMB_MINECART = register("tmt_minecart", "main");

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(HEALING_BOMB_MINECART, MinecartModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(AloofEntityTypes.HEALING_BOMB.value(), HealingBombRenderer::new);
        event.registerEntityRenderer(AloofEntityTypes.HEALING_BOMB_MINECART.value(), HealingBombMinecartRenderer::new);
    }

    public static ModelLayerLocation register(String name, String layer) {
        return new ModelLayerLocation(Aloof.location(name), layer);
    }
}
