package com.syndicatemc.aloof.client.renderer.entity;

import com.syndicatemc.aloof.client.model.HarvestChildModel;
import com.syndicatemc.aloof.common.entity.living.HarvestChild;
import com.syndicatemc.aloof.core.Aloof;
import com.syndicatemc.aloof.core.other.AloofModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class HarvestChildRenderer extends MobRenderer<HarvestChild, HarvestChildModel<HarvestChild>> {
    public HarvestChildRenderer(EntityRendererProvider.Context context) {
        super(context, new HarvestChildModel<>(context.bakeLayer(AloofModelLayers.HARVEST_CHILD)), 0.35F);
    }

    @Override
    public ResourceLocation getTextureLocation(HarvestChild child) {
        return Aloof.location("textures/entity/harvest_child.png");
    }
}
