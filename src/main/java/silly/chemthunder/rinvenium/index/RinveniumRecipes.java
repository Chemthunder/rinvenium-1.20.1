package silly.chemthunder.rinvenium.index;

import net.minecraft.recipe.CuttingRecipe;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import silly.chemthunder.rinvenium.Rinvenium;
import silly.chemthunder.rinvenium.recipe.ReskinRecipe;

public class RinveniumRecipes {
    /** Recipe Types */
    public static final RecipeType<ReskinRecipe> RESKIN_TYPE = registerType("reskin");

    /** Recipe Serializers */
    public static final RecipeSerializer<ReskinRecipe> RESKIN_SERIALIZER = registerSerializer("reskin", new ReskinRecipe.Serializer());


    private static <T extends Recipe<?>> RecipeType<T> registerType(String name) {
        return Registry.register(Registries.RECIPE_TYPE, Rinvenium.id(name), new RecipeType<T>() {
            public String toString() {
                return name;
            }
        });
    }

    private static <S extends RecipeSerializer<T>, T extends Recipe<?>> S registerSerializer(String name, S serializer) {
        return Registry.register(Registries.RECIPE_SERIALIZER, Rinvenium.id(name), serializer);
    }


    public static void registerRinveniumRecipes() {
        Rinvenium.LOGGER.info("Registering Rinvenium Recipes");
    }
}
