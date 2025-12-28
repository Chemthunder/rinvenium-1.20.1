package silly.chemthunder.rinvenium.index;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import silly.chemthunder.rinvenium.Rinvenium;
import silly.chemthunder.rinvenium.enchant.RushEnchantment;

import java.util.LinkedHashMap;
import java.util.Map;

public interface RinveniumEnchantments {
    Map<Enchantment, Identifier> ENCHANTMENTS = new LinkedHashMap<>();

    Enchantment RUSH = createEnchantment("rush", new RushEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND));

    private static Enchantment createEnchantment(String name, Enchantment enchantment) {
        ENCHANTMENTS.put(enchantment, new Identifier(Rinvenium.MOD_ID, name));
        return enchantment;
    }

    static void index() {
        ENCHANTMENTS.keySet().forEach(enchantment -> Registry.register(Registries.ENCHANTMENT, ENCHANTMENTS.get(enchantment), enchantment));
    }
}
