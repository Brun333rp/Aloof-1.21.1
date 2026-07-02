package com.syndicatemc.aloof.common.block;

import com.syndicatemc.aloof.core.registry.AloofBlocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class LoofahSpongeBlock extends AbstractSpongeBlock {
    public LoofahSpongeBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected int getMaxDepth() {
        return 4;
    }

    @Override
    protected int getMaxCount() {
        return 32;
    }

    @Override
    protected BlockState getSaturatedBlock() {
        return AloofBlocks.WET_LOOFAH_SPONGE.get().defaultBlockState();
    }
}
