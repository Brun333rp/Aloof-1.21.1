package com.syndicatemc.aloof.client.model;

import com.syndicatemc.aloof.common.entity.living.HarvestChild;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.SheepModel;
import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class HarvestChildModel<T extends LivingEntity> extends HierarchicalModel<T> {
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;

    public HarvestChildModel(ModelPart root) {
        this.root = root.getChild("root");
        this.head = this.root.getChild("head");
        this.body = this.root.getChild("body");
        this.rightLeg = this.root.getChild("right_leg");
        this.leftLeg = this.root.getChild("left_leg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();

        PartDefinition root = partDefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-6.0F, -6.0F, -6.0F, 12.0F, 6.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(16, 18).addBox(-5.0F, -16.0F, 0.0F, 10.0F, 10.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(16, 18).addBox(-5.0F, -16.0F, 0.0F, 10.0F, 10.0F, 0.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, -4.0F, 0.0F));

        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 18).addBox(-2.0F, -3.0F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 0.0F));

        PartDefinition leftLeg = root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(2, 25).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, -1.0F, 0.0F));

        PartDefinition rightLeg = root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 25).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, -1.0F, 0.0F));

        return LayerDefinition.create(meshDefinition, 48, 48);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity instanceof HarvestChild) {
            Vec3 movement = entity.getDeltaMovement();
            float mod = movement.x > 0.01F || movement.x < -0.01F || movement.z > 0.01F || movement.z < -0.01F ? 20.0F : 1.0F;

            this.head.yRot = (netHeadYaw * (float)Math.PI / 180F);
            this.head.xRot = (headPitch * (float)Math.PI / 180F);
            this.head.zRot = 0.1F * (float)Math.sin(0.025F * ageInTicks * mod);
            this.body.zRot = 0.02F * (float)Math.sin(0.025F * ageInTicks * mod);

            this.rightLeg.xRot = -2F * Mth.triangleWave(limbSwing, 13F) * limbSwingAmount;
            this.leftLeg.xRot = 2F * Mth.triangleWave(limbSwing, 13F) * limbSwingAmount;
        }
    }
}