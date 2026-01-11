package silly.chemthunder.rinvenium.index;

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
import silly.chemthunder.rinvenium.item.AscentaItem;
import silly.chemthunder.rinvenium.item.EnviniumSpearItem;
import silly.chemthunder.rinvenium.item.EvicruxItem;
import silly.chemthunder.rinvenium.item.HotGItem;

import java.util.LinkedHashMap;
import java.util.Map;

public interface RinveniumItems {
    Map<Item, Identifier> ITEMS = new LinkedHashMap<>();

    //  Item WEAPON_RACK = create("weapon_rack", new WeaponRackItem(new Item.Settings()));

    Item ENVINIUM_SPEAR = create("envinium_spear", new EnviniumSpearItem(ToolMaterials.NETHERITE, 5, -2.4f, new Item.Settings()
            .maxCount(1)
    ));

    Item ASCENTA_DE_RIVULETA = create("ascenta_de_rivuleta", new AscentaItem(new Item.Settings()
            .maxCount(1)
    ));

    Item DESCENTO_ASTRUM_EVICRUX = create("descento_astrum_evicrux", new EvicruxItem(new Item.Settings()
            .maxCount(1)
    ));

    Item HAIL_OF_THE_GODS = create("hail_of_the_gods", new HotGItem(new Item.Settings()
            .maxCount(1)
    ));

    // Descento Astrum Evicrux

    static <T extends Item> T create(String name, T item) {
        ITEMS.put(item, Rinvenium.id(name));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(RinveniumItems::addCombatEntries);
        return item;
    }


    private static void addCombatEntries(FabricItemGroupEntries fabricItemGroupEntries) {
        fabricItemGroupEntries.addAfter(Items.TOTEM_OF_UNDYING, ENVINIUM_SPEAR);
        fabricItemGroupEntries.addAfter(ENVINIUM_SPEAR, ASCENTA_DE_RIVULETA);
        fabricItemGroupEntries.addAfter(ASCENTA_DE_RIVULETA, DESCENTO_ASTRUM_EVICRUX);
    }

    static void index() {
        ITEMS.forEach((item, id) -> Registry.register(Registries.ITEM, id, item));
    }
}
