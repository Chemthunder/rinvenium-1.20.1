package silly.chemthunder.rinvenium.cca.entity;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
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
import silly.chemthunder.rinvenium.index.RinveniumDamageSources;
import silly.chemthunder.rinvenium.index.RinveniumPackets;
import silly.chemthunder.rinvenium.index.RinveniumSoundEvents;
import silly.chemthunder.rinvenium.index.RinveniumStatusEffects;
import silly.chemthunder.rinvenium.render.SlashRender;
import silly.chemthunder.rinvenium.util.persistent.DeathSequenceState;

import java.util.List;

public class DeathSequenceComponent implements TripleIntComponent, BoolComponent, AutoSyncedComponent, CommonTickingComponent {
    /* List of timers:
     *  - Global
     *  - Slashes
     *  - Impact Frames
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

            if (player.getServer() != null) {
                player.getServer().getPlayerManager().getPlayerList().forEach(serverPlayerEntity -> {
                    if (serverPlayerEntity.squaredDistanceTo(player) <= 128 * 128) {
                        this.bossBar.addPlayer(serverPlayerEntity);
                        serverPlayerEntity.addStatusEffect(new StatusEffectInstance(RinveniumStatusEffects.WATCHED, MAIN_TOTAL_TIME - 300, 0, false, false, false));
                        if (serverPlayerEntity.currentScreenHandler != null) {
                            serverPlayerEntity.closeHandledScreen();
                        }

                        RinveniumPackets.spawnFakePlayerPosPitchYawAge(serverPlayerEntity, "orchidpuppy", spawnPos, pitch, yaw, -1);
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
            //playSound(RinveniumSoundEvents.HEARTBEAT, 1.0f, 0.75f);
        }
        if (this.globalTimer == 20 * 10) {
            sendServerMessageS("<orchidpuppy> good. we enjoy that fear.");
            //playSound(RinveniumSoundEvents.HEARTBEAT, 1.0f, 1.5f);
        }
        if (this.globalTimer == 20 * 15) {
            sendServerMessageS("<orchidpuppy> how about we speed up the process?");
        }
        if (this.globalTimer == 20 * 16.5) {
            if (player.getServer() != null) {
                player.getServer().getPlayerManager().getPlayerList().forEach(serverPlayerEntity -> {
                    if (serverPlayerEntity.squaredDistanceTo(player) <= 128 * 128) {
                        RinveniumPackets.sendFakePlayerArmSwing(serverPlayerEntity, "orchidpuppy");
                    }
                });
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
            Vec3d originDelta = new Vec3d(randomX, randomY, randomZ).multiply(0.5);
            origin = origin.add(originDelta);
            float pitch = world.random.nextFloat() * 360.0F;
            float yaw = world.random.nextFloat() * 360.0F;
            float roll = world.random.nextFloat() * 360.0F;
            SlashRender slashRender = new SlashRender(
                    origin,
                    60
            );
            slashRender.addTransformation(RotationAxis.POSITIVE_Y.rotationDegrees(90));
            slashRender.addTransformation(RotationAxis.NEGATIVE_X.rotationDegrees(90));
            slashRender.setSize(20.0f);

            origin = player.getPos().add(0, (player.getBoundingBox().maxY - player.getBoundingBox().minY) / 2, 0);
            SlashRender bigSlashRender = new SlashRender(
                    origin,
                    100
            );
            bigSlashRender.addTransformation(RotationAxis.POSITIVE_Y.rotationDegrees(90));
            bigSlashRender.addTransformation(RotationAxis.NEGATIVE_X.rotationDegrees(45));
            bigSlashRender.addTransformation(RotationAxis.POSITIVE_Z.rotationDegrees(60));
            bigSlashRender.setSize(32.0f);
            if (player.getServer() != null) {
                player.getServer().getPlayerManager().getPlayerList().forEach(serverPlayerEntity -> {
                    if (serverPlayerEntity.squaredDistanceTo(player) <= 128 * 128 && !player.getWorld().isClient) {
                        addSlashes(
                                player.getServer().getPlayerManager().getPlayerList(),
                                slashRender,
                                bigSlashRender
                        );
                    }
                });
            }
        }
    }

    private void addSlashes(List<ServerPlayerEntity> playerList, SlashRender... slashes) {
        List<SlashRender> slashRenders = List.of(slashes);
        Vec3d origin = player.getPos().add(0, (player.getBoundingBox().maxY - player.getBoundingBox().minY) / 2, 0);
        playerList.forEach(serverPlayerEntity -> {
            if (!slashRenders.isEmpty()) {
                if (this.slashTimer >= 0 && this.slashTimer < 20 * 1.5) {
                    if (this.slashTimer % 15 == 0) {
                        player.damage(RinveniumDamageSources.orchid(player), 0);
                    }
                    if (this.slashTimer == 0) {
                        PacketByteBuf slashBuf = PacketByteBufs.create();
                        int numberOfSlashes = 3;
                        int[] ageDelta = new int[numberOfSlashes];
                        for (int i = 0; i < numberOfSlashes; i++) {
                            ageDelta[i] = i * 15;
                        }
                        slashBuf = SlashRender.writeMultiple(slashBuf, origin, slashes[0].maxAge, slashes[0].maxAge, true, numberOfSlashes, ageDelta);
                        ServerPlayNetworking.send(serverPlayerEntity, RinveniumPackets.ADD_MULTIPLE_SLASHES, slashBuf);
                    }
                }
                if (this.slashTimer >= 20 * 1.5 && this.slashTimer < 20 * 3) {
                    if (this.slashTimer % 7 == 0) {
                        player.damage(RinveniumDamageSources.orchid(player), 0);
                    }
                    if (this.slashTimer == 20 * 1.5) {
                        PacketByteBuf slashBuf = PacketByteBufs.create();
                        int numberOfSlashes = 6;
                        int[] ageDelta = new int[numberOfSlashes];
                        for (int i = 0; i < numberOfSlashes; i++) {
                            ageDelta[i] = i * 7;
                        }
                        slashBuf = SlashRender.writeMultiple(slashBuf, origin, slashes[0].maxAge, slashes[0].maxAge, true, numberOfSlashes, ageDelta);
                        ServerPlayNetworking.send(serverPlayerEntity, RinveniumPackets.ADD_MULTIPLE_SLASHES, slashBuf);
                    }
                }
                if (this.slashTimer >= 20 * 3 && this.slashTimer < 72) {
                    if (this.slashTimer % 5 == 0) {
                        player.damage(RinveniumDamageSources.orchid(player), 0);
                    }
                    if (this.slashTimer == 20 * 3) {
                        PacketByteBuf slashBuf = PacketByteBufs.create();
                        int numberOfSlashes = 4;
                        int[] ageDelta = new int[numberOfSlashes];
                        for (int i = 0; i < numberOfSlashes; i++) {
                            ageDelta[i] = i * 5;
                        }
                        slashBuf = SlashRender.writeMultiple(slashBuf, origin, slashes[0].maxAge, slashes[0].maxAge, true, numberOfSlashes, ageDelta);
                        ServerPlayNetworking.send(serverPlayerEntity, RinveniumPackets.ADD_MULTIPLE_SLASHES, slashBuf);
                    }
                }
                if (this.slashTimer >= 72 && this.slashTimer < 90) {
                    if (this.slashTimer % 3 == 0) {
                        player.damage(RinveniumDamageSources.orchid(player), 0);
                    }
                    if (this.slashTimer == 72) {
                        PacketByteBuf slashBuf = PacketByteBufs.create();
                        int numberOfSlashes = 9;
                        int[] ageDelta = new int[numberOfSlashes];
                        for (int i = 0; i < numberOfSlashes; i++) {
                            ageDelta[i] = i * 3;
                        }
                        slashBuf = SlashRender.writeMultiple(slashBuf, origin, slashes[0].maxAge, slashes[0].maxAge, true, numberOfSlashes, ageDelta);
                        ServerPlayNetworking.send(serverPlayerEntity, RinveniumPackets.ADD_MULTIPLE_SLASHES, slashBuf);
                        //RinveniumPackets.sendImpactFrame(serverPlayerEntity, 10, this.player);
                    }
                }
                if (this.slashTimer >= 90 && this.slashTimer < 110) {
                    if (this.slashTimer % 2 == 0) {
                        player.damage(RinveniumDamageSources.orchid(player), 0);
                    }
                    if (this.slashTimer == 90) {
                        PacketByteBuf slashBuf = PacketByteBufs.create();
                        int numberOfSlashes = 10;
                        int[] ageDelta = new int[numberOfSlashes];
                        for (int i = 0; i < numberOfSlashes; i++) {
                            ageDelta[i] = i * 2;
                        }
                        slashBuf = SlashRender.writeMultiple(slashBuf, origin, slashes[0].maxAge, slashes[0].maxAge, true, numberOfSlashes, ageDelta);
                        ServerPlayNetworking.send(serverPlayerEntity, RinveniumPackets.ADD_MULTIPLE_SLASHES, slashBuf);
                    }
                }
                if (this.slashTimer == 165) {
                    if (serverPlayerEntity.squaredDistanceTo(this.player) <= 128 * 128) {
                        RinveniumPackets.sendImpactFrame(serverPlayerEntity, 40, this.player);
                    }
                }
                if (this.slashTimer == 170) {
                    if (player.getServer() != null) {
                        DeathSequenceState deathSequenceState = DeathSequenceState.getServerState(player.getServer());
                        deathSequenceState.shouldStartPostTick = true;
                        deathSequenceState.markDirty();
                    }
                    if (slashRenders.size() > 1) {
                        PacketByteBuf slashBuf = PacketByteBufs.create();
                        slashBuf = SlashRender.writeSingular(slashBuf, origin, slashes[1].size, slashes[1].maxAge, false);
                        ServerPlayNetworking.send(serverPlayerEntity, RinveniumPackets.ADD_SINGULAR_SLASH, slashBuf);
                    } else {
                        PacketByteBuf slashBuf = PacketByteBufs.create();
                        slashBuf = SlashRender.writeSingular(slashBuf, origin, slashes[0].size, slashes[0].maxAge, true);
                        ServerPlayNetworking.send(serverPlayerEntity, RinveniumPackets.ADD_SINGULAR_SLASH, slashBuf);
                    }
                    //sendServerMessageT(player.getDisplayName().copy().formatted(Formatting.YELLOW).append(Text.literal(" was executed").formatted(Formatting.YELLOW)));
                    this.resetAll();
                    player.damage(RinveniumDamageSources.orchid(player), 1000000);
                }
            }
        });
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
        this.shouldStart = false;
        this.bossBar.clearPlayers();
        this.sync();
    }

    private void sendServerMessageT(MutableText literal) {
        if (player.getServer() != null) {
            player.getServer().getPlayerManager().broadcast(literal, false);
        }
    }
    private void sendServerMessageS(String string) {
        if (player.getServer() != null) {
            player.getServer().getPlayerManager().broadcast(Text.literal(string).formatted(Formatting.RED), false);
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
