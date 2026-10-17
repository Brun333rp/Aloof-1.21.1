package com.syndicatemc.aloof.core.other;

import com.syndicatemc.aloof.client.model.HarvestChildModel;
import com.syndicatemc.aloof.client.renderer.HealingBombMinecartRenderer;
import com.syndicatemc.aloof.client.renderer.HealingBombRenderer;
import com.syndicatemc.aloof.client.renderer.entity.HarvestChildRenderer;
import com.syndicatemc.aloof.common.entity.living.HarvestChild;
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
    public static final ModelLayerLocation HARVEST_CHILD = register("harvest_child", "main");

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(HEALING_BOMB_MINECART, MinecartModel::createBodyLayer);
        event.registerLayerDefinition(HARVEST_CHILD, HarvestChildModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(AloofEntityTypes.HEALING_BOMB.value(), HealingBombRenderer::new);
        event.registerEntityRenderer(AloofEntityTypes.HEALING_BOMB_MINECART.value(), HealingBombMinecartRenderer::new);

        event.registerEntityRenderer(AloofEntityTypes.HARVEST_CHILD.value(), HarvestChildRenderer::new);
    }

    public static ModelLayerLocation register(String name, String layer) {
        return new ModelLayerLocation(Aloof.location(name), layer);
    }
}
