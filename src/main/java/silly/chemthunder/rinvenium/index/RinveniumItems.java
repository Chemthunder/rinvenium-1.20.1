package silly.chemthunder.rinvenium.index;

import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import silly.chemthunder.rinvenium.Rinvenium;
import silly.chemthunder.rinvenium.item.EnviniumSpearItem;

import java.util.LinkedHashMap;
import java.util.Map;

public interface RinveniumItems {
    Map<Item, Identifier> ITEMS = new LinkedHashMap<>();

    //  Item WEAPON_RACK = create("weapon_rack", new WeaponRackItem(new Item.Settings()));

    Item ENVINIUM_SPEAR = create("envinium_spear", new EnviniumSpearItem(ToolMaterials.NETHERITE, 5, -2.4f, new Item.Settings()
            .maxCount(1)
    ));

    static <T extends Item> T create(String name, T item) {
        ITEMS.put(item, Rinvenium.id(name));
        return item;
    }

    static void index() {
        ITEMS.forEach((item, id) -> Registry.register(Registries.ITEM, id, item));
    }
}
