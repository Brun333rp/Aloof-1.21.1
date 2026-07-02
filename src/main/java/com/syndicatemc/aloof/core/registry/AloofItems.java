package com.syndicatemc.aloof.core.registry;

import com.syndicatemc.aloof.common.item.HealingBombMinecartItem;
import com.syndicatemc.aloof.common.item.HealingSalveItem;
import com.syndicatemc.aloof.core.Aloof;
import com.teamabnormals.blueprint.core.util.item.CreativeModeTabContentsPopulator;
import com.teamabnormals.blueprint.core.util.registry.ItemSubRegistryHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.registries.DeferredItem;

import static net.minecraft.world.item.CreativeModeTabs.*;
import static net.minecraft.world.item.crafting.Ingredient.of;

public class AloofItems {
    public static final ItemSubRegistryHelper ITEMS = Aloof.REGISTRY_HELPER.getItemSubHelper();

    public static final DeferredItem<Item> LOOFAH_SEEDS = ITEMS.createItem("loofah_seeds", () -> new ItemNameBlockItem(AloofBlocks.LOOFAH_CROP.get(), new Item.Properties()));
    public static final DeferredItem<Item> SILPHIUM_SEEDS = ITEMS.createItem("silphium_seeds", () -> new ItemNameBlockItem(AloofBlocks.SILPHIUM_CROP.get(), new Item.Properties()));

    public static final DeferredItem<Item> SILPHIUM_FLOWER = ITEMS.createItem("silphium_flower", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> HEALING_SALVE = ITEMS.createItem("healing_salve", () -> new HealingSalveItem(new Item.Properties().durability(30)));
    public static final DeferredItem<Item> HEALING_BOMB_MINECART = ITEMS.createItem("healing_bomb_minecart", () -> new HealingBombMinecartItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> TORCHFLOWER_SEEDS = ITEMS.createItem("torchflower_seeds", () -> new ItemNameBlockItem(AloofBlocks.TORCHFLOWER_CROP.get(), new Item.Properties()));

    public static void setupTabEditors() {
        CreativeModeTabContentsPopulator.mod(Aloof.MOD_ID)
                .tab(NATURAL_BLOCKS)
                .addItemsBefore(of(Items.PITCHER_POD), TORCHFLOWER_SEEDS)
                .addItemsAfter(of(Items.PITCHER_POD), LOOFAH_SEEDS, SILPHIUM_SEEDS)
                .tab(REDSTONE_BLOCKS)
                .addItemsAfter(of(Items.TNT_MINECART), HEALING_BOMB_MINECART)
                .tab(TOOLS_AND_UTILITIES)
                .addItemsBefore(of(Items.NAME_TAG), HEALING_SALVE)
                .addItemsAfter(of(Items.TNT_MINECART), HEALING_BOMB_MINECART)
                .tab(COMBAT)
                .addItemsBefore(of(Items.TOTEM_OF_UNDYING), HEALING_SALVE)
                .tab(INGREDIENTS)
                .addItemsAfter(of(Items.HONEYCOMB), SILPHIUM_FLOWER);
    }
}
