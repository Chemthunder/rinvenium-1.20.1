package silly.chemthunder.rinvenium.cca;

import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import dev.onyxstudios.cca.api.v3.item.ItemComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.item.ItemComponentInitializer;
import net.minecraft.entity.player.PlayerEntity;
import silly.chemthunder.rinvenium.cca.entity.SpearDashingComponent;
import silly.chemthunder.rinvenium.cca.entity.riva.AscensionPlayerComponent;
import silly.chemthunder.rinvenium.cca.item.EnviniumSpearItemComponent;
import silly.chemthunder.rinvenium.item.EnviniumSpearItem;

public class RinveniumComponents implements ItemComponentInitializer, EntityComponentInitializer {
    @Override
    public void registerItemComponentFactories(ItemComponentFactoryRegistry registry) {
        registry.register(item -> item instanceof EnviniumSpearItem, EnviniumSpearItemComponent.KEY, EnviniumSpearItemComponent::new);
    }

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.beginRegistration(PlayerEntity.class, SpearDashingComponent.KEY).respawnStrategy(RespawnCopyStrategy.NEVER_COPY).end(SpearDashingComponent::new);
        registry.beginRegistration(PlayerEntity.class, AscensionPlayerComponent.KEY).respawnStrategy(RespawnCopyStrategy.NEVER_COPY).end(AscensionPlayerComponent::new);
    }
}
