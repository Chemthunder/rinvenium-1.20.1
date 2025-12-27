package silly.chemthunder.rinvenium.cca.item;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.item.ItemComponent;
import net.minecraft.item.ItemStack;
import silly.chemthunder.rinvenium.Rinvenium;

public class EnviniumSpearItemComponent extends ItemComponent {
    public static final ComponentKey<EnviniumSpearItemComponent> KEY = ComponentRegistry.getOrCreate(Rinvenium.id("spear"), EnviniumSpearItemComponent.class);

    private static final String SPEAR = "spear";
    public EnviniumSpearItemComponent(ItemStack stack) {
        super(stack);
    }

    public int getCharge() {
        return this.getInt(SPEAR);
    }

    public void setCharge(int type) {
        this.putInt(SPEAR, type);
    }
}
