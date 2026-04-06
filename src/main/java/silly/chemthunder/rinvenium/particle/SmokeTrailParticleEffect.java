package silly.chemthunder.rinvenium.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.dynamic.Codecs;
import silly.chemthunder.rinvenium.index.RinveniumParticles;

public class SmokeTrailParticleEffect extends AgeVaryingParticleEffect {
    public static final int TWO_SECONDS = 40;
    public static final SmokeTrailParticleEffect DEFAULT = new SmokeTrailParticleEffect(TWO_SECONDS);

    public static final Codec<SmokeTrailParticleEffect> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codecs.NONNEGATIVE_INT.fieldOf("max_age").forGetter(smokeTrailParticle -> smokeTrailParticle.maxAge)
                    ).apply(instance, SmokeTrailParticleEffect::new)
    );

    public static final ParticleEffect.Factory<SmokeTrailParticleEffect> PARAMETER_FACTORY = new ParticleEffect.Factory<SmokeTrailParticleEffect>() {
        @Override
        public SmokeTrailParticleEffect read(ParticleType<SmokeTrailParticleEffect> type, StringReader reader) throws CommandSyntaxException {
            return new SmokeTrailParticleEffect(reader.readInt());
        }

        @Override
        public SmokeTrailParticleEffect read(ParticleType<SmokeTrailParticleEffect> type, PacketByteBuf buf) {
            return new SmokeTrailParticleEffect(buf.readInt());
        }
    };

    public SmokeTrailParticleEffect(int maxAge) {
        super(maxAge);
    }

    @Override
    public ParticleType<SmokeTrailParticleEffect> getType() {
        return RinveniumParticles.SMOKE_TRAIL;
    }
}