package com.syndicatemc.aloof.common.block;

import com.syndicatemc.aloof.core.registry.AloofBlocks;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;

public class PumiceBlock extends AbstractSpongeBlock{
    public PumiceBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    protected int getMaxDepth() {
        return 3;
    }

    protected int getMaxCount() {
        return 16;
    }

    protected boolean isWaterSponge() {
        return false;
    }

    protected TagKey<Fluid> getFluid() {
        return FluidTags.LAVA;
    }

    protected BlockState getSaturatedBlock() {
        return AloofBlocks.SOAKED_PUMICE.get().defaultBlockState();
    }

    protected SoundEvent getSound() {
        return SoundEvents.BUCKET_FILL_LAVA;
    }
}
