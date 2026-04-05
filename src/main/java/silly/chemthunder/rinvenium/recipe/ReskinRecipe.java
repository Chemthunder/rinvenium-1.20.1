package silly.chemthunder.rinvenium.recipe;

import com.google.gson.JsonObject;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import silly.chemthunder.rinvenium.index.RinveniumRecipes;

public class ReskinRecipe implements Recipe<Inventory> {
    protected final Ingredient input;
    protected final ItemStack output;
    protected final Identifier id;
    protected final String group;

    public ReskinRecipe(Ingredient input, ItemStack output, Identifier id, String group) {
        this.input = input;
        this.output = output;
        this.id = id;
        this.group = group;
    }

    @Override
    public boolean matches(Inventory inventory, World world) {
        return this.input.test(inventory.getStack(0));
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(Blocks.SMITHING_TABLE);
    }

    @Override
    public RecipeType<?> getType() {
        return RinveniumRecipes.RESKIN_TYPE;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RinveniumRecipes.RESKIN_SERIALIZER;
    }

    @Override
    public Identifier getId() {
        return this.id;
    }

    @Override
    public String getGroup() {
        return this.group;
    }

    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        return this.output;
    }

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        DefaultedList<Ingredient> defaultedList = DefaultedList.of();
        defaultedList.add(this.input);
        return defaultedList;
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack craft(Inventory inventory, DynamicRegistryManager registryManager) {
        return this.output.copy();
    }

    public static class Serializer implements RecipeSerializer<ReskinRecipe> {

        public ReskinRecipe read(Identifier identifier, JsonObject jsonObject) {
            String string = JsonHelper.getString(jsonObject, "group", "");
            Ingredient ingredient;
            if (JsonHelper.hasArray(jsonObject, "ingredient")) {
                ingredient = Ingredient.fromJson(JsonHelper.getArray(jsonObject, "ingredient"), false);
            } else {
                ingredient = Ingredient.fromJson(JsonHelper.getObject(jsonObject, "ingredient"), false);
            }

            String string2 = JsonHelper.getString(jsonObject, "result");
            int i = JsonHelper.getInt(jsonObject, "count");
            ItemStack itemStack = new ItemStack(Registries.ITEM.get(new Identifier(string2)), i);
            return new ReskinRecipe(ingredient, itemStack, identifier, string);
        }

        public ReskinRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
            String string = packetByteBuf.readString();
            Ingredient ingredient = Ingredient.fromPacket(packetByteBuf);
            ItemStack itemStack = packetByteBuf.readItemStack();
            return new ReskinRecipe(ingredient, itemStack, identifier, string);
        }

        public void write(PacketByteBuf packetByteBuf, ReskinRecipe reskinRecipe) {
            packetByteBuf.writeString(reskinRecipe.group);
            reskinRecipe.input.write(packetByteBuf);
            packetByteBuf.writeItemStack(reskinRecipe.output);
        }
    }
}
