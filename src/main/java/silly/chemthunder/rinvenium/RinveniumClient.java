package silly.chemthunder.rinvenium;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import silly.chemthunder.rinvenium.entity.client.EnvixiaArmorRenderer;
import silly.chemthunder.rinvenium.entity.client.EnvixiaArmorModel;
import silly.chemthunder.rinvenium.index.RinveniumEntities;
import silly.chemthunder.rinvenium.index.RinveniumItems;
import silly.chemthunder.rinvenium.index.RinveniumPackets;
import silly.chemthunder.rinvenium.index.RinveniumParticles;
import silly.chemthunder.rinvenium.index.client.RinveniumEntityModelLayers;

public class RinveniumClient implements ClientModInitializer {
    
    public void onInitializeClient() {
        RinveniumEntities.clientInit();
        RinveniumPackets.registerS2CPackets();
        RinveniumParticles.clientInit();

        EntityModelLayerRegistry.registerModelLayer(EnvixiaArmorRenderer.MODEL_LAYER, EnvixiaArmorModel::getTexturedModelData);
        ArmorRenderer.register(new EnvixiaArmorRenderer(), RinveniumItems.ENVIXIA_HELMET, RinveniumItems.ENVIXIA_CHESTPLATE, RinveniumItems.ENVIXIA_LEGGINGS, RinveniumItems.ENVIXIA_BOOTS);
    }
}
