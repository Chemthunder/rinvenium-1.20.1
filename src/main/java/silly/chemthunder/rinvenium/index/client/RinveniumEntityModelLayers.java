package silly.chemthunder.rinvenium.index.client;

import com.google.common.collect.Sets;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import silly.chemthunder.rinvenium.Rinvenium;

import java.util.Set;

@Environment(EnvType.CLIENT)
public class RinveniumEntityModelLayers {
    private static final Set<EntityModelLayer> LAYERS = Sets.newHashSet();

    public static final EntityModelLayer ENVIXIA_ARMOR = registerMain("envixia_armor");

    public static EntityModelLayer register(String name, String layer) {
        EntityModelLayer entityModelLayer = new EntityModelLayer(Rinvenium.id(name), layer);
        if (!LAYERS.add(entityModelLayer)) {
            throw new IllegalStateException("Duplicate registration for " + entityModelLayer);
        } else {
            return entityModelLayer;
        }
    }
    public static EntityModelLayer registerMain(String name) {
        return register(name, "main");
    }
}
