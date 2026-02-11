package silly.chemthunder.rinvenium.cca.entity;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import silly.chemthunder.rinvenium.Rinvenium;

public class SpearDashingComponent implements AutoSyncedComponent, CommonTickingComponent {
    public static final ComponentKey<SpearDashingComponent> KEY = ComponentRegistry.getOrCreate(Rinvenium.id("dash"), SpearDashingComponent.class);
    public int dashTicks = 0;
    private final PlayerEntity player;
    
    public void sync() {
        KEY.sync(this.player);
    }
    public SpearDashingComponent(PlayerEntity player) {
        this.player = player;
    }

    public void tick() {
        if (dashTicks > 0) {
            dashTicks--;
            spawnParticles(player, player.getWorld());
            if (dashTicks == 0) {
                sync();
            }
        }
    }

    private void spawnParticles(PlayerEntity player, World world) {
        double x = player.getX();
        double y = player.getY();
        double z = player.getZ();

        if (world instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(ParticleTypes.ELECTRIC_SPARK,
                    x,
                    y + 0.1f,
                    z,
                    2,
                    0.08,
                    0.08,
                    0.08,
                    0.09
            );

            serverWorld.spawnParticles(ParticleTypes.END_ROD,
                    x,
                    y + 0.1f,
                    z,
                    2,
                    0.08,
                    0.08,
                    0.08,
                    0.09
            );
        }
    }

    @Override
    public void readFromNbt(NbtCompound nbtCompound) {
        this.dashTicks = nbtCompound.getInt("dashTicks");
    }

    @Override
    public void writeToNbt(NbtCompound nbtCompound) {
        nbtCompound.putInt("dashTicks", dashTicks);
    }
}
