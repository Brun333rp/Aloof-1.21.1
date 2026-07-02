package com.syndicatemc.aloof.core.other.tags;

import com.teamabnormals.blueprint.core.util.TagUtil;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class AloofItemTags {
    public static final TagKey<Item> ASH = commonTag("dusts/ash");

    public static final TagKey<Item> SEEDS_LOOFAH = commonTag("seeds/loofah");
    public static final TagKey<Item> SEEDS_SILPHIUM = commonTag("seeds/silphium");

    public static final TagKey<Item> STORAGE_BLOCKS_SILPHIUM = commonTag("storage_blocks/silphium");

    private static TagKey<Item> commonTag(String key) {
        return TagUtil.itemTag("c", key);
    }
}
