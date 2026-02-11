package silly.chemthunder.rinvenium.cca.item;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import dev.onyxstudios.cca.api.v3.item.ItemComponent;
import net.minecraft.item.ItemStack;
import silly.chemthunder.rinvenium.Rinvenium;

public class EnviniumSpearItemComponent extends ItemComponent {
    public static final ComponentKey<EnviniumSpearItemComponent> KEY = ComponentRegistry.getOrCreate(Rinvenium.id("spear"), EnviniumSpearItemComponent.class);

    private static final String SPEAR_RUSH = "spear_rush";
    private static final String FINAL_RUSH = "final_rush";
    private static final String PARRY_WINDOW = "parry_window";
    private static final String DAMAGE_WINDOW = "damage_window";
    private static final String IS_IN_DAMAGE_STATE = "is_in_damage_state";
    
    public EnviniumSpearItemComponent(ItemStack stack) {
        super(stack);
    }

    public int getCharge() {
        return this.getInt(SPEAR_RUSH);
    }

    public void setCharge(int type) {
        this.putInt(SPEAR_RUSH, type);
    }

    public boolean getFinalRush() {
        return this.getBoolean(FINAL_RUSH);
    }

    public void setFinalRush(boolean type) {
        this.putBoolean(FINAL_RUSH, type);
    }

    public int getParryWindow() {
        return this.getInt(PARRY_WINDOW);
    }

    public void setParryWindow(int type) {
        this.putInt(PARRY_WINDOW, type);
    }

    public int getDamageWindow() {
        return this.getInt(DAMAGE_WINDOW);
    }

    public void setDamageWindow(int type) {
        this.putInt(DAMAGE_WINDOW, type);
    }

    public boolean isInDamageState() {
        return this.getBoolean(IS_IN_DAMAGE_STATE);
    }
    public void setIsInDamageState(boolean value) {
        this.putBoolean(IS_IN_DAMAGE_STATE, value);
    }
}
