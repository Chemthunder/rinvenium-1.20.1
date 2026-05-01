package silly.chemthunder.rinvenium.index;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import silly.chemthunder.rinvenium.Rinvenium;
import silly.chemthunder.rinvenium.network.s2c.AddCustomFogS2CPacket;
import silly.chemthunder.rinvenium.network.s2c.AddImpactFrameS2CPacket;
import silly.chemthunder.rinvenium.network.s2c.AddScreenFlashS2CPacket;
import silly.chemthunder.rinvenium.network.s2c.SpawnFlashParticleS2CPacket;

public class RinveniumPackets {
    /**C2S Packets*/
    /*public static final Identifier DUMMY = createC2SId("dummy");*/

    public static void registerC2SPackets() {

    }

    /**S2C Packets*/
    public static final Identifier FLASH_PARTICLE = createS2CId("flash_particle");
    public static final Identifier ADD_SCREEN_FLASH = createS2CId("add_screen_flash");
    public static final Identifier ADD_IMPACT_FRAME = createS2CId("add_impact_frame");
    public static final Identifier ADD_CUSTOM_FOG = createS2CId("add_custom_fog");

    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(FLASH_PARTICLE, SpawnFlashParticleS2CPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(ADD_SCREEN_FLASH, AddScreenFlashS2CPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(ADD_IMPACT_FRAME, AddImpactFrameS2CPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(ADD_CUSTOM_FOG, AddCustomFogS2CPacket::receive);
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

    public static void sendImpactFrame(ServerPlayerEntity player, int time) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(time);
        buf.writeInt(0xFFFFFF);
        buf.writeInt(1);
        buf.writeFloat(1.0f);
        buf.writeDouble(player.getX());
        buf.writeDouble(player.getY());
        buf.writeDouble(player.getZ());
        buf.writeUuid(player.getUuid());

        ServerPlayNetworking.send(player, RinveniumPackets.ADD_IMPACT_FRAME, buf);
    }
}