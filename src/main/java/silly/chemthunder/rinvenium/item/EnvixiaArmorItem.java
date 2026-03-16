package silly.chemthunder.rinvenium.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import silly.chemthunder.rinvenium.index.RinveniumItems;

public class EnvixiaArmorItem extends ArmorItem {
    public EnvixiaArmorItem(ArmorMaterial material, Type type, Settings settings) {
        super(material, type, settings);
    }

    public static boolean hasFullSuit(PlayerEntity player) {
        return player.getInventory().getArmorStack(0).isOf(RinveniumItems.ENVIXIA_BOOTS)
                && player.getInventory().getArmorStack(1).isOf(RinveniumItems.ENVIXIA_LEGGINGS)
                && player.getInventory().getArmorStack(2).isOf(RinveniumItems.ENVIXIA_CHESTPLATE)
                && player.getInventory().getArmorStack(3).isOf(RinveniumItems.ENVIXIA_HELMET);
    }
}
