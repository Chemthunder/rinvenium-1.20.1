package silly.chemthunder.rinvenium.index;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import silly.chemthunder.rinvenium.Rinvenium;
import silly.chemthunder.rinvenium.item.*;
import silly.chemthunder.rinvenium.item.tool.EnviniumToolMaterial;

import java.util.LinkedHashMap;
import java.util.Map;

public interface RinveniumItems {
    Map<Item, Identifier> ITEMS = new LinkedHashMap<>();

    Item EMPTY = create("empty", new Item(new FabricItemSettings()));
    Item DEBUGGER = create("debugger", new DebuggerItem(new Item.Settings().maxCount(1).fireproof()));
    Item ENVINIUM_SPEAR = create("envinium_spear", new EnviniumSpearItem(EnviniumToolMaterial.ENVINIUM, 5, -2.4f, new Item.Settings().maxCount(1)));
    Item HAIL_OF_THE_GODS = create("hail_of_the_gods", new HotGItem(new FabricItemSettings().maxCount(1)));

    Item AURIO_INGOT = create("aurio_ingot", new Item(new FabricItemSettings()));
    Item ENVINIA_INGOT = create("envinia_ingot", new Item(new FabricItemSettings()));
    Item ENVIXIUS_INGOT = create("envixius_ingot", new Item(new FabricItemSettings().fireproof()));
    Item ENVIXIUS_PLATE = create("envixius_plate", new Item(new FabricItemSettings().fireproof()));
    Item SUPERHEATED_AURIO_INGOT = create("superheated_aurio_ingot", new HotItem(new FabricItemSettings(), AURIO_INGOT));
    Item SUPERHEATED_ENVINIA_INGOT = create("superheated_envinia_ingot", new HotItem(new FabricItemSettings(), ENVINIA_INGOT));
    Item SUPERHEATED_ENVIXIUS_INGOT = create("superheated_envixius_ingot", new HotItem(new FabricItemSettings().fireproof(), ENVIXIUS_INGOT));
    Item SUPERHEATED_ENVIXIUS_PLATE = create("superheated_envixius_plate", new HotItem(new FabricItemSettings().fireproof(), ENVIXIUS_PLATE));
    Item BATTERY = create("battery", new Item(new FabricItemSettings()));
    Item ION_CELL = create("ion_cell", new Item(new FabricItemSettings()));

    Item ENVIXIA_CORE = create("envixia_core", new EnvixiaCoreItem(new FabricItemSettings().maxCount(1).fireproof()));
    Item ENVIXIA_HELMET = create("envixia_helmet", new ArmorItem(RinveniumArmorMaterials.ENIVIXIA, ArmorItem.Type.HELMET, new FabricItemSettings().maxCount(1).fireproof()));
    Item ENVIXIA_CHESTPLATE = create("envixia_chestplate", new ArmorItem(RinveniumArmorMaterials.ENIVIXIA, ArmorItem.Type.CHESTPLATE, new FabricItemSettings().maxCount(1).fireproof()));
    Item ENVIXIA_LEGGINGS = create("envixia_leggings", new ArmorItem(RinveniumArmorMaterials.ENIVIXIA, ArmorItem.Type.LEGGINGS, new FabricItemSettings().maxCount(1).fireproof()));
    Item ENVIXIA_BOOTS = create("envixia_boots", new ArmorItem(RinveniumArmorMaterials.ENIVIXIA, ArmorItem.Type.BOOTS, new FabricItemSettings().maxCount(1).fireproof()));

    static <T extends Item> T create(String name, T item) {
        ITEMS.put(item, Rinvenium.id(name));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(RinveniumItems::addCombatEntries);
        return item;
    }

    private static void addCombatEntries(FabricItemGroupEntries fabricItemGroupEntries) {
        fabricItemGroupEntries.addAfter(Items.TOTEM_OF_UNDYING,
                ENVINIUM_SPEAR,
                HAIL_OF_THE_GODS,
                ENVIXIA_HELMET,
                ENVIXIA_CHESTPLATE,
                ENVIXIA_LEGGINGS,
                ENVIXIA_BOOTS
        );
    }

    static void init() {
        ITEMS.forEach((item, id) -> Registry.register(Registries.ITEM, id, item));
    }
}
