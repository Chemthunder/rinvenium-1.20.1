package silly.chemthunder.rinvenium.util.inject;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public interface HungerDecrement {
    void rinvenium$eatSubtract(Item item, ItemStack stack);
    void rinvenium$subtract(int food, float saturationModifier);
}