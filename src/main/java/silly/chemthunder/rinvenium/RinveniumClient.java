package silly.chemthunder.rinvenium;

import net.fabricmc.api.ClientModInitializer;
import silly.chemthunder.rinvenium.index.RinveniumEntities;

public class RinveniumClient implements ClientModInitializer {
    
    public void onInitializeClient() {
        RinveniumEntities.clientInit();
    }
}
