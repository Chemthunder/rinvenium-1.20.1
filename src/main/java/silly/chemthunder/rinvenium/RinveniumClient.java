package silly.chemthunder.rinvenium;

import net.fabricmc.api.ClientModInitializer;
import silly.chemthunder.rinvenium.index.RinveniumEntities;

public class RinveniumClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        RinveniumEntities.clientIndex();
    }
}
