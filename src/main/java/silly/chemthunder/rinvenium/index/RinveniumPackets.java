package silly.chemthunder.rinvenium.index;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Identifier;
import silly.chemthunder.rinvenium.Rinvenium;
import silly.chemthunder.rinvenium.network.s2c.SpawnFlashParticleS2CPacket;

public class RinveniumPackets {
    /**C2S Packets*/
    /*public static final Identifier DUMMY = createC2SId("dummy");*/

    public static void registerC2SPackets() {

    }

    /**S2C Packets*/
    public static final Identifier FLASH_PARTICLE = createS2CId("flash_particle");

    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(FLASH_PARTICLE, SpawnFlashParticleS2CPacket::receive);
    }

    public static Identifier createC2SId(String name) {
        return Rinvenium.id(name + "_c2s_packet");
    }
    public static Identifier createS2CId(String name) {
        return Rinvenium.id(name + "_s2c_packet");
    }

    public static void init() {
        Rinvenium.LOGGER.info("Registering Rinvenium Packets");
    }
}
