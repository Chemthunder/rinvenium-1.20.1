package silly.chemthunder.rinvenium.index;

import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.client.render.entity.EmptyEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import silly.chemthunder.rinvenium.Rinvenium;
import silly.chemthunder.rinvenium.entity.AscentaShotEntity;

import java.util.LinkedHashMap;
import java.util.Map;

public interface RinveniumEntities {
    Map<EntityType<? extends Entity>, Identifier> ENTITIES = new LinkedHashMap<>();

    //  Item WEAPON_RACK = create("weapon_rack", new WeaponRackItem(new Item.Settings()));

    EntityType<AscentaShotEntity> ASCENTA_SHOT = createEntity("ascenta_shot", FabricEntityTypeBuilder.create(SpawnGroup.MISC, AscentaShotEntity::new).disableSaving().dimensions(EntityDimensions.changing(2.0f, 2.0f)).build());

    private static <T extends EntityType<? extends Entity>> T createEntity(String name, T entity) {
        ENTITIES.put(entity, new Identifier(Rinvenium.MOD_ID, name));
        return entity;
    }

    static void index() {
        ENTITIES.keySet().forEach(entityType -> Registry.register(Registries.ENTITY_TYPE, ENTITIES.get(entityType), entityType));
    }

    static void clientIndex() {
        EntityRendererRegistry.register(ASCENTA_SHOT, EmptyEntityRenderer::new);
    }
}
