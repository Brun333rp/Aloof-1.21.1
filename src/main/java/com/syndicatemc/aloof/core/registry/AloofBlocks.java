package com.syndicatemc.aloof.core.registry;

import com.syndicatemc.aloof.common.block.*;
import com.syndicatemc.aloof.core.Aloof;
import com.teamabnormals.blueprint.common.block.BlueprintDirectionalBlock;
import com.teamabnormals.blueprint.core.util.PropertyUtil;
import com.teamabnormals.blueprint.core.util.item.CreativeModeTabContentsPopulator;
import com.teamabnormals.blueprint.core.util.registry.BlockSubRegistryHelper;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;

import static net.minecraft.world.item.CreativeModeTabs.*;
import static net.minecraft.world.item.crafting.Ingredient.of;

public class AloofBlocks {
    public static final BlockSubRegistryHelper BLOCKS = Aloof.REGISTRY_HELPER.getBlockSubHelper();

    public static final DeferredBlock<Block> LOOFAH_CROP = BLOCKS.createBlockNoItem("loofah_crop", () -> new LoofahCropBlock(AloofProperties.LOOFAH_CROP));
    public static final DeferredBlock<Block> LOOFAH_GOURD = BLOCKS.createBlock("loofah_gourd", () -> new LoofahGourdBlock(AloofProperties.LOOFAH_GOURD));
    public static final DeferredBlock<Block> LOOFAH_SPONGE = BLOCKS.createBlock("loofah_sponge", () -> new LoofahSpongeBlock(AloofProperties.LOOFAH_SPONGE));
    public static final DeferredBlock<Block> WET_LOOFAH_SPONGE = BLOCKS.createBlock("wet_loofah_sponge", () -> new WetLoofahSpongeBlock(AloofProperties.WET_LOOFAH_SPONGE));

    public static final DeferredBlock<Block> SILPHIUM_CROP = BLOCKS.createBlockNoItem("silphium_crop", () -> new SilphiumCropBlock(AloofProperties.SILPHIUM_CROP));
    public static final DeferredBlock<Block> SILPHIUM_BUNDLE = BLOCKS.createBlock("silphium_bundle", () -> new BlueprintDirectionalBlock(AloofProperties.SILPHIUM_BUNDLE));
    public static final DeferredBlock<Block> HEALING_BOMB = BLOCKS.createBlock("healing_bomb", () -> new HealingBombBlock(AloofProperties.HEALING_BOMB));

    public static final DeferredBlock<Block> TORCHFLOWER = BLOCKS.createBlock("torchflower", () -> new NewTorchflowerBlock(AloofProperties.TORCHFLOWER));
    public static final DeferredBlock<Block> TORCHFLOWER_CROP = BLOCKS.createBlockNoItem("torchflower_crop", () -> new NewTorchflowerCropBlock(AloofProperties.TORCHFLOWER_CROP));
    public static final DeferredBlock<Block> POTTED_TORCHFLOWER = BLOCKS.createBlock("potted_torchflower", () -> new FlowerPotBlock(TORCHFLOWER.get(), PropertyUtil.flowerPot()));

    public static final DeferredBlock<Block> PITCHER_ROOT = BLOCKS.createBlock("pitcher_root", () -> new PitcherRootBlock(AloofProperties.PITCHER_ROOT));
    public static final DeferredBlock<Block> PITCHER_ROOT_BUNDLE = BLOCKS.createBlock("pitcher_root_bundle", () -> new BlueprintDirectionalBlock(AloofProperties.PITCHER_ROOT_BUNDLE));

    public static final DeferredBlock<Block> PUMICE = BLOCKS.createBlock("pumice", () -> new PumiceBlock(AloofProperties.PUMICE));
    public static final DeferredBlock<Block> SOAKED_PUMICE = BLOCKS.createBlock("soaked_pumice", () -> new SoakedPumiceBlock(AloofProperties.SOAKED_PUMICE));
    public static final DeferredBlock<Block> SMOOTH_PUMICE = BLOCKS.createBlock("smooth_pumice", () -> new Block(AloofProperties.PUMICE));
    public static final DeferredBlock<Block> POLISHED_PUMICE = BLOCKS.createBlock("polished_pumice", () -> new Block(AloofProperties.PUMICE));
    public static final DeferredBlock<Block> PUMICE_BRICKS = BLOCKS.createBlock("pumice_bricks", () -> new PumiceBricksBlock(AloofProperties.PUMICE_BRICKS));

    public static final class AloofProperties {
        public static final Properties LOOFAH_CROP = Properties.ofFullCopy(Blocks.WHEAT).offsetType(BlockBehaviour.OffsetType.XZ).mapColor(MapColor.COLOR_CYAN);
        public static final Properties LOOFAH_GOURD = Properties.ofFullCopy(Blocks.CRIMSON_PLANKS).mapColor(MapColor.COLOR_LIGHT_GREEN);
        public static final Properties LOOFAH_SPONGE = Properties.ofFullCopy(Blocks.SPONGE).mapColor(MapColor.TERRACOTTA_YELLOW);
        public static final Properties WET_LOOFAH_SPONGE = Properties.ofFullCopy(Blocks.WET_SPONGE).sound(SoundType.MUDDY_MANGROVE_ROOTS).mapColor(MapColor.TERRACOTTA_YELLOW);

        public static final Properties PUMICE = Properties.ofFullCopy(Blocks.BLACKSTONE).sound(SoundType.DRIPSTONE_BLOCK).mapColor(MapColor.COLOR_GRAY);
        public static final Properties SOAKED_PUMICE = Properties.ofFullCopy(Blocks.BLACKSTONE).sound(SoundType.DRIPSTONE_BLOCK).mapColor(MapColor.COLOR_ORANGE).lightLevel(state -> 12);
        public static final Properties PUMICE_BRICKS = Properties.ofFullCopy(Blocks.BLACKSTONE).sound(SoundType.DRIPSTONE_BLOCK).mapColor(MapColor.COLOR_GRAY).lightLevel(state -> state.getValue(PumiceBricksBlock.SATURATED) * 4);

        public static final Properties SILPHIUM_CROP = Properties.ofFullCopy(Blocks.WHEAT).offsetType(BlockBehaviour.OffsetType.XZ).mapColor(MapColor.TERRACOTTA_LIGHT_GREEN);
        public static final Properties SILPHIUM_BUNDLE = Properties.ofFullCopy(Blocks.HAY_BLOCK).mapColor(MapColor.TERRACOTTA_LIGHT_GREEN);
        public static final Properties HEALING_BOMB = Properties.ofFullCopy(Blocks.TNT).mapColor(MapColor.TERRACOTTA_YELLOW);

        public static final Properties TORCHFLOWER = Properties.ofFullCopy(Blocks.TORCHFLOWER).lightLevel(state -> state.getValue(NewTorchflowerBlock.LIT) ? state.getValue(NewTorchflowerBlock.SOUL) ? 10 : 15 : 0);
        public static final Properties TORCHFLOWER_CROP = Properties.ofFullCopy(Blocks.TORCHFLOWER_CROP).offsetType(BlockBehaviour.OffsetType.XZ);

        public static final Properties PITCHER_ROOT = Properties.ofFullCopy(Blocks.CRIMSON_PLANKS).mapColor(MapColor.TERRACOTTA_WHITE).destroyTime(1.0F);
        public static final Properties PITCHER_ROOT_BUNDLE = Properties.ofFullCopy(Blocks.CRIMSON_PLANKS).mapColor(MapColor.TERRACOTTA_WHITE);
    }

    public static void setupTabEditors() {
        CreativeModeTabContentsPopulator.mod(Aloof.MOD_ID)
                .tab(NATURAL_BLOCKS)
                .addItemsAfter(of(Items.LILY_OF_THE_VALLEY), TORCHFLOWER)
                .addItemsAfter(of(Items.JACK_O_LANTERN),
                        LOOFAH_GOURD,
                        LOOFAH_SPONGE,
                        WET_LOOFAH_SPONGE,
                        PITCHER_ROOT_BUNDLE,
                        SILPHIUM_BUNDLE
                )
                .tab(REDSTONE_BLOCKS)
                .addItemsAfter(of(Items.TNT), HEALING_BOMB)
                .tab(COMBAT)
                .addItemsAfter(of(Items.TNT), HEALING_BOMB)
                .tab(FOOD_AND_DRINKS)
                .addItemsAfter(of(Items.CAKE), PITCHER_ROOT);
    }
}
