package silly.chemthunder.rinvenium.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import org.jetbrains.annotations.Nullable;

public class HailOfTheGodsTrailParticle extends SpriteBillboardParticle {
    private final SpriteProvider spriteProvider;

    public HailOfTheGodsTrailParticle(ClientWorld clientWorld, double x, double y, double z, double velX, double velY, double velZ, SpriteProvider spriteProvider) {
        super(clientWorld, x, y, z, velX, velY, velZ);
        this.maxAge = 5;
        this.velocityMultiplier = 0.0f;
        this.spriteProvider = spriteProvider;
        this.collidesWithWorld = false;
        this.gravityStrength = 0.0f;
        this.scale = 0.1f;
        this.setSprite(spriteProvider);
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public @Nullable Particle createParticle(DefaultParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            HailOfTheGodsTrailParticle particle = new HailOfTheGodsTrailParticle(world, x, y, z, velocityX, velocityY, velocityZ, spriteProvider);
            particle.setVelocity(0, 0, 0);
            return particle;
        }
    }
}
