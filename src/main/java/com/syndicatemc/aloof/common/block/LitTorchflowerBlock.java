package com.syndicatemc.aloof.common.block;

import com.syndicatemc.aloof.core.other.AloofUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class LitTorchflowerBlock extends FlowerBlock {
    public LitTorchflowerBlock(BlockBehaviour.Properties properties) {
        super(MobEffects.GLOWING, 0, properties);
    }

    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        Vec3 offset = state.getOffset(level, pos);
        double x = pos.getX() + 0.5D + AloofUtils.getVariance(random, 0.2F) + offset.x;
        double y = pos.getY() + 0.75D + AloofUtils.getVariance(random, 0.2F);
        double z = pos.getZ() + 0.5D + AloofUtils.getVariance(random, 0.2F) + offset.z;
        level.addParticle(ParticleTypes.FLAME, x, y, z, 0.0D, 0.0D, 0.0D);
    }

    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        return new ItemStack(Items.TORCHFLOWER);
    }
}
