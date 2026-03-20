package silly.chemthunder.rinvenium.index;

import net.minecraft.item.FoodComponent;
import silly.chemthunder.rinvenium.Rinvenium;

public class RinveniumFoodComponents {
    public static final FoodComponent BATTERY = new FoodComponent.Builder().hunger(4).saturationModifier(0.6f).build();
    public static final FoodComponent ION_CELL = new FoodComponent.Builder().hunger(8).saturationModifier(1.2f).build();

    public static void initRinveniumFoodComponents() {
        Rinvenium.LOGGER.info("Initializing Rinvenium Food Components");
    }
}
