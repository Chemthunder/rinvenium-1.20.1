package silly.chemthunder.rinvenium.cca;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import dev.onyxstudios.cca.api.v3.item.ItemComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.item.ItemComponentInitializer;
import net.minecraft.entity.player.PlayerEntity;
import silly.chemthunder.rinvenium.Rinvenium;
import silly.chemthunder.rinvenium.cca.entity.HailOfTheGodComponent;
import silly.chemthunder.rinvenium.cca.entity.SpearDashingComponent;
import silly.chemthunder.rinvenium.cca.entity.SpearParryComponent;
import silly.chemthunder.rinvenium.cca.entity.riva.AscensionPlayerComponent;
import silly.chemthunder.rinvenium.cca.item.EnviniumSpearItemComponent;
import silly.chemthunder.rinvenium.item.EnviniumSpearItem;

public class RinveniumComponents implements ItemComponentInitializer, EntityComponentInitializer {
    public static final ComponentKey<SpearParryComponent> SPEAR_PARRY = ComponentRegistry.getOrCreate(Rinvenium.id("spear_parry"), SpearParryComponent.class);
    public static final ComponentKey<HailOfTheGodComponent> HAIL_OF_THE_GODS = ComponentRegistry.getOrCreate(Rinvenium.id("hail_of_the_gods"), HailOfTheGodComponent.class);
    // either move to SpearParryComponent.class or move all Component Keys here for consistency :p
    // i like to have all the keys here

    public void registerItemComponentFactories(ItemComponentFactoryRegistry registry) {
        registry.register(item -> item instanceof EnviniumSpearItem, EnviniumSpearItemComponent.KEY, EnviniumSpearItemComponent::new);
    }

    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.beginRegistration(PlayerEntity.class, SPEAR_PARRY).respawnStrategy(RespawnCopyStrategy.NEVER_COPY).end(SpearParryComponent::new);
        registry.beginRegistration(PlayerEntity.class, HAIL_OF_THE_GODS).respawnStrategy(RespawnCopyStrategy.NEVER_COPY).end(HailOfTheGodComponent::new);
        registry.beginRegistration(PlayerEntity.class, SpearDashingComponent.KEY).respawnStrategy(RespawnCopyStrategy.NEVER_COPY).end(SpearDashingComponent::new);
        registry.beginRegistration(PlayerEntity.class, AscensionPlayerComponent.KEY).respawnStrategy(RespawnCopyStrategy.NEVER_COPY).end(AscensionPlayerComponent::new);
    }

}
