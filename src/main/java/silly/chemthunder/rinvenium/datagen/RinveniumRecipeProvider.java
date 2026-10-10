package silly.chemthunder.rinvenium.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.CookingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.util.Identifier;
import silly.chemthunder.rinvenium.index.RinveniumItems;

import java.util.function.Consumer;

public class RinveniumRecipeProvider extends FabricRecipeProvider {
    public RinveniumRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    public void generate(Consumer<RecipeJsonProvider> recipeExporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, RinveniumItems.ENVIXIA_HELMET, 1)
                .pattern("P P")
                .pattern("PIP")
                .input('P', RinveniumItems.ENVIXIUS_PLATE)
                .input('I', RinveniumItems.ION_CELL)
                .criterion(hasItem(RinveniumItems.ENVIXIUS_PLATE), conditionsFromItem(RinveniumItems.ENVIXIUS_PLATE))
                .criterion(hasItem(RinveniumItems.ION_CELL), conditionsFromItem(RinveniumItems.ION_CELL))
                .offerTo(recipeExporter, new Identifier(getRecipeName(RinveniumItems.ENVIXIA_HELMET)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, RinveniumItems.ENVIXIA_CHESTPLATE, 1)
                .pattern("P P")
                .pattern("XIX")
                .pattern("PPP")
                .input('P', RinveniumItems.ENVIXIUS_PLATE)
                .input('I', RinveniumItems.ION_CELL)
                .input('X', RinveniumItems.ENVINIA_INGOT)
                .criterion(hasItem(RinveniumItems.ENVIXIUS_PLATE), conditionsFromItem(RinveniumItems.ENVIXIUS_PLATE))
                .criterion(hasItem(RinveniumItems.ION_CELL), conditionsFromItem(RinveniumItems.ION_CELL))
                .criterion(hasItem(RinveniumItems.ENVINIA_INGOT), conditionsFromItem(RinveniumItems.ENVINIA_INGOT))
                .offerTo(recipeExporter, new Identifier(getRecipeName(RinveniumItems.ENVIXIA_CHESTPLATE)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, RinveniumItems.ENVIXIA_LEGGINGS, 1)
                .pattern("PPP")
                .pattern("X X")
                .pattern("P P")
                .input('P', RinveniumItems.ENVIXIUS_PLATE)
                .input('X', RinveniumItems.ENVINIA_INGOT)
                .criterion(hasItem(RinveniumItems.ENVIXIUS_PLATE), conditionsFromItem(RinveniumItems.ENVIXIUS_PLATE))
                .criterion(hasItem(RinveniumItems.ENVINIA_INGOT), conditionsFromItem(RinveniumItems.ENVINIA_INGOT))
                .offerTo(recipeExporter, new Identifier(getRecipeName(RinveniumItems.ENVIXIA_LEGGINGS)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, RinveniumItems.ENVIXIA_BOOTS, 1)
                .pattern("P P")
                .pattern("P P")
                .input('P', RinveniumItems.ENVIXIUS_PLATE)
                .criterion(hasItem(RinveniumItems.ENVIXIUS_PLATE), conditionsFromItem(RinveniumItems.ENVIXIUS_PLATE))
                .offerTo(recipeExporter, new Identifier(getRecipeName(RinveniumItems.ENVIXIA_BOOTS)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, RinveniumItems.ENVINIUM_GLAIVE, 1)
                .pattern(" CX")
                .pattern("CSX")
                .pattern("N  ")
                .input('X', RinveniumItems.ENVIXIUS_INGOT)
                .input('S', Items.NETHERITE_SWORD)
                .input('N', RinveniumItems.ENVINIA_INGOT)
                .input('C', RinveniumItems.ION_CELL)
                .criterion(hasItem(RinveniumItems.ENVIXIUS_INGOT), conditionsFromItem(RinveniumItems.ENVIXIUS_INGOT))
                .criterion(hasItem(RinveniumItems.ENVINIA_INGOT), conditionsFromItem(RinveniumItems.ENVINIA_INGOT))
                .criterion(hasItem(RinveniumItems.ION_CELL), conditionsFromItem(RinveniumItems.ION_CELL))
                .criterion(hasItem(Items.NETHERITE_SWORD), conditionsFromItem(Items.NETHERITE_SWORD))
                .offerTo(recipeExporter, new Identifier(getRecipeName(RinveniumItems.ENVINIUM_GLAIVE)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, RinveniumItems.HAIL_OF_THE_GODS, 1)
                .pattern(" PX")
                .pattern("CXP")
                .pattern("NN ")
                .input('X', RinveniumItems.ENVIXIUS_INGOT)
                .input('P', RinveniumItems.ENVIXIUS_PLATE)
                .input('N', RinveniumItems.ENVINIA_INGOT)
                .input('C', RinveniumItems.ION_CELL)
                .criterion(hasItem(RinveniumItems.ENVIXIUS_INGOT), conditionsFromItem(RinveniumItems.ENVIXIUS_INGOT))
                .criterion(hasItem(RinveniumItems.ENVIXIUS_PLATE), conditionsFromItem(RinveniumItems.ENVIXIUS_PLATE))
                .criterion(hasItem(RinveniumItems.ENVINIA_INGOT), conditionsFromItem(RinveniumItems.ENVINIA_INGOT))
                .criterion(hasItem(RinveniumItems.ION_CELL), conditionsFromItem(RinveniumItems.ION_CELL))
                .offerTo(recipeExporter, new Identifier(getRecipeName(RinveniumItems.HAIL_OF_THE_GODS)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, RinveniumItems.ENVIXIA_CORE, 1)
                .pattern(" N ")
                .pattern("XCX")
                .pattern(" N ")
                .input('X', RinveniumItems.ENVIXIUS_INGOT)
                .input('N', RinveniumItems.ENVINIA_INGOT)
                .input('C', RinveniumItems.ION_CELL)
                .criterion(hasItem(RinveniumItems.ENVIXIUS_INGOT), conditionsFromItem(RinveniumItems.ENVIXIUS_INGOT))
                .criterion(hasItem(RinveniumItems.ENVINIA_INGOT), conditionsFromItem(RinveniumItems.ENVINIA_INGOT))
                .criterion(hasItem(RinveniumItems.ION_CELL), conditionsFromItem(RinveniumItems.ION_CELL))
                .offerTo(recipeExporter, new Identifier(getRecipeName(RinveniumItems.ENVIXIA_CORE)));

        CookingRecipeJsonBuilder.createBlasting(
                Ingredient.ofItems(RinveniumItems.AURIO_INGOT),
                RecipeCategory.MISC,
                RinveniumItems.SUPERHEATED_AURIO_INGOT,
                0.1f,
                80
        ).criterion(hasItem(RinveniumItems.AURIO_INGOT), conditionsFromItem(RinveniumItems.AURIO_INGOT))
                .offerTo(recipeExporter, new Identifier(getRecipeName(RinveniumItems.SUPERHEATED_AURIO_INGOT)));

        CookingRecipeJsonBuilder.createBlasting(
                Ingredient.ofItems(RinveniumItems.ENVINIA_INGOT),
                RecipeCategory.MISC,
                RinveniumItems.SUPERHEATED_ENVINIA_INGOT,
                0.1f,
                100
        ).criterion(hasItem(RinveniumItems.ENVINIA_INGOT), conditionsFromItem(RinveniumItems.ENVINIA_INGOT))
                .offerTo(recipeExporter, new Identifier(getRecipeName(RinveniumItems.SUPERHEATED_ENVINIA_INGOT)));

        CookingRecipeJsonBuilder.createBlasting(
                Ingredient.ofItems(RinveniumItems.ENVIXIUS_INGOT),
                RecipeCategory.MISC,
                RinveniumItems.SUPERHEATED_ENVIXIUS_INGOT,
                0.1f,
                100
        ).criterion(hasItem(RinveniumItems.ENVIXIUS_INGOT), conditionsFromItem(RinveniumItems.ENVIXIUS_INGOT))
                .offerTo(recipeExporter, new Identifier(getRecipeName(RinveniumItems.SUPERHEATED_ENVIXIUS_INGOT)));

        CookingRecipeJsonBuilder.createBlasting(
                Ingredient.ofItems(RinveniumItems.ENVIXIUS_PLATE),
                RecipeCategory.MISC,
                RinveniumItems.SUPERHEATED_ENVIXIUS_PLATE,
                0.1f,
                100
        ).criterion(hasItem(RinveniumItems.ENVIXIUS_PLATE), conditionsFromItem(RinveniumItems.ENVIXIUS_PLATE))
                .offerTo(recipeExporter, new Identifier(getRecipeName(RinveniumItems.SUPERHEATED_ENVIXIUS_PLATE)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, RinveniumItems.BATTERY, 4)
                .pattern("CRI")
                .input('C', RinveniumItems.AURIO_INGOT)
                .input('R', Items.REDSTONE)
                .input('I', Items.IRON_INGOT)
                .criterion(hasItem(RinveniumItems.AURIO_INGOT), conditionsFromItem(RinveniumItems.AURIO_INGOT))
                .criterion(hasItem(Items.REDSTONE), conditionsFromItem(Items.REDSTONE))
                .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT))
                .offerTo(recipeExporter, new Identifier(getRecipeName(RinveniumItems.BATTERY)));

    }
}