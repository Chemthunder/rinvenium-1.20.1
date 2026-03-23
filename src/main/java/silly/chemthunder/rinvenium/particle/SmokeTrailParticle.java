package silly.chemthunder.rinvenium.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class SmokeTrailParticle<T extends AgeVaryingParticleEffect> extends SpriteBillboardParticle {
    private final SpriteProvider spriteProvider;

    public SmokeTrailParticle(ClientWorld clientWorld, double x, double y, double z, double velX, double velY, double velZ, T parameters, SpriteProvider spriteProvider) {
        super(clientWorld, x, y, z, velX, velY, velZ);
        this.velocityMultiplier = 0.0f;
        this.spriteProvider = spriteProvider;
        this.velocityX = 0;
        this.velocityY = 0;
        this.velocityZ = 0;
        this.maxAge = parameters.getMaxAge();
        this.setSpriteForAge(spriteProvider);
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        super.tick();
        this.setSpriteForAge(this.spriteProvider);
    }

    @Environment(EnvType.CLIENT)
    public static class Default extends SmokeTrailParticle<SmokeTrailParticleEffect> {
        public Default(ClientWorld clientWorld, double d, double e, double f, double g, double h, double i, SmokeTrailParticleEffect parameters, SpriteProvider spriteProvider) {
            super(clientWorld, d, e, f, g, h, i, parameters, spriteProvider);
        }

        @Environment(EnvType.CLIENT)
        public static class Factory implements ParticleFactory<SmokeTrailParticleEffect> {
            private final SpriteProvider spriteProvider;

            public Factory(SpriteProvider spriteProvider) {
                this.spriteProvider = spriteProvider;
            }

            @Override
            public @Nullable Particle createParticle(SmokeTrailParticleEffect parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
                return new Default(world, x, y, z, velocityX, velocityY, velocityZ, parameters, this.spriteProvider);
            }
        }
    }
}
