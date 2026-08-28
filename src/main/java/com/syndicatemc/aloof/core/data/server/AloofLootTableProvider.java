package com.syndicatemc.aloof.core.data.server;

import com.google.common.collect.ImmutableList;
import com.syndicatemc.aloof.common.block.PitcherRootBlock;
import com.syndicatemc.aloof.common.block.SilphiumCropBlock;
import com.syndicatemc.aloof.core.Aloof;
import com.syndicatemc.aloof.core.registry.AloofBlocks;
import com.syndicatemc.aloof.core.registry.AloofItems;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.syndicatemc.aloof.core.registry.AloofBlocks.*;

public class AloofLootTableProvider extends LootTableProvider {
    public AloofLootTableProvider(PackOutput output, CompletableFuture<Provider> provider) {
        super(output, BuiltInLootTables.all(), ImmutableList.of(
                new LootTableProvider.SubProviderEntry(AloofBlockLoot::new, LootContextParamSets.BLOCK)
        ), provider);
    }

    @Override
    protected void validate(WritableRegistry<LootTable> registry, ValidationContext context, ProblemReporter.Collector collector) {}

    private static class AloofBlockLoot extends BlockLootSubProvider {
        private static final Set<Item> EXPLOSION_RESISTANT = Stream.of(Blocks.DRAGON_EGG, Blocks.BEACON, Blocks.CONDUIT, Blocks.SKELETON_SKULL, Blocks.WITHER_SKELETON_SKULL, Blocks.PLAYER_HEAD, Blocks.ZOMBIE_HEAD, Blocks.CREEPER_HEAD, Blocks.DRAGON_HEAD, Blocks.PIGLIN_HEAD, Blocks.SHULKER_BOX, Blocks.BLACK_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.LIGHT_GRAY_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.WHITE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX).map(ItemLike::asItem).collect(Collectors.toSet());

        protected AloofBlockLoot(Provider provider) {
            super(EXPLOSION_RESISTANT, FeatureFlags.REGISTRY.allFlags(), provider);
        }

        @Override
        public void generate() {
            for (DeferredBlock<?> block : new DeferredBlock[]{
                    LOOFAH_GOURD, LOOFAH_SPONGE, WET_LOOFAH_SPONGE,
                    SILPHIUM_BUNDLE, HEALING_BOMB,

                    TORCHFLOWER,
                    PITCHER_ROOT_BUNDLE,

                    PUMICE, SOAKED_PUMICE,
                    SMOOTH_PUMICE, POLISHED_PUMICE,
                    PUMICE_BRICKS
            }) {
                this.dropSelf(block.get());
            }

            loofahCropLootTable(LOOFAH_CROP);
            silphiumCropLootTable(SILPHIUM_CROP);
            this.dropOther(TORCHFLOWER_CROP.get(), AloofItems.TORCHFLOWER_SEEDS);
            this.dropPottedContents(POTTED_TORCHFLOWER.get());
            pitcherRootLootTable(PITCHER_ROOT);
        }

        @Override
        public Iterable<Block> getKnownBlocks() {
            return BuiltInRegistries.BLOCK.stream().filter(block -> BuiltInRegistries.BLOCK.getKey(block).getNamespace().equals(Aloof.MOD_ID)).collect(Collectors.toSet());
        }

        private void loofahCropLootTable(DeferredBlock<?> loofahObject) {
            Block loofah = loofahObject.get();
            this.add(loofah, LootTable.lootTable().withPool(this.applyExplosionCondition(loofah, LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1.0F))
                    .add(LootItem.lootTableItem(AloofItems.LOOFAH_SEEDS)
                            .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(loofah)
                                    .setProperties(StatePropertiesPredicate.Builder.properties()
                                            .hasProperty(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER))))))
            );
        }

        private void silphiumCropLootTable(DeferredBlock<?> silphiumObject) {
            Block silphium = silphiumObject.get();
            this.add(silphium, LootTable.lootTable().withPool(this.applyExplosionCondition(silphium, LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1.0F))
                    .add(LootItem.lootTableItem(AloofItems.SILPHIUM_SEEDS)
                            .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(silphium)
                                    .setProperties(StatePropertiesPredicate.Builder.properties()
                                            .hasProperty(SilphiumCropBlock.SECTION, 1)))))
                    .add(LootItem.lootTableItem(AloofItems.SILPHIUM_FLOWER)
                            .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(silphium)
                                    .setProperties(StatePropertiesPredicate.Builder.properties()
                                            .hasProperty(SilphiumCropBlock.SECTION, 4).hasProperty(SilphiumCropBlock.AGE, 9)))))
            );
        }

        private void pitcherRootLootTable(DeferredBlock<?> pitcherObject) {
            Block pitcher = pitcherObject.get();
            this.add(pitcher, LootTable.lootTable().withPool(this.applyExplosionCondition(pitcher, LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1.0F))
                    .add(LootItem.lootTableItem(PITCHER_ROOT.asItem())
                            .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(pitcher)
                                    .setProperties(StatePropertiesPredicate.Builder.properties()
                                            .hasProperty(PitcherRootBlock.EATEN, 0))))))
            );
        }
    }
}
