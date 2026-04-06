package silly.chemthunder.rinvenium.index;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;
import silly.chemthunder.rinvenium.Rinvenium;

public class RinveniumFoodComponents {
    public static final FoodComponent BATTERY = new FoodComponent.Builder().hunger(4).saturationModifier(0.6f).build();
    public static final FoodComponent ION_CELL = new FoodComponent.Builder().hunger(8).saturationModifier(1.2f).statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 140, 0), 1.0f).alwaysEdible().build();

    public static void initRinveniumFoodComponents() {
        Rinvenium.LOGGER.info("Initializing Rinvenium Food Components");
    }
}