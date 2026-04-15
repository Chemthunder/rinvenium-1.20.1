package silly.chemthunder.rinvenium.util.persistent;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;

public class DeathSequenceState extends PersistentState {
    public static final String CAN_SEQUENCE = "Can Sequence";

    public boolean canSequence = false;

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putBoolean(CAN_SEQUENCE, this.canSequence);
        return nbt;
    }

    public static DeathSequenceState createFromNbt(NbtCompound nbt) {
        DeathSequenceState state = new DeathSequenceState();
        state.canSequence = nbt.getBoolean(CAN_SEQUENCE);
        return state;
    }

    public static DeathSequenceState createNew() {
        DeathSequenceState state = new DeathSequenceState();
        state.canSequence = false;
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