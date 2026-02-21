package silly.chemthunder.rinvenium.index;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import silly.chemthunder.rinvenium.Rinvenium;
import silly.chemthunder.rinvenium.item.DebuggerItem;
import silly.chemthunder.rinvenium.item.EnviniumSpearItem;
import silly.chemthunder.rinvenium.item.HotGItem;
import silly.chemthunder.rinvenium.item.tool.EnviniumToolMaterial;

import java.util.LinkedHashMap;
import java.util.Map;

public interface RinveniumItems {
    Map<Item, Identifier> ITEMS = new LinkedHashMap<>();

    Item EMPTY = create("empty", new Item(new FabricItemSettings()));
    Item DEBUGGER = create("debugger", new DebuggerItem(new Item.Settings().maxCount(1).fireproof()));
    Item ENVINIUM_SPEAR = create("envinium_spear", new EnviniumSpearItem(EnviniumToolMaterial.ENVINIUM, 5, -2.4f, new Item.Settings().maxCount(1)));
    Item HAIL_OF_THE_GODS = create("hail_of_the_gods", new HotGItem(new FabricItemSettings().maxCount(1)));

    static <T extends Item> T create(String name, T item) {
        ITEMS.put(item, Rinvenium.id(name));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(RinveniumItems::addCombatEntries);
        return item;
    }

    private static void addCombatEntries(FabricItemGroupEntries fabricItemGroupEntries) {
        fabricItemGroupEntries.addAfter(Items.TOTEM_OF_UNDYING, ENVINIUM_SPEAR, HAIL_OF_THE_GODS);
    }

    static void init() {
        ITEMS.forEach((item, id) -> Registry.register(Registries.ITEM, id, item));
    }
}
