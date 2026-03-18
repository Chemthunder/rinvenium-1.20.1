package silly.chemthunder.rinvenium.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import silly.chemthunder.rinvenium.datagen.RinveniumItemTagProvider;
import silly.chemthunder.rinvenium.util.RinveniumUuids;

@Mixin(CraftingScreenHandler.class)
public abstract class CraftingScreenHandlerMixin extends AbstractRecipeScreenHandler<RecipeInputInventory> {
    public CraftingScreenHandlerMixin(ScreenHandlerType<?> screenHandlerType, int i) {
        super(screenHandlerType, i);
    }

    @ModifyExpressionValue(method = "updateResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/inventory/CraftingResultInventory;shouldCraftRecipe(Lnet/minecraft/world/World;Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/recipe/Recipe;)Z"))
    private static boolean rinvenium$lockCraftToRiva(boolean original, @Local ServerPlayerEntity serverPlayerEntity, @Local CraftingRecipe craftingRecipe, @Local(argsOnly = true) RecipeInputInventory craftingInventory, @Local(argsOnly = true) World world) {
        ItemStack itemStack = craftingRecipe.craft(craftingInventory, world.getRegistryManager());
        if (itemStack.isIn(RinveniumItemTagProvider.LOCKED_RECIPES)) {
            return original && RinveniumUuids.canCraftEnvixia(serverPlayerEntity);
        } else {
            return original;
        }
    }
}
