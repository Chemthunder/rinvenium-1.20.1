package silly.chemthunder.rinvenium.cca.entity;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import silly.chemthunder.rinvenium.Rinvenium;
import silly.chemthunder.rinvenium.cca.RinveniumComponents;
import silly.chemthunder.rinvenium.cca.primitive.BoolComponent;
import silly.chemthunder.rinvenium.cca.primitive.TripleIntComponent;
import silly.chemthunder.rinvenium.index.RinveniumSoundEvents;
import silly.chemthunder.rinvenium.index.RinveniumStatusEffects;
import silly.chemthunder.rinvenium.render.CustomFog;
import silly.chemthunder.rinvenium.render.manager.global.CustomFogManager;
import silly.chemthunder.rinvenium.util.RinveniumUtil;
import silly.chemthunder.rinvenium.util.inject.RenderContainer;

public class DeathSequenceComponent implements TripleIntComponent, BoolComponent, AutoSyncedComponent, CommonTickingComponent {
    /* List of timers:
     *  - Global
     *  - Slashes
     */

    public static final String SHOULD_START_KEY = "ShouldStart";
    public static final String GLOBAL_TIMER_KEY = "GlobalTimer";
    public static final String SLASH_TIMER_KEY = "SlashTimer";
    public static final String SKY_COLOR_KEY = "SkyColor";
    public static final String BASE_COLOR_KEY = "BaseSkyColor";
    public static final Identifier BOSSBAR_ID = Rinvenium.id("death_sequence_bossbar");
    public static final int MAIN_TOTAL_TIME = 2000;
    private final PlayerEntity player;
    private boolean shouldStart = false;
    private int globalTimer = 0;
    private int slashTimer = 0;
    private int skyColor = RinveniumUtil.colorFromFloat(BackgroundRenderer.red, BackgroundRenderer.green, BackgroundRenderer.blue);

    public DeathSequenceComponent(PlayerEntity player) {
        this.player = player;
    }

    public DeathSequenceComponent get(@NotNull PlayerEntity player) {
        return RinveniumComponents.DEATH_SEQUENCE.get(player);
    }

    private void sync() {
        RinveniumComponents.DEATH_SEQUENCE.sync(this.player);
    }

    @Override
    public boolean getBool() {
        return this.shouldStart;
    }

    @Override
    public void setBool(boolean value) {
        this.shouldStart = value;
        this.sync();
    }

    @Override
    public int getTripleIntValue1() {
        return this.globalTimer;
    }

    @Override
    public int getTripleIntValue2() {
        return this.slashTimer;
    }

    @Override
    public int getTripleIntValue3() {
        return this.skyColor;
    }

    @Override
    public void setTripleIntValue1(int value) {
        this.globalTimer = value;
        this.sync();
    }

    @Override
    public void setTripleIntValue2(int value) {
        this.slashTimer = value;
        this.sync();
    }

    @Override
    public void setTripleIntValue3(int value) {
        this.skyColor = value;
        this.sync();
    }

    @Override
    public void addToTripleIntValue1(int count) {
        this.globalTimer += count;
        this.sync();
    }

    @Override
    public void addToTripleIntValue2(int count) {
        this.slashTimer += count;
        this.sync();
    }

    @Override
    public void addToTripleIntValue3(int count) {

    }

    @Override
    public void incrementTripleIntValue1() {
        this.globalTimer++;
        this.sync();
    }

    @Override
    public void incrementTripleIntValue2() {
        this.slashTimer++;
        this.sync();
    }

    @Override
    public void incrementTripleIntValue3() {

    }

    @Override
    public void decrementTripleIntValue1() {
        this.globalTimer--;
        this.sync();
    }

    @Override
    public void decrementTripleIntValue2() {
        this.slashTimer--;
        this.sync();
    }

    @Override
    public void decrementTripleIntValue3() {

    }

    @Override
    public void readFromNbt(NbtCompound nbtCompound) {
        this.shouldStart = nbtCompound.getBoolean(SHOULD_START_KEY);
        this.globalTimer = nbtCompound.getInt(GLOBAL_TIMER_KEY);
        this.slashTimer = nbtCompound.getInt(SLASH_TIMER_KEY);
    }

    @Override
    public void writeToNbt(NbtCompound nbtCompound) {
        nbtCompound.putBoolean(SHOULD_START_KEY, this.shouldStart);
        nbtCompound.putInt(GLOBAL_TIMER_KEY, this.globalTimer);
        nbtCompound.putInt(SLASH_TIMER_KEY, this.slashTimer);
    }

    @Override
    public void tick() {
        if (this.shouldStart) {
            incrementTripleIntValue1();
        }
        if (this.globalTimer == 20 * 1) {
            playSound(RinveniumSoundEvents.BELL, 1.0f, 1.0f);
        }
        if (this.globalTimer == 20 * 3) {
            sendServerMessageT(Text.literal("orchidpuppy has entered reality").formatted(Formatting.YELLOW));
            player.getServer().getBossBarManager().add(BOSSBAR_ID, Text.literal("abyss").formatted(Formatting.DARK_RED)).setColor(BossBar.Color.RED);
        }
        if (this.globalTimer == 20 * 4) {
            if (player.getServer() != null) {
                player.getServer().getPlayerManager().getPlayerList().forEach(serverPlayerEntity -> {
                    if (serverPlayerEntity.squaredDistanceTo(player) <= 128 * 128) {
                        serverPlayerEntity.addStatusEffect(new StatusEffectInstance(RinveniumStatusEffects.WATCHED, MAIN_TOTAL_TIME, 0, false, false, false));
                        if (serverPlayerEntity.currentScreenHandler != null) {
                            serverPlayerEntity.closeHandledScreen();
                        }
                    }
                });
            }
        }
        if (this.globalTimer == 20 * 5) {
            sendServerMessageS("<orchidpuppy> oh.");
        }
        if (this.globalTimer == 20 * 6) {
            sendServerMessageS("<orchidpuppy> hello, friend.");
        }
        if (this.globalTimer == 20 * 8) {
            sendServerMessageS("<orchidpuppy> on the verge of death, aren't you?");
            playSound(RinveniumSoundEvents.HEARTBEAT, 1.0f, 0.75f);
        }
        if (this.globalTimer == 20 * 10) {
            sendServerMessageS("<orchidpuppy> good. we enjoy that fear.");
            playSound(RinveniumSoundEvents.HEARTBEAT, 1.0f, 1.5f);
        }
        if (this.globalTimer == 20 * 15) {
            sendServerMessageS("<orchidpuppy> how about we speed up the process?");
        }
        if (this.globalTimer == 20 * 16) {
            CustomFogManager.add(new CustomFog(0.5f, 0.0f, 0.0f, -1));
        }



        if (this.globalTimer == 20 * 5) {
            sendServerMessageS("<orchidpuppy> ...to the other friends left.");
        }
        if (this.globalTimer == 20 * 5) {
            sendServerMessageS("<orchidpuppy> just remember.");
        }
        if (this.globalTimer == 20 * 5) {
            if (player.getServer() != null) {
                if (player.getServer().getBossBarManager().get(BOSSBAR_ID) != null) {
                    player.getServer().getBossBarManager().get(BOSSBAR_ID).setName(Text.literal("we're always watching").formatted(Formatting.ITALIC).formatted(Formatting.RED));
                }
            }
        }
    }

    private void sendServerMessageT(Text literal) {
        if (player.getServer() != null) {
            player.getServer().sendMessage(literal);
        }
    }
    private void sendServerMessageS(String string) {
        if (player.getServer() != null) {
            player.getServer().sendMessage(Text.literal(string));
        }
    }
    private void playSound(SoundEvent soundEvent, float volume, float pitch) {
        if (player.getServer() != null) {
            player.getServer().getPlayerManager().getPlayerList().forEach(serverPlayerEntity -> {
                if (serverPlayerEntity.squaredDistanceTo(player) <= 128 * 128) {
                    serverPlayerEntity.playSound(soundEvent,  volume, pitch);
                }
            });
        }
    }
}
