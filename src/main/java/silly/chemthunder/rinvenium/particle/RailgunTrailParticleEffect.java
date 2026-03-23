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

public class RailgunTrailParticleEffect extends AgeVaryingParticleEffect {
    public static final int TWO_SECONDS = 40;
    public static final RailgunTrailParticleEffect DEFAULT = new RailgunTrailParticleEffect(TWO_SECONDS);
    public static final Codec<RailgunTrailParticleEffect> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codecs.NONNEGATIVE_INT.fieldOf("max_age").forGetter(railgunTrailParticleEffect -> railgunTrailParticleEffect.maxAge)
                    ).apply(instance, RailgunTrailParticleEffect::new)
    );
    public static final ParticleEffect.Factory<RailgunTrailParticleEffect> PARAMETER_FACTORY = new ParticleEffect.Factory<RailgunTrailParticleEffect>() {
        @Override
        public RailgunTrailParticleEffect read(ParticleType<RailgunTrailParticleEffect> type, StringReader reader) throws CommandSyntaxException {
            return new RailgunTrailParticleEffect(reader.readInt());
        }

        @Override
        public RailgunTrailParticleEffect read(ParticleType<RailgunTrailParticleEffect> type, PacketByteBuf buf) {
            return new RailgunTrailParticleEffect(buf.readInt());
        }
    };

    public RailgunTrailParticleEffect(int maxAge) {
        super(maxAge);
    }

    @Override
    public ParticleType<RailgunTrailParticleEffect> getType() {
        return RinveniumParticles.RAILGUN_TRAIL;
    }

}
