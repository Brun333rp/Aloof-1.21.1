package com.syndicatemc.aloof.core.other.tags;

import com.syndicatemc.aloof.core.Aloof;
import com.teamabnormals.blueprint.core.util.TagUtil;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class AloofBlockTags {
    public static final TagKey<Block> LOOFAH_PLANTABLE_ON = modTag("loofah_plantable_on");
    public static final TagKey<Block> SILPHIUM_PLANTABLE_ON = modTag("silphium_plantable_on");

    public static final TagKey<Block> FARMLAND = modTag("farmland");

    public static final TagKey<Block> STORAGE_BLOCKS_SILPHIUM = commonTag("storage_blocks/silphium");

    private static TagKey<Block> modTag(String key) {
        return TagUtil.blockTag(Aloof.MOD_ID, key);
    }

    private static TagKey<Block> commonTag(String key) {
        return TagUtil.blockTag("c", key);
    }
}
