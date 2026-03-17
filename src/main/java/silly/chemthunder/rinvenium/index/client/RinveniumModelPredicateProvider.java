package silly.chemthunder.rinvenium.index.client;

import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;
import silly.chemthunder.rinvenium.index.RinveniumItems;
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
    }
}
