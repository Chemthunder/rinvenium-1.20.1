package silly.chemthunder.rinvenium.cca.entity;

import com.mojang.authlib.GameProfile;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import silly.chemthunder.rinvenium.Rinvenium;
import silly.chemthunder.rinvenium.cca.RinveniumComponents;
import silly.chemthunder.rinvenium.cca.primitive.BoolComponent;
import silly.chemthunder.rinvenium.cca.primitive.TripleIntComponent;
import silly.chemthunder.rinvenium.index.RinveniumSoundEvents;
import silly.chemthunder.rinvenium.index.RinveniumStatusEffects;
import silly.chemthunder.rinvenium.render.FakePlayerRenderer;
import silly.chemthunder.rinvenium.render.SlashRender;
import silly.chemthunder.rinvenium.render.VertexColorSet;
import silly.chemthunder.rinvenium.render.manager.global.PlayerRendererManager;
import silly.chemthunder.rinvenium.render.manager.global.SlashRendererManager;
import silly.chemthunder.rinvenium.util.RinveniumUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DeathSequenceComponent implements TripleIntComponent, BoolComponent, AutoSyncedComponent, CommonTickingComponent {
    /* List of timers:
     *  - Global
     *  - Slashes
     */

    public static final String SHOULD_START_KEY = "ShouldStart";
    public static final String GLOBAL_TIMER_KEY = "GlobalTimer";
    public static final String SLASH_TIMER_KEY = "SlashTimer";
    public static final String IMPACT_FRAME_KEY = "ImpactFrameKey";
    public static final String BOSSBAR_NAME_KEY = "BossBarName";
    public static final Identifier BOSSBAR_ID = Rinvenium.id("death_sequence_bossbar");
    public static final int MAIN_TOTAL_TIME = 20 * 36;
    private final PlayerEntity player;
    private boolean shouldStart = false;
    private int globalTimer = 0;
    private int slashTimer = 0;
    private int impactFrameTimer = 0;
    private String bossBarName = "abyss";
    private ServerBossBar bossBar = (ServerBossBar)new ServerBossBar(Text.of(bossBarName), BossBar.Color.RED, BossBar.Style.PROGRESS).setDarkenSky(true);

    public DeathSequenceComponent(PlayerEntity player) {
        this.player = player;
    }

    public static DeathSequenceComponent get(@NotNull PlayerEntity player) {
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
        if (!value) {
            this.resetTimes();
        }
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
        return this.impactFrameTimer;
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
        this.impactFrameTimer = value;
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
        this.impactFrameTimer += count;
        this.sync();
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
        this.impactFrameTimer++;
        this.sync();
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
        this.impactFrameTimer--;
        this.sync();
    }

    @Override
    public void readFromNbt(NbtCompound nbtCompound) {
        this.shouldStart = nbtCompound.getBoolean(SHOULD_START_KEY);
        this.globalTimer = nbtCompound.getInt(GLOBAL_TIMER_KEY);
        this.slashTimer = nbtCompound.getInt(SLASH_TIMER_KEY);
        this.impactFrameTimer = nbtCompound.getInt(IMPACT_FRAME_KEY);
        this.bossBarName = nbtCompound.getString(BOSSBAR_NAME_KEY);
    }

    @Override
    public void writeToNbt(NbtCompound nbtCompound) {
        nbtCompound.putBoolean(SHOULD_START_KEY, this.shouldStart);
        nbtCompound.putInt(GLOBAL_TIMER_KEY, this.globalTimer);
        nbtCompound.putInt(SLASH_TIMER_KEY, this.slashTimer);
        nbtCompound.putInt(IMPACT_FRAME_KEY, this.impactFrameTimer);
        nbtCompound.putString(BOSSBAR_NAME_KEY, this.bossBarName);
    }

    @Override
    public void tick() {
        if (this.shouldStart) {
            incrementTripleIntValue1();
        }
        player.sendMessage(Text.literal("Global Tick: " + this.globalTimer), true);
        if (this.globalTimer == 20 * 1) {
            playSound(RinveniumSoundEvents.BELL, 1.0f, 1.0f);
        }
        if (this.globalTimer == 20 * 3) {
            sendServerMessageT(Text.literal("orchidpuppy has entered reality").formatted(Formatting.YELLOW));
            Vec3d spawnPos = player.getEyePos().add(player.getRotationVector().normalize().multiply(4.5));
            Vec3d fakePlayerEyePos = spawnPos.add(0, EntityType.PLAYER.getDimensions().height * 0.85f, 0);
            double d = player.getEyePos().x - fakePlayerEyePos.x;
            double e = player.getEyePos().y - fakePlayerEyePos.y;
            double f = player.getEyePos().z - fakePlayerEyePos.z;
            double g = Math.sqrt(d * d + f * f);
            float pitch = MathHelper.wrapDegrees((float)(-(MathHelper.atan2(e, g) * 180.0F / (float)Math.PI)));
            float yaw = MathHelper.wrapDegrees((float)(MathHelper.atan2(f, d) * 180.0F / (float)Math.PI) - 90.0F);
            PlayerRendererManager.add(new FakePlayerRenderer(new GameProfile(UUID.randomUUID(), "orchidpuppy"), spawnPos, pitch, yaw, -1, "orchidpuppy"));

            if (player.getServer() != null) {
                player.getServer().getPlayerManager().getPlayerList().forEach(serverPlayerEntity -> {
                    if (serverPlayerEntity.squaredDistanceTo(player) <= 128 * 128) {
                        this.bossBar.addPlayer(serverPlayerEntity);
                    }
                });
            }
        }
        if (this.globalTimer == 20 * 4) {
            if (player.getServer() != null) {
                player.getServer().getPlayerManager().getPlayerList().forEach(serverPlayerEntity -> {
                    if (serverPlayerEntity.squaredDistanceTo(player) <= 128 * 128) {
                        serverPlayerEntity.addStatusEffect(new StatusEffectInstance(RinveniumStatusEffects.WATCHED, MAIN_TOTAL_TIME - 80, 0, false, false, false));
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
        if (this.globalTimer == 20 * 16.5) {
            if (!PlayerRendererManager.get().isEmpty()) {
                PlayerRendererManager.get().peek().fakePlayer.swingHand(Hand.MAIN_HAND, true);
            }
        }
        if (this.globalTimer >= 20 * 17 && this.globalTimer <= 20 * 28) {
            Vec3d origin = player.getPos().add(0, (player.getBoundingBox().maxY - player.getBoundingBox().minY) / 2, 0);
            World world = player.getWorld();
            float randomX = world.random.nextFloat();
            randomX = randomX < 0.5 ? -randomX : randomX - 0.5f;
            float randomY = world.random.nextFloat();
            randomY = randomY < 0.5 ? -randomY : randomY - 0.5f;
            float randomZ = world.random.nextFloat();
            randomZ = randomZ < 0.5 ? -randomZ : randomZ - 0.5f;
            Vec3d originDelta = new Vec3d(randomX, randomY, randomZ);
            origin = origin.add(originDelta);
            float pitch = world.random.nextFloat() * 360.0F;
            float yaw = world.random.nextFloat() * 360.0F;
            float roll = world.random.nextFloat() * 360.0F;
            SlashRender slashRender = new SlashRender(
                    origin,
                    60,
                    new VertexColorSet(1.0f, 0.0f, 0.0f, 0.9f),
                    new VertexColorSet(0.4f, 0.0f, 0.0f, 0.9f),
                    new VertexColorSet(1.0f, 0.0f, 0.0f, 0.9f),
                    new VertexColorSet(1.0f, 0.9f, 0.9f, 1.0f)
            );
            slashRender.addTransformation(RotationAxis.POSITIVE_Y.rotationDegrees(90));
            slashRender.addTransformation(RotationAxis.NEGATIVE_X.rotationDegrees(90));
            slashRender.addTransformation(RotationAxis.POSITIVE_X.rotationDegrees(pitch));
            slashRender.addTransformation(RotationAxis.POSITIVE_Z.rotationDegrees(roll));
            slashRender.addTransformation(RotationAxis.POSITIVE_Y.rotationDegrees(yaw));
            slashRender.setSize(10.0f);

            origin = player.getPos().add(0, (player.getBoundingBox().maxY - player.getBoundingBox().minY) / 2, 0);
            SlashRender bigSlashRender = new SlashRender(
                    origin,
                    100,
                    new VertexColorSet(1.0f, 0.0f, 0.0f, 0.9f),
                    new VertexColorSet(0.4f, 0.0f, 0.0f, 0.9f),
                    new VertexColorSet(1.0f, 0.0f, 0.0f, 0.9f),
                    new VertexColorSet(1.0f, 0.9f, 0.9f, 1.0f)
            );
            bigSlashRender.addTransformation(RotationAxis.POSITIVE_Y.rotationDegrees(90));
            bigSlashRender.addTransformation(RotationAxis.NEGATIVE_X.rotationDegrees(45));
            bigSlashRender.addTransformation(RotationAxis.POSITIVE_Z.rotationDegrees(60));
            bigSlashRender.setSize(16.0f);

            addSlashes(
                    slashRender,
                    bigSlashRender
            );
        }


        if (this.globalTimer == 20 * 31) {
            if (player.getWorld() != null && player.getServer() != null) {
                ServerPlayerEntity serverPlayerEntity = player.getWorld().getClosestEntity(player.getServer().getPlayerManager().getPlayerList(), TargetPredicate.createAttackable().setPredicate(Entity::isAlive), player, player.getX(), player.getY(), player.getZ());
                if (serverPlayerEntity != null) {
                    Vec3d bystanderTpPos = serverPlayerEntity.getPos().add(serverPlayerEntity.getRotationVector().normalize().multiply(1.5));
                    Vec3d fakePlayerEyePos = bystanderTpPos.add(0, EntityType.PLAYER.getDimensions().height * 0.85f, 0);
                    double d = serverPlayerEntity.getEyePos().x - fakePlayerEyePos.x;
                    double e = serverPlayerEntity.getEyePos().y - fakePlayerEyePos.y;
                    double f = serverPlayerEntity.getEyePos().z - fakePlayerEyePos.z;
                    double g = Math.sqrt(d * d + f * f);
                    float pitch = MathHelper.wrapDegrees((float)(-(MathHelper.atan2(e, g) * 180.0F / (float)Math.PI)));
                    float yaw = MathHelper.wrapDegrees((float)(MathHelper.atan2(f, d) * 180.0F / (float)Math.PI) - 90.0F);
                    if (!PlayerRendererManager.get().isEmpty()) {
                        FakePlayerRenderer orchid = PlayerRendererManager.getPlayer("orchidpuppy");
                        if (orchid != null) {
                            orchid.pitch = pitch;
                            orchid.yaw = yaw;
                        }

                    }
                }
            }
        }
        if (this.globalTimer == 20 * 32) {
            sendServerMessageS("<orchidpuppy> ...to the other friends left.");
        }
        if (this.globalTimer == 20 * 33) {
            sendServerMessageS("<orchidpuppy> just remember.");
        }
        if (this.globalTimer == 20 * 35) {
            if (player.getServer() != null) {
                this.bossBar.setName(Text.literal("we're always watching").formatted(Formatting.RED).formatted(Formatting.ITALIC));
                this.bossBarName = "we're always watching";
                this.sync();
            }
        }
        if (this.globalTimer == 20 * 36) {
            PlayerRendererManager.remove("orchidpuppy");
        }
        if (this.globalTimer >= 20 * 36 + 20 * 60) {
            shouldStart = false;
            this.bossBar.clearPlayers();
            resetAll();
        }
    }

    private void addSlashes(SlashRender... slashes) {
        List<SlashRender> slashRenders = List.of(slashes);
        if (!slashRenders.isEmpty()) {
            if (this.slashTimer >= 0 && this.slashTimer < 20 * 1.5 && this.slashTimer % 10 == 0) {
                SlashRendererManager.add(slashRenders.get(0));
            }
            if (this.slashTimer >= 20 * 1.5 && this.slashTimer < 20 * 3 && this.slashTimer % 5 == 0) {
                SlashRendererManager.add(slashRenders.get(0));
            }
            if (this.slashTimer >= 20 * 3 && this.slashTimer < 72 && this.slashTimer % 3 == 0) {
                SlashRendererManager.add(slashRenders.get(0));
            }
            if (this.slashTimer >= 76 && this.slashTimer < 90 && this.slashTimer % 2 == 0) {
                SlashRendererManager.add(slashRenders.get(0));
            }
            if (this.slashTimer >= 90 && this.slashTimer < 110) {
                SlashRendererManager.add(slashRenders.get(0));
            }
            if (this.slashTimer == 120) {
                if (slashRenders.size() > 1) {
                    SlashRendererManager.add(slashRenders.get(1));
                } else {
                    SlashRendererManager.add(slashRenders.get(0));
                }
                sendServerMessageT(player.getDisplayName().copy().formatted(Formatting.YELLOW).append(Text.literal(" was executed").formatted(Formatting.YELLOW)));
            }
        }
        this.incrementTripleIntValue2();
    }

    public void resetTimes() {
        this.setTripleIntValue1(0);
        this.setTripleIntValue2(0);
        this.setTripleIntValue3(0);
    }

    public void resetAll() {
        resetTimes();
        this.bossBarName = "abyss";
        this.bossBar.setName(Text.literal(bossBarName).formatted(Formatting.WHITE));

        this.sync();
    }

    private void sendServerMessageT(MutableText literal) {
        if (player.getServer() != null) {
            player.sendMessage(literal);
        }
    }
    private void sendServerMessageS(String string) {
        if (player.getServer() != null) {
            player.sendMessage(Text.literal(string).formatted(Formatting.RED));
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
