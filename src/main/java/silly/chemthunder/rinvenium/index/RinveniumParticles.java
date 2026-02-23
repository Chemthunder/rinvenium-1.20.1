package silly.chemthunder.rinvenium.index;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import silly.chemthunder.rinvenium.Rinvenium;
import silly.chemthunder.rinvenium.particle.HailOfTheGodsTrailParticle;

public class RinveniumParticles {
    public static final DefaultParticleType HAIL_OF_THE_GODS_TRAIL = register("hail_of_the_gods_trail", true);
    public static final DefaultParticleType HAIL_OF_THE_GODS_SMOKE = register("hail_of_the_gods_smoke", true);

    private static DefaultParticleType register(String name, boolean alwaysShow) {
        return Registry.register(Registries.PARTICLE_TYPE, Rinvenium.id(name), FabricParticleTypes.simple(alwaysShow));
    }

    @Environment(EnvType.CLIENT)
    public static void clientInit() {
        ParticleFactoryRegistry.getInstance().register(HAIL_OF_THE_GODS_TRAIL, HailOfTheGodsTrailParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(HAIL_OF_THE_GODS_SMOKE, HailOfTheGodsTrailParticle.Factory::new);
    }

    public static void init() {
        Rinvenium.LOGGER.info("Registering Rinvenium Particles");
    }
}
