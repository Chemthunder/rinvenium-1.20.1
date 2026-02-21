package silly.chemthunder.rinvenium.network.s2c;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.particle.Particle;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.DyeColor;

public class SpawnFlashParticleS2CPacket {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        double x = buf.readDouble();
        double y = buf.readDouble();
        double z = buf.readDouble();
        int k = DyeColor.YELLOW.getFireworkColor();
        float f = ((k & 0xFF0000) >> 16) / 255.0F;
        float g = ((k & 0xFF00) >> 8) / 255.0F;
        float h = ((k & 0xFF) >> 0) / 255.0F;
        Particle particle = client.particleManager.createParticle(ParticleTypes.FLASH, x, y, z, 0.0, 0.0, 0.0);
        particle.setColor(f, g, h);
        particle.scale(0.01f);
        client.particleManager.addParticle(particle);
    }
}
