package silly.chemthunder.rinvenium.cca.entity;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvents;
import org.jetbrains.annotations.NotNull;
import silly.chemthunder.rinvenium.cca.RinveniumComponents;
import silly.chemthunder.rinvenium.cca.primitive.DoubleIntComponent;
import silly.chemthunder.rinvenium.index.RinveniumItems;

public class HailOfTheGodComponent implements DoubleIntComponent, AutoSyncedComponent, CommonTickingComponent {
    public static final String USE_TIME_KEY = "HotGUseTime";
    public static final String OVERHEAT_TIME_KEY = "HotGOverheatTime";
    public static final int MAX_USE_TIME = 120 * 3;
    public static final int MAX_OVERHEAT_TIME = 60 * 5;
    private final PlayerEntity player;
    private int useTime = 0;
    private int overheatTime = 0;

    public HailOfTheGodComponent(PlayerEntity player) {
        this.player = player;
    }

    public static HailOfTheGodComponent get(@NotNull PlayerEntity player) {
        return RinveniumComponents.HAIL_OF_THE_GODS.get(player);
    }

    private void sync() {
        RinveniumComponents.HAIL_OF_THE_GODS.sync(player);
    }

    @Override
    public int getDoubleIntValue1() {
        return this.useTime;
    }

    @Override
    public int getDoubleIntValue2() {
        return this.overheatTime;
    }

    @Override
    public void setDoubleIntValue1(int value) {
        this.useTime = value;
        this.sync();
    }

    @Override
    public void setDoubleIntValue2(int value) {
        this.overheatTime = value;
        this.sync();
    }

    @Override
    public void addToDoubleIntValue1(int count) {
        this.useTime += count;
        this.sync();
    }

    @Override
    public void addToDoubleIntValue2(int count) {
        this.overheatTime += count;
        this.sync();
    }

    @Override
    public void incrementDoubleIntValue1() {
        this.useTime++;
        this.sync();
    }

    @Override
    public void incrementDoubleIntValue2() {
        this.overheatTime++;
        this.sync();
    }

    @Override
    public void decrementDoubleIntValue1() {
        this.useTime--;
        this.sync();
    }

    @Override
    public void decrementDoubleIntValue2() {
        this.overheatTime--;
        this.sync();
    }

    @Override
    public void tick() {
        if (this.useTime < 0) {
            this.setDoubleIntValue1(0);
        }
        if (this.overheatTime < 0) {
            this.setDoubleIntValue2(0);
        }
        if (this.overheatTime >= MAX_OVERHEAT_TIME) {
            player.getItemCooldownManager().set(RinveniumItems.HAIL_OF_THE_GODS, 270);
            player.playSound(SoundEvents.BLOCK_LAVA_EXTINGUISH, 0.8f, 1.1f + player.getWorld().random.nextFloat() * 0.1f);
            if (player.getActiveItem() != null && player.getActiveItem().isOf(RinveniumItems.HAIL_OF_THE_GODS)) {
                player.stopUsingItem();
            }
        }
        if (this.overheatTime <= 0) {
            if (this.useTime > 0) {
                if (!player.isUsingItem()) {
                    this.addToDoubleIntValue1(-2);
                } else if (player.getActiveItem() != null && !player.getActiveItem().isOf(RinveniumItems.HAIL_OF_THE_GODS)) {
                    this.addToDoubleIntValue1(-2);
                }
            }
        } else {
            if (!player.isUsingItem()) {
                this.addToDoubleIntValue2(-2);
            } else if (player.getActiveItem() != null && !player.getActiveItem().isOf(RinveniumItems.HAIL_OF_THE_GODS)) {
                this.addToDoubleIntValue2(-2);
            }
        }
    }

    @Override
    public void readFromNbt(NbtCompound nbtCompound) {
        this.useTime = nbtCompound.getInt(USE_TIME_KEY);
        this.overheatTime = nbtCompound.getInt(OVERHEAT_TIME_KEY);
    }

    @Override
    public void writeToNbt(NbtCompound nbtCompound) {
        nbtCompound.putInt(USE_TIME_KEY, this.useTime);
        nbtCompound.putInt(OVERHEAT_TIME_KEY, this.overheatTime);
    }
}
