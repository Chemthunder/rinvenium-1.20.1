package silly.chemthunder.rinvenium;

import net.fabricmc.api.ClientModInitializer;
import silly.chemthunder.rinvenium.index.RinveniumEntities;
import silly.chemthunder.rinvenium.index.RinveniumPackets;
import silly.chemthunder.rinvenium.index.RinveniumParticles;

public class RinveniumClient implements ClientModInitializer {
    
    public void onInitializeClient() {
        RinveniumEntities.clientInit();
        RinveniumPackets.registerS2CPackets();
        RinveniumParticles.clientInit();
    }
}
