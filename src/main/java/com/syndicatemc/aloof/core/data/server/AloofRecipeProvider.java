package com.syndicatemc.aloof.core.data.server;

import com.google.common.collect.ImmutableList;
import com.syndicatemc.aloof.core.Aloof;
import com.syndicatemc.aloof.core.other.AloofConstants;
import com.syndicatemc.aloof.core.other.tags.AloofItemTags;
import com.syndicatemc.aloof.core.registry.AloofBlocks;
import com.syndicatemc.aloof.core.registry.AloofItems;
import com.teamabnormals.blueprint.core.data.server.BlueprintRecipeProvider;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import net.neoforged.neoforge.common.conditions.NotCondition;
import net.neoforged.neoforge.common.crafting.CompoundIngredient;
import vectorwing.farmersdelight.common.crafting.ingredient.ItemAbilityIngredient;
import vectorwing.farmersdelight.common.item.KnifeItem;
import vectorwing.farmersdelight.common.tag.CommonTags;
import vectorwing.farmersdelight.data.builder.CuttingBoardRecipeBuilder;

import java.util.concurrent.CompletableFuture;

import static net.minecraft.data.recipes.RecipeCategory.*;

public class AloofRecipeProvider extends BlueprintRecipeProvider {
    public static Ingredient KNIVES = matchesTool(KnifeItem.KNIFE_DIG, CommonTags.Items.TOOLS_KNIFE);
    public static Ingredient AXES_STRIP = matchesTool(ItemAbilities.AXE_STRIP, ItemTags.AXES);

    public AloofRecipeProvider(PackOutput output, CompletableFuture<Provider> provider) {
        super(Aloof.MOD_ID, output, provider);
    }

    @Override
    public void buildRecipes(RecipeOutput output) {
        smeltingRecipe(output, ImmutableList.of(AloofBlocks.WET_LOOFAH_SPONGE), MISC, AloofBlocks.LOOFAH_SPONGE, 0.1F, 200, "loofah");
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(AloofBlocks.LOOFAH_GOURD), AXES_STRIP, AloofBlocks.LOOFAH_SPONGE)
                .addResultWithChance(AloofItems.LOOFAH_SEEDS, 0.4F)
                .addSound(SoundEvents.AXE_STRIP)
                .build(output.withConditions(new ModLoadedCondition(AloofConstants.FARMERS_DELIGHT)), Aloof.location(getItemName(AloofBlocks.LOOFAH_GOURD)));

        ShapelessRecipeBuilder.shapeless(MISC, Items.YELLOW_DYE)
                .requires(Ingredient.of(AloofItems.SILPHIUM_FLOWER))
                .group("silphium")
                .unlockedBy("has_silphium", has(AloofItems.SILPHIUM_FLOWER)).save(output, Aloof.location(getItemName(Items.YELLOW_DYE) + "_from_" + getItemName(AloofItems.SILPHIUM_FLOWER)));
        ShapelessRecipeBuilder.shapeless(TOOLS, AloofItems.HEALING_SALVE)
                .requires(Items.BOWL)
                .requires(AloofItems.SILPHIUM_FLOWER, 3)
                .requires(Items.HONEY_BOTTLE)
                .requires(Items.GUNPOWDER, 2)
                .group("silphium")
                .unlockedBy("has_silphium", has(AloofItems.SILPHIUM_FLOWER)).save(output.withConditions(new NotCondition(new ModLoadedCondition(AloofConstants.SUPPLEMENTARIES))));
        ShapelessRecipeBuilder.shapeless(TOOLS, AloofItems.HEALING_SALVE)
                .requires(Items.BOWL)
                .requires(AloofItems.SILPHIUM_FLOWER, 3)
                .requires(Items.HONEY_BOTTLE)
                .requires(Ingredient.of(AloofItemTags.ASH), 2)
                .group("silphium")
                .unlockedBy("has_silphium", has(AloofItems.SILPHIUM_FLOWER)).save(output.withConditions(new ModLoadedCondition(AloofConstants.SUPPLEMENTARIES)), Aloof.location(getItemName(AloofItems.HEALING_SALVE) + "_alt"));
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(AloofItems.SILPHIUM_FLOWER), KNIVES, Items.YELLOW_DYE, 2)
                .addResultWithChance(AloofItems.SILPHIUM_SEEDS, 0.2F)
                .build(output.withConditions(new ModLoadedCondition(AloofConstants.FARMERS_DELIGHT)), Aloof.location("cutting/" + getItemName(AloofItems.SILPHIUM_FLOWER)));
        ShapedRecipeBuilder.shaped(REDSTONE, AloofBlocks.HEALING_BOMB)
                .define('S', AloofItems.SILPHIUM_FLOWER)
                .define('G', Tags.Items.GUNPOWDERS)
                .pattern("GSG").pattern("SGS").pattern("GSG")
                .unlockedBy("has_silphium", has(AloofItems.SILPHIUM_FLOWER)).save(output);
        ShapelessRecipeBuilder.shapeless(REDSTONE, AloofItems.HEALING_BOMB_MINECART)
                .requires(AloofBlocks.HEALING_BOMB)
                .requires(Items.MINECART)
                .unlockedBy("has_healing_bomb", has(AloofBlocks.HEALING_BOMB)).save(output);

        ShapelessRecipeBuilder.shapeless(MISC, Items.ORANGE_DYE)
                .requires(Ingredient.of(AloofBlocks.TORCHFLOWER))
                .group("torchflower")
                .unlockedBy("has_torchflower", has(AloofBlocks.TORCHFLOWER)).save(output, Aloof.location(getItemName(Items.ORANGE_DYE) + "_from_" + getItemName(AloofBlocks.TORCHFLOWER)));
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(AloofBlocks.TORCHFLOWER), KNIVES, Items.ORANGE_DYE, 2)
                .addResultWithChance(AloofItems.TORCHFLOWER_SEEDS, 0.2F)
                .build(output.withConditions(new ModLoadedCondition(AloofConstants.FARMERS_DELIGHT)), Aloof.location("cutting/" + getItemName(AloofBlocks.TORCHFLOWER)));

        this.storageRecipes(output, MISC, AloofItems.SILPHIUM_FLOWER, MISC, AloofBlocks.SILPHIUM_BUNDLE);

        ShapedRecipeBuilder.shaped(MISC, AloofBlocks.PITCHER_ROOT_BUNDLE)
                .define('R', AloofBlocks.PITCHER_ROOT)
                .pattern("RR").pattern("RR")
                .unlockedBy("has_root", has(AloofBlocks.PITCHER_ROOT)).save(output);
        ShapelessRecipeBuilder.shapeless(MISC, AloofBlocks.PITCHER_ROOT, 4)
                .requires(AloofBlocks.PITCHER_ROOT_BUNDLE)
                .unlockedBy("has_root", has(AloofBlocks.PITCHER_ROOT)).save(output);
    }

    private static Ingredient matchesTool(ItemAbility toolAction, TagKey<Item> fallbackTag) {
        return CompoundIngredient.of(new ItemAbilityIngredient(toolAction).toVanilla(), Ingredient.of(fallbackTag));
    }
}
