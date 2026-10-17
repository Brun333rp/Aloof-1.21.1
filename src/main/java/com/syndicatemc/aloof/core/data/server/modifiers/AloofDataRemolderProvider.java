package com.syndicatemc.aloof.core.data.server.modifiers;

import com.syndicatemc.aloof.common.block.SilphiumCropBlock;
import com.syndicatemc.aloof.core.Aloof;
import com.syndicatemc.aloof.core.registry.AloofBlocks;
import com.syndicatemc.aloof.core.registry.AloofItems;
import com.teamabnormals.blueprint.common.remolder.data.RemolderProvider;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntries;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.teamabnormals.blueprint.common.remolder.RemolderTypes.replace;
import static com.teamabnormals.blueprint.common.remolder.RemolderTypes.sequence;
import static com.teamabnormals.blueprint.common.remolder.data.DynamicReference.target;
import static com.teamabnormals.blueprint.common.remolder.data.DynamicReference.value;
import static com.teamabnormals.blueprint.common.remolder.util.LootRemolders.*;

public class AloofDataRemolderProvider extends RemolderProvider {
    public AloofDataRemolderProvider(PackOutput output, CompletableFuture<Provider> provider) {
        super(Aloof.MOD_ID, PackOutput.Target.DATA_PACK, output, provider);
    }

    @Override
    protected void registerEntries(Provider provider) {
        String snifferDigging = "loot_table/gameplay/sniffer_digging";
        this.entry(snifferDigging)
                .path(snifferDigging)
                .remolder(sequence(
                        addEntry(0, LootItem.lootTableItem(AloofItems.LOOFAH_SEEDS).build()),
                        addEntry(0, LootItem.lootTableItem(AloofItems.SILPHIUM_SEEDS).build())
                ));

        String pitcherPlant = "loot_table/blocks/pitcher_crop";
        this.entry(pitcherPlant)
                .path(pitcherPlant)
                .remolder(sequence(
                        addPool(LootPool.lootPool()
                                .name(Aloof.MOD_ID + ":pitcher_root")
                                .setRolls(ConstantValue.exactly(1))
                                .add(LootItem.lootTableItem(AloofBlocks.PITCHER_ROOT.asItem())
                                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.PITCHER_CROP)
                                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                                        .hasProperty(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER).hasProperty(BlockStateProperties.AGE_4, 4))))
                                .build()),
                        addPool(LootPool.lootPool()
                                .name(Aloof.MOD_ID + ":pitcher_pod")
                                .setRolls(ConstantValue.exactly(1))
                                .add(LootItem.lootTableItem(Items.PITCHER_POD)
                                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.PITCHER_CROP)
                                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                                        .hasProperty(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER).hasProperty(BlockStateProperties.AGE_4, 4))))
                                .build())
                ));
    }
}
