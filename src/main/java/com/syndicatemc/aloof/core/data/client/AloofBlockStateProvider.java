package com.syndicatemc.aloof.core.data.client;

import com.syndicatemc.aloof.common.block.NewTorchflowerBlock;
import com.syndicatemc.aloof.common.block.PitcherRootBlock;
import com.syndicatemc.aloof.common.block.PumiceBricksBlock;
import com.syndicatemc.aloof.common.block.SilphiumCropBlock;
import com.syndicatemc.aloof.core.Aloof;
import com.teamabnormals.blueprint.core.data.client.BlueprintBlockStateProvider;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

import static com.syndicatemc.aloof.core.registry.AloofBlocks.*;

public class AloofBlockStateProvider extends BlueprintBlockStateProvider {
    public AloofBlockStateProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, Aloof.MOD_ID, helper);
    }

    @Override
    protected void registerStatesAndModels() {
        loofahGourdBlock(LOOFAH_GOURD);

        for (DeferredBlock<?> block : new DeferredBlock[]{
                PUMICE, SMOOTH_PUMICE, POLISHED_PUMICE
        }) {
            this.block(block);
        }
        for (DeferredBlock<?> block : new DeferredBlock[]{
                LOOFAH_SPONGE, WET_LOOFAH_SPONGE,
                SOAKED_PUMICE
        }) {
            this.cubeColumnBlock(block);
        }

        loofahCropBlock(LOOFAH_CROP);
        silphiumCropBlock(SILPHIUM_CROP);
        this.directionalBlock(SILPHIUM_BUNDLE);
        torchflowerBlock(TORCHFLOWER);
        torchflowerCropBlock(TORCHFLOWER_CROP);
        this.simpleBlock(POTTED_TORCHFLOWER.get(), models().singleTexture(name(POTTED_TORCHFLOWER.get()), ResourceLocation.withDefaultNamespace("block/flower_pot_cross"), "plant", Aloof.location("block/torchflower")));
        pitcherRootBlock(PITCHER_ROOT);
        this.directionalBlock(PITCHER_ROOT_BUNDLE);

        this.cubeBottomTopBlock(HEALING_BOMB);

        pumiceBrickBlock(PUMICE_BRICKS);
    }

    private void loofahCropBlock(DeferredBlock<?> loofahObject) {
        Block loofah = loofahObject.get();

        this.getVariantBuilder(loofah).forAllStates(state -> {
            boolean isUpper = state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER;
            int age = state.getValue(BlockStateProperties.AGE_7);
            ConfiguredModel.Builder<?> builder = ConfiguredModel.builder().modelFile(isUpper && age > 3 ? loofahCropModel(age, true) : loofahCropModel(age, false));

            return builder.build();
        });
    }

    private ModelFile loofahCropModel(int stage, boolean isUpper) {
        return isUpper ?
                this.models().cross(Aloof.MOD_ID + ":loofah_crop_stage" + stage + "_top", Aloof.location("block/loofah_crop_stage" + stage + "_top")) :
                this.models().cross(Aloof.MOD_ID + ":loofah_crop_stage" + stage, Aloof.location("block/loofah_crop_stage" + stage));
    }

    private void loofahGourdBlock(DeferredBlock<?> loofahObject) {
        Block loofah = loofahObject.get();
        ModelFile baseModel = this.models().cubeBottomTop(name(loofah), Aloof.location("block/loofah_gourd_side"), Aloof.location("block/loofah_gourd_bottom"), Aloof.location("block/loofah_gourd_top"));
        ModelFile topModel = this.models().cubeBottomTop(name(loofah) + "top", Aloof.location("block/loofah_gourd_side_top"), Aloof.location("block/loofah_gourd_inside"), Aloof.location("block/loofah_gourd_top"));
        ModelFile bottomModel = this.models().cubeBottomTop(name(loofah) + "bottom", Aloof.location("block/loofah_gourd_side_bottom"), Aloof.location("block/loofah_gourd_bottom"), Aloof.location("block/loofah_gourd_inside"));
        this.getVariantBuilder(loofah).forAllStates(state -> {
            boolean isTop = state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER;
            ConfiguredModel.Builder<?> builder = ConfiguredModel.builder().modelFile(isTop ?
                    topModel :
                    bottomModel);
            return builder.build();
        });
        this.simpleBlockItem(loofah, baseModel);
    }

    private void silphiumCropBlock(DeferredBlock<?> silphumObject) {
        Block silphium = silphumObject.get();

        this.getVariantBuilder(silphium).forAllStates(state -> {
            int section = state.getValue(SilphiumCropBlock.SECTION);
            int age = state.getValue(SilphiumCropBlock.AGE);
            ModelFile section1 = silphiumCropModel(Math.min(age, 3), 1);
            ModelFile section2 = silphiumCropModel(Math.max(Math.min(age, 6), 3), 2);
            ModelFile section3 = silphiumCropModel(Math.max(age, 5), 3);
            ModelFile section4 = silphiumCropModel(Math.max(age, 8), 4);

            ConfiguredModel.Builder<?> builder = ConfiguredModel.builder().modelFile(switch (section) {
                case 1 -> section1;
                case 2 -> section2;
                case 3 -> section3;
                default -> section4;
            });

            return builder.build();
        });
    }

    private ModelFile silphiumCropModel(int stage, int section) {
        return this.models().withExistingParent(name(SILPHIUM_CROP.get()) + "_stage" + stage + "_section" + section, Aloof.location("block/cross_crop")).texture("cross", Aloof.location("block/" + name(SILPHIUM_CROP.get()) + "_stage" + stage + "_section" + section));
    }

    private void torchflowerBlock(DeferredBlock<?> torchflowerObject) {
        Block torchflower = torchflowerObject.get();

        this.getVariantBuilder(torchflower).forAllStates(state -> {
            boolean onFarmland = state.getValue(NewTorchflowerBlock.ON_FARMLAND);
            boolean isSoul = state.getValue(NewTorchflowerBlock.SOUL);

            ModelFile model = onFarmland ?
                    this.models().withExistingParent((isSoul ? "soul_" : "") + name(torchflower) + "_crop", Aloof.location("block/cross_crop")).texture("cross", Aloof.location( "block/" + (isSoul ? "soul_" : "") + name(torchflower))) :
                    this.models().cross((isSoul ? "soul_" : "") + name(torchflower), Aloof.location("block/" + (isSoul ? "soul_" : "") + name(torchflower)));

            ConfiguredModel.Builder<?> builder = ConfiguredModel.builder().modelFile(model);

            return builder.build();
        });

        this.generatedItem(torchflower, "block");
    }

    private void torchflowerCropBlock(DeferredBlock<?> torchflowerObject) {
        Block torchflower = torchflowerObject.get();

        this.getVariantBuilder(torchflower).forAllStates(state -> {
            int age = state.getValue(BlockStateProperties.AGE_3);
            boolean isSoul = state.getValue(NewTorchflowerBlock.SOUL);

            String name = (isSoul && age > 1 ? "soul_" : "") + name(torchflower) + "_stage" + age;

            ModelFile model = this.models().withExistingParent(name, Aloof.location("block/cross_crop")).texture("cross", Aloof.location( "block/" + name));

            ConfiguredModel.Builder<?> builder = ConfiguredModel.builder().modelFile(model);

            return builder.build();
        });
    }

    private void pitcherRootBlock(DeferredBlock<?> pitcherObject) {
        Block pitcher = pitcherObject.get();

        this.getVariantBuilder(pitcher).forAllStates(state -> {
            int eaten = state.getValue(PitcherRootBlock.EATEN);
            Direction facing = state.getValue(BlockStateProperties.FACING);

            ModelFile model = facing == Direction.UP ?
                    new ModelFile.ExistingModelFile(Aloof.location("block/pitcher_root_eaten" + eaten), this.models().existingFileHelper) :
                    new ModelFile.ExistingModelFile(Aloof.location("block/pitcher_root_side_eaten" + eaten), this.models().existingFileHelper);

            ConfiguredModel.Builder<?> builder = ConfiguredModel.builder().modelFile(model);
            switch (facing) {
                case SOUTH -> builder.rotationY(180);
                case EAST -> builder.rotationY(90);
                case WEST -> builder.rotationY(270);
                default -> builder.rotationY(0);
            }
            return builder.build();
        });

        this.generatedItem(pitcher, "item");
    }

    private void pumiceBrickBlock(DeferredBlock<?> object) {
        Block block = object.get();
        ModelFile baseModel = pumiceBrickModel(0, false);
        ModelFile soaked0Model = pumiceBrickModel(0, true);
        ModelFile soaked1Model = pumiceBrickModel(1, true);
        ModelFile soaked2Model = pumiceBrickModel(2, true);

        this.getVariantBuilder(block).forAllStates(state -> {
            int saturated = state.getValue(PumiceBricksBlock.SATURATED);
            ConfiguredModel.Builder<?> builder = ConfiguredModel.builder().modelFile(switch (saturated) {
                case 3 -> soaked2Model;
                case 2 -> soaked1Model;
                case 1 -> soaked0Model;
                default -> baseModel;
            });
            return builder.build();
        });
        this.simpleBlockItem(block, baseModel);
    }

    private ModelFile pumiceBrickModel(int stage, boolean isSaturated) {
        return isSaturated ?
                this.models().cubeAll(Aloof.MOD_ID + ":soaked_pumice_bricks_stage" + stage, Aloof.location("block/soaked_pumice_bricks_stage" + stage)) :
                this.models().cubeAll(Aloof.MOD_ID + ":pumice_bricks", Aloof.location("block/pumice_bricks"));
    }
}
