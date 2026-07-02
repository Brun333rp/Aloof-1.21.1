package com.syndicatemc.aloof.common.block;

import com.syndicatemc.aloof.core.registry.AloofBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WetSpongeBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class WetLoofahSpongeBlock extends WetSpongeBlock {
    public WetLoofahSpongeBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (level.dimensionType().ultraWarm()) {
            level.setBlock(pos, AloofBlocks.LOOFAH_SPONGE.get().defaultBlockState(), 3);
            level.levelEvent(2009, pos, 0);
            level.playSound(null, pos, SoundEvents.WET_SPONGE_DRIES, SoundSource.BLOCKS, 1.0F, (1.0F + level.getRandom().nextFloat() * 0.2F) * 0.7F);
        }
    }
}
