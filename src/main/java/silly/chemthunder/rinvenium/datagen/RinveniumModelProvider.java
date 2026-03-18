package silly.chemthunder.rinvenium.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import net.minecraft.item.Items;
import silly.chemthunder.rinvenium.index.RinveniumItems;

import static net.minecraft.data.client.Models.*;
import static silly.chemthunder.rinvenium.index.RinveniumItems.*;

public class RinveniumModelProvider extends FabricModelProvider {
    public RinveniumModelProvider(FabricDataOutput output) {
        super(output);
    }

    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {

    }

    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(DEBUGGER, Items.STICK, HANDHELD);

        itemModelGenerator.register(AURIO_INGOT, GENERATED);
        itemModelGenerator.register(ENVINIA_INGOT, GENERATED);
        itemModelGenerator.register(ENVIXIUS_INGOT, GENERATED);
        itemModelGenerator.register(ENVIXIUS_PLATE, GENERATED);

        itemModelGenerator.register(SUPERHEATED_AURIO_INGOT, GENERATED);
        itemModelGenerator.register(SUPERHEATED_ENVINIA_INGOT, GENERATED);
        itemModelGenerator.register(SUPERHEATED_ENVIXIUS_INGOT, GENERATED);
        itemModelGenerator.register(SUPERHEATED_ENVIXIUS_PLATE, GENERATED);
        
        itemModelGenerator.register(BATTERY, GENERATED);
        itemModelGenerator.register(ION_CELL, GENERATED);

        //itemModelGenerator.register(ENVIXIA_CORE, GENERATED);
        itemModelGenerator.register(ENVIXIA_HELMET, GENERATED);
        itemModelGenerator.register(ENVIXIA_CHESTPLATE, GENERATED);
        itemModelGenerator.register(ENVIXIA_LEGGINGS, GENERATED);
        itemModelGenerator.register(ENVIXIA_BOOTS, GENERATED);


        // for (Item value : RinveniumItems.ITEMS) {
        //     if (value != HAIL_OF_THE_GODS && value != DEBUGGER) {
        //             itemModelGenerator.register(value, Models.GENERATED);
        //     }
        // }
        // maybe quick thing to optimize this idfk
    }
}
