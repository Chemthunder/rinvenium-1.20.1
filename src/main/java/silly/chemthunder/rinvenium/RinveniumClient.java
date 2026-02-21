package silly.chemthunder.rinvenium;

import net.fabricmc.api.ClientModInitializer;
import silly.chemthunder.rinvenium.index.RinveniumEntities;
import silly.chemthunder.rinvenium.index.RinveniumPackets;

public class RinveniumClient implements ClientModInitializer {
    
    public void onInitializeClient() {
        RinveniumEntities.clientInit();
        RinveniumPackets.registerS2CPackets();
    }
}
