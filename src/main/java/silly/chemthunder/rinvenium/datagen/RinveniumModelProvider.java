package silly.chemthunder.rinvenium.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import net.minecraft.item.Items;
import silly.chemthunder.rinvenium.index.RinveniumItems;

public class RinveniumModelProvider extends FabricModelProvider {
    public RinveniumModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {

    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(RinveniumItems.DEBUGGER, Items.STICK, Models.HANDHELD);
    }
}
