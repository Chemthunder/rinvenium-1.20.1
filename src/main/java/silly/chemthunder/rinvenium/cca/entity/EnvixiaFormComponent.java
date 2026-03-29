package silly.chemthunder.rinvenium.cca.entity;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Ingredient;
import org.jetbrains.annotations.NotNull;
import silly.chemthunder.rinvenium.cca.RinveniumComponents;
import silly.chemthunder.rinvenium.cca.primitive.IntComponent;
import silly.chemthunder.rinvenium.cca.primitive.TripleBoolComponent;
import silly.chemthunder.rinvenium.index.RinveniumItems;
import silly.chemthunder.rinvenium.item.EnvixiaArmorItem;

import java.util.UUID;

public class EnvixiaFormComponent implements TripleBoolComponent, IntComponent, AutoSyncedComponent, CommonTickingComponent {
    public static final UUID HEALTH_BUFF_UUID = UUID.fromString("f79edb50-8a3b-408e-a87e-35ba81c43cb4");
    public static final String HEALTH_BUFF_ID = "Envixia Health Boost";
    public static final String IS_IN_ENVIXIA_KEY = "IsInEnvixia";
    public static final String CAN_FLY_KEY = "CanFly";
    public static final String SHOULD_TICK_FLY_TIME_KEY = "ShouldTickFlyTime";
    public static final String FLY_TIME_KEY = "FlyTime";
    private final PlayerEntity player;
    private boolean isInEnvixia = false;
    private boolean canFly = false;
    private boolean shouldTickFlyTime = false;
    private int flyTime = 0;
    private int tickDelta = 0;

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
        return this.flyTime;
    }

    @Override
    public void setInt(int value) {
        this.flyTime = value;
        this.sync();
    }

    @Override
    public void addValueToInt(int count) {
        this.flyTime += count;
        this.sync();
    }

    @Override
    public void incrementInt() {
        this.flyTime++;
        this.sync();
    }

    @Override
    public void decrementInt() {
        this.flyTime--;
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
            this.shouldTickFlyTime = false;
        }
        return this.shouldTickFlyTime;
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
        this.shouldTickFlyTime = value;
        this.sync();
    }

    @Override
    public void tick() {
        this.tickDelta++;
        if (this.tickDelta >= 20) {
            this.tickDelta = 0;
        }
        if (this.flyTime < 0) {
            this.flyTime = 0;
        }
        if (player.getInventory().getArmorStack(2).isOf(RinveniumItems.ENVIXIA_CHESTPLATE)) {
            if (!player.getAbilities().creativeMode) {
                player.getAbilities().allowFlying = !player.getItemCooldownManager().isCoolingDown(RinveniumItems.ENVIXIA_CHESTPLATE) && this.isInEnvixia;
            }
            if (player.getItemCooldownManager().isCoolingDown(RinveniumItems.ENVIXIA_CHESTPLATE) || !this.isInEnvixia) {
                if (!player.getAbilities().creativeMode && !player.isSpectator()) {
                    player.getAbilities().flying = false;
                }
            }
        }
        if (this.isInEnvixia) {
            if (player.getAbilities().flying) {
                this.setTripleBoolValue3(!player.getItemCooldownManager().isCoolingDown(RinveniumItems.ENVIXIA_CHESTPLATE));
                if (player.getAbilities().creativeMode && this.shouldTickFlyTime) {
                    this.setTripleBoolValue3(false);
                }
                if (this.shouldTickFlyTime) {
                    this.incrementInt();
                }
                if (this.flyTime >= 60) {
                    player.getAbilities().allowFlying = false;
                    player.getAbilities().flying = false;
                    player.getItemCooldownManager().set(RinveniumItems.ENVIXIA_CHESTPLATE, 200);
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 40, 0, true, false));
                    this.setInt(0);
                }
            } else {
                if (this.flyTime > 0) {
                    if (this.tickDelta % 5 == 0) {
                        this.decrementInt();
                    }
                }
            }
        }

        if (player.getAbilities().creativeMode && !player.getAbilities().allowFlying) {
            player.getAbilities().allowFlying = true;
        }
        if (player.isSpectator() && !player.getAbilities().flying) {
            player.getAbilities().flying = true;
        }


        if (this.isInEnvixia && EnvixiaArmorItem.hasFullSuit(this.player)) {
            if (!player.getAttributes().hasModifierForAttribute(EntityAttributes.GENERIC_MAX_HEALTH, HEALTH_BUFF_UUID)) {
                player.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).addTemporaryModifier(new EntityAttributeModifier(
                        HEALTH_BUFF_UUID,
                        HEALTH_BUFF_ID,
                        20.0,
                        EntityAttributeModifier.Operation.ADDITION
                ));
                if (player.getHealth() >= 20.0f) {
                    player.setHealth(player.getMaxHealth());
                }
                player.updateGlowing();
            }
        } else {
            if (player.getAttributes().hasModifierForAttribute(EntityAttributes.GENERIC_MAX_HEALTH, HEALTH_BUFF_UUID)) {
                player.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).removeModifier(HEALTH_BUFF_UUID);
                if (player.getHealth() >= player.getMaxHealth()) {
                    player.setHealth(player.getMaxHealth());
                }
                player.updateGlowing();
            }
        }
    }

    @Override
    public void readFromNbt(NbtCompound nbtCompound) {
        this.isInEnvixia = nbtCompound.getBoolean(IS_IN_ENVIXIA_KEY);
        this.canFly = nbtCompound.getBoolean(CAN_FLY_KEY);
        this.shouldTickFlyTime = nbtCompound.getBoolean(SHOULD_TICK_FLY_TIME_KEY);
        this.flyTime = nbtCompound.getInt(FLY_TIME_KEY);
    }

    @Override
    public void writeToNbt(NbtCompound nbtCompound) {
        nbtCompound.putBoolean(IS_IN_ENVIXIA_KEY, this.isInEnvixia);
        nbtCompound.putBoolean(CAN_FLY_KEY, this.canFly);
        nbtCompound.putBoolean(SHOULD_TICK_FLY_TIME_KEY, this.shouldTickFlyTime);
        nbtCompound.putInt(FLY_TIME_KEY, this.flyTime);
    }
}
