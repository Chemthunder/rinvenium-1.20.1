package silly.chemthunder.rinvenium.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import org.jetbrains.annotations.Nullable;
import silly.chemthunder.rinvenium.Rinvenium;
import silly.chemthunder.rinvenium.index.RinveniumItems;

import java.util.concurrent.CompletableFuture;

public class RinveniumItemTagProvider extends FabricTagProvider.ItemTagProvider {
    public static TagKey<Item> BIG_ITEM_RENDERING = TagKey.of(RegistryKeys.ITEM, Rinvenium.id("big_item_renderer"));

    public RinveniumItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        this.getOrCreateTagBuilder(BIG_ITEM_RENDERING)
                .add(
                        RinveniumItems.ENVINIUM_SPEAR
                );
    }
}
