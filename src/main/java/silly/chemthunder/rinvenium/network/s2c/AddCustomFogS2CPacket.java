package silly.chemthunder.rinvenium.network.s2c;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import silly.chemthunder.rinvenium.render.CustomFog;
import silly.chemthunder.rinvenium.render.manager.global.CustomFogManager;
import silly.chemthunder.rinvenium.util.inject.RenderContainer;

public class AddCustomFogS2CPacket {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        if (client.world != null && client.world.getEntities() != null) {
            if (client.player != null) {
                float red = buf.readFloat();
                float green = buf.readFloat();
                float blue = buf.readFloat();
                int maxAge = buf.readInt();
            }
        }
    }
}
