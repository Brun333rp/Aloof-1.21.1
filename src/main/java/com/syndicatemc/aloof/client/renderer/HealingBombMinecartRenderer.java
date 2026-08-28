package com.syndicatemc.aloof.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.syndicatemc.aloof.common.entity.MinecartHealingBomb;
import com.syndicatemc.aloof.core.other.AloofModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MinecartRenderer;
import net.minecraft.client.renderer.entity.TntMinecartRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;

public class HealingBombMinecartRenderer extends MinecartRenderer<MinecartHealingBomb> {
    private final BlockRenderDispatcher blockRenderer;

    public HealingBombMinecartRenderer(EntityRendererProvider.Context context) {
        super(context, AloofModelLayers.HEALING_BOMB_MINECART);
        this.blockRenderer = context.getBlockRenderDispatcher();
    }

    @Override
    protected void renderMinecartContents(MinecartHealingBomb entity, float partialTicks, BlockState state, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        int i = entity.getFuse();
        if (i > -1 && i - partialTicks + 1.0F < 10.0F) {
            float f = 1.0F - (i - partialTicks + 1.0F) / 10.0F;
            f = Mth.clamp(f, 0.0F, 1.0F);
            f *= f;
            f *= f;
            float f1 = 1.0F + f * 0.3F;
            poseStack.scale(f1, f1, f1);
        }

        TntMinecartRenderer.renderWhiteSolidBlock(this.blockRenderer, state, poseStack, buffer, packedLight, i > -1 && i / 5 % 2 == 0);
    }
}
