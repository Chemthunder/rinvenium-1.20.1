package silly.chemthunder.rinvenium.index;

import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.item.Item;

import java.util.Map;

public interface RinveniumCauldronBehavior {

    Map<Item, CauldronBehavior> AURIO_CAULDRON_BEHAVIOR = CauldronBehavior.createMap();
    Map<Item, CauldronBehavior> ENVINIA_CAULDRON_BEHAVIOR = CauldronBehavior.createMap();
}
