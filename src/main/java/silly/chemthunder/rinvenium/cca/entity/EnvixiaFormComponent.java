package silly.chemthunder.rinvenium.cca.entity;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;
import silly.chemthunder.rinvenium.cca.RinveniumComponents;
import silly.chemthunder.rinvenium.cca.primitive.IntComponent;
import silly.chemthunder.rinvenium.cca.primitive.TripleBoolComponent;

public class EnvixiaFormComponent implements TripleBoolComponent, IntComponent, AutoSyncedComponent, CommonTickingComponent {
    public static final String IS_IN_ENVIXIA_KEY = "isInEnvixia";
    public static final String CAN_FLY_KEY = "canFly";
    public static final String SHOULD_START_DEATH_SEQ_KEY = "shouldStartDeathSeq";
    public static final String DEATH_SEQ_ANIMATION_TIME_KEY = "deathSeqAnimationTime";
    private final PlayerEntity player;
    private boolean isInEnvixia = false;
    private boolean canFly = false;
    private boolean shouldStartDeathSeq = false;

    public EnvixiaFormComponent(PlayerEntity player) {
        this.player = player;
    }

    public static EnvixiaFormComponent get(@NotNull PlayerEntity player) {
        return RinveniumComponents.ENVIXIA_FORM.get(player);
    }
    private void sync() {
        RinveniumComponents.ENVIXIA_FORM.sync(player);
    }

    @Override
    public int getInt() {
        return 0;
    }

    @Override
    public void setInt(int value) {
        this.sync();
    }

    @Override
    public void addValueToInt(int count) {

    }

    @Override
    public void incrementInt() {

    }

    @Override
    public void decrementInt() {

    }

    @Override
    public boolean getTripleBoolValue1() {

        return false;
    }

    @Override
    public boolean getTripleBoolValue2() {
        return false;
    }

    @Override
    public boolean getTripleBoolValue3() {
        return false;
    }

    @Override
    public void setTripleBoolValue1(boolean value) {

    }

    @Override
    public void setTripleBoolValue2(boolean value) {

    }

    @Override
    public void setTripleBoolValue3(boolean value) {

    }

    @Override
    public void tick() {

    }

    @Override
    public void readFromNbt(NbtCompound nbtCompound) {

    }

    @Override
    public void writeToNbt(NbtCompound nbtCompound) {

    }
}
