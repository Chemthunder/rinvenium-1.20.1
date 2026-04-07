package silly.chemthunder.rinvenium.index.client;

import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;
import silly.chemthunder.rinvenium.cca.RinveniumComponents;
import silly.chemthunder.rinvenium.cca.item.SpearTextureItemComponent;
import silly.chemthunder.rinvenium.index.RinveniumItems;
import silly.chemthunder.rinvenium.item.EnviniumSpearItem;
import silly.chemthunder.rinvenium.item.EnvixiaCoreItem;

public class RinveniumModelPredicateProvider {
    public static void registerModelPredicates() {
        ModelPredicateProviderRegistry.register(
                RinveniumItems.ENVIXIA_CORE,
                new Identifier("activated"),
                (stack, world, entity, seed) -> {
                    if (stack.isOf(RinveniumItems.ENVIXIA_CORE) && stack.getItem() instanceof EnvixiaCoreItem coreItem) {
                        return coreItem.isComplete(stack) ? 1.0f : 0.0f;
                    }
                    return 0.0f;
                }
        );
        ModelPredicateProviderRegistry.register(
                RinveniumItems.ENVINIUM_SPEAR,
                new Identifier("custom_spear_texture"),
                (stack, world, entity, seed) -> {
                    if (stack.isOf(RinveniumItems.ENVINIUM_SPEAR)) {
                        SpearTextureItemComponent spearTextureItemComponent = RinveniumComponents.SPEAR_TEXTURE.get(stack);
                        return (float) EnviniumSpearItem.Texture.valueOf(spearTextureItemComponent.getTexture().toUpperCase()).ordinal() / EnviniumSpearItem.Texture.values().length;
                    }
                    return 0.0f;
                }
        );
    }
}