package silly.chemthunder.rinvenium.util.persistent;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;
import silly.chemthunder.rinvenium.util.RinveniumUtil;

import java.util.UUID;

public class DeathSequenceState extends PersistentState {
    public static final String CAN_SEQUENCE = "Can Sequence";
    public static final String PLAYER = "PlayerToDie";
    public static final String SHOULD_START_POST_TICK = "ShouldStartPostTick";
    public static final String POST_TICK = "PostTick";

    public boolean canSequence = false;
    public UUID playerUuid = RinveniumUtil.EMPTY_UUID;
    public boolean shouldStartPostTick = false;
    public int postTick = 0;

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putBoolean(CAN_SEQUENCE, this.canSequence);
        nbt.putUuid(PLAYER, this.playerUuid);
        nbt.putBoolean(SHOULD_START_POST_TICK, this.shouldStartPostTick);
        nbt.putInt(POST_TICK, this.postTick);
        return nbt;
    }

    public static DeathSequenceState createFromNbt(NbtCompound nbt) {
        DeathSequenceState state = new DeathSequenceState();
        state.canSequence = nbt.getBoolean(CAN_SEQUENCE);
        state.playerUuid = nbt.getUuid(PLAYER);
        state.shouldStartPostTick = nbt.getBoolean(SHOULD_START_POST_TICK);
        state.postTick = nbt.getInt(POST_TICK);
        return state;
    }

    public static DeathSequenceState createNew() {
        DeathSequenceState state = new DeathSequenceState();
        state.canSequence = false;
        state.playerUuid = RinveniumUtil.EMPTY_UUID;
        state.shouldStartPostTick = false;
        state.postTick = 0;
        return state;
    }

    public static DeathSequenceState getServerState(MinecraftServer server) {
        ServerWorld serverWorld = server.getWorld(ServerWorld.OVERWORLD);
        assert serverWorld != null;

        DeathSequenceState state = serverWorld.getPersistentStateManager().getOrCreate(DeathSequenceState::createFromNbt, DeathSequenceState::createNew, "envixiaDeathSequence");
        state.markDirty();
        return state;
    }
}