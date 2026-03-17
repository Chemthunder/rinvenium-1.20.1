package silly.chemthunder.rinvenium.cca.entity;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;
import silly.chemthunder.rinvenium.cca.RinveniumComponents;
import silly.chemthunder.rinvenium.cca.primitive.IntComponent;
import silly.chemthunder.rinvenium.cca.primitive.TripleBoolComponent;
import silly.chemthunder.rinvenium.item.EnvixiaArmorItem;

public class EnvixiaFormComponent implements TripleBoolComponent, IntComponent, AutoSyncedComponent, CommonTickingComponent {
    public static final String IS_IN_ENVIXIA_KEY = "IsInEnvixia";
    public static final String CAN_FLY_KEY = "CanFly";
    public static final String SHOULD_START_DEATH_SEQ_KEY = "ShouldStartDeathSeq";
    public static final String DEATH_SEQ_ANIMATION_TIME_KEY = "DeathSeqAnimationTime";
    private final PlayerEntity player;
    private boolean isInEnvixia = false;
    private boolean canFly = false;
    private boolean shouldStartDeathSeq = false;
    private int death_seq_animation_time = 0;

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
        return this.death_seq_animation_time;
    }

    @Override
    public void setInt(int value) {
        this.death_seq_animation_time = value;
        this.sync();
    }

    @Override
    public void addValueToInt(int count) {
        this.death_seq_animation_time += count;
        this.sync();
    }

    @Override
    public void incrementInt() {
        this.death_seq_animation_time++;
        this.sync();
    }

    @Override
    public void decrementInt() {
        this.death_seq_animation_time--;
        this.sync();
    }

    @Override
    public boolean getTripleBoolValue1() {
        if (!EnvixiaArmorItem.hasFullSuit(this.player)) {
            this.isInEnvixia = false;
        }
        return this.isInEnvixia;
    }

    @Override
    public boolean getTripleBoolValue2() {
        if (!this.isInEnvixia || !EnvixiaArmorItem.hasFullSuit(this.player)) {
            this.canFly = false;
        }
        return this.canFly;
    }

    @Override
    public boolean getTripleBoolValue3() {
        if (!this.isInEnvixia || player.getHealth() > 0) {
            this.shouldStartDeathSeq = false;
        }
        return this.shouldStartDeathSeq;
    }

    @Override
    public void setTripleBoolValue1(boolean value) {
        this.isInEnvixia = value;
        this.sync();
    }

    @Override
    public void setTripleBoolValue2(boolean value) {
        this.canFly = value;
        this.sync();
    }

    @Override
    public void setTripleBoolValue3(boolean value) {
        this.shouldStartDeathSeq = value;
        this.sync();
    }

    @Override
    public void tick() {
        if (this.shouldStartDeathSeq) {
            startDeathSequence();
        }
        if (!player.isSpectator()) {
            if (!player.getAbilities().allowFlying && this.isInEnvixia) {
                this.setTripleBoolValue2(true);
            } else if ((!this.isInEnvixia || !EnvixiaArmorItem.hasFullSuit(this.player)) && !player.getAbilities().creativeMode) {
                this.setTripleBoolValue2(false);
            }
            if (!player.getAbilities().creativeMode) {
                player.getAbilities().allowFlying = this.getTripleBoolValue2();
            }
        }
    }

    private void startDeathSequence() {

    }

    @Override
    public void readFromNbt(NbtCompound nbtCompound) {
        this.isInEnvixia = nbtCompound.getBoolean(IS_IN_ENVIXIA_KEY);
        this.canFly = nbtCompound.getBoolean(CAN_FLY_KEY);
        this.shouldStartDeathSeq = nbtCompound.getBoolean(SHOULD_START_DEATH_SEQ_KEY);
        this.death_seq_animation_time = nbtCompound.getInt(DEATH_SEQ_ANIMATION_TIME_KEY);
    }

    @Override
    public void writeToNbt(NbtCompound nbtCompound) {
        nbtCompound.putBoolean(IS_IN_ENVIXIA_KEY, this.isInEnvixia);
        nbtCompound.putBoolean(CAN_FLY_KEY, this.canFly);
        nbtCompound.putBoolean(SHOULD_START_DEATH_SEQ_KEY, this.shouldStartDeathSeq);
        nbtCompound.putInt(DEATH_SEQ_ANIMATION_TIME_KEY, this.death_seq_animation_time);
    }
}
