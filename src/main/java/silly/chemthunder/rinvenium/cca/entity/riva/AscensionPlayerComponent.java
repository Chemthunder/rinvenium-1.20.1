package silly.chemthunder.rinvenium.cca.entity.riva;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import silly.chemthunder.rinvenium.Rinvenium;

public class AscensionPlayerComponent implements AutoSyncedComponent, CommonTickingComponent {
    public static final ComponentKey<AscensionPlayerComponent> KEY = ComponentRegistry.getOrCreate(Rinvenium.id("riva_ascent"), AscensionPlayerComponent.class);
    private final PlayerEntity player;
    public boolean isAscented = false;

    public void sync() {
        KEY.sync(this.player);
    }
    public AscensionPlayerComponent(PlayerEntity player) {
        this.player = player;
    }


    public void tick() {
        if (isAscented) {

        }
    }

    // nbt =======
    public void readFromNbt(NbtCompound nbtCompound) {
        this.isAscented = nbtCompound.getBoolean("isAscented");
    }

    public void writeToNbt(NbtCompound nbtCompound) {
        nbtCompound.putBoolean("isAscented", isAscented);
    }
}
