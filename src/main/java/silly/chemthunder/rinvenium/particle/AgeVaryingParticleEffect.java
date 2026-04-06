package silly.chemthunder.rinvenium.particle;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.registry.Registries;

import java.util.Locale;

public abstract class AgeVaryingParticleEffect implements ParticleEffect {
    public final int maxAge;

    public AgeVaryingParticleEffect(int maxAge) {
        this.maxAge = maxAge;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeInt(this.maxAge);
    }

    @Override
    public String asString() {
        return String.format(Locale.ROOT, "%s", Registries.PARTICLE_TYPE.getId(this.getType()));
    }

    public int getMaxAge() {
        return this.maxAge;
    }
}