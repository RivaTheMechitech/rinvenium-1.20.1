package silly.chemthunder.rinvenium.index;

import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.client.render.entity.EmptyEntityRenderer;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import silly.chemthunder.rinvenium.Rinvenium;
import silly.chemthunder.rinvenium.entity.AscentaShotEntity;
import silly.chemthunder.rinvenium.entity.CoreChargeGrenadeEntity;
import silly.chemthunder.rinvenium.entity.GunshotEntity;

import java.util.LinkedHashMap;
import java.util.Map;

public interface RinveniumEntities {
    Map<EntityType<? extends Entity>, Identifier> ENTITIES = new LinkedHashMap<>();

    EntityType<AscentaShotEntity> ASCENTA_SHOT = createEntity("ascenta_shot", FabricEntityTypeBuilder.create(SpawnGroup.MISC, AscentaShotEntity::new).disableSaving().dimensions(EntityDimensions.changing(2.0f, 2.0f)).build());
    EntityType<GunshotEntity> GUNSHOT = createEntityVanilla("gunshot", EntityType.Builder.<GunshotEntity>create(GunshotEntity::new, SpawnGroup.MISC).disableSaving().setDimensions(2.0f, 2.0f).trackingTickInterval(40).maxTrackingRange(8).build("gunshot"));
    EntityType<CoreChargeGrenadeEntity> CORE_CHARGE_GRENADE = createEntity("core_charge_grenade", FabricEntityTypeBuilder.create(SpawnGroup.MISC, CoreChargeGrenadeEntity::new).disableSaving().dimensions(EntityDimensions.changing(1.0f, 1.0f)).build());

    private static <T extends EntityType<? extends Entity>> T createEntity(String name, T entity) {
        ENTITIES.put(entity, new Identifier(Rinvenium.MOD_ID, name));
        return entity;
    }

    private static <T extends EntityType<? extends Entity>> T createEntityVanilla(String name, T entity) {
        Identifier id = new Identifier(Rinvenium.MOD_ID, name);
        ENTITIES.put(entity, id);
        return entity;
    }

    static void init() {
        ENTITIES.keySet().forEach(entityType -> Registry.register(Registries.ENTITY_TYPE, ENTITIES.get(entityType), entityType));
    }

    static void clientInit() {
        EntityRendererRegistry.register(ASCENTA_SHOT, EmptyEntityRenderer::new);
        EntityRendererRegistry.register(GUNSHOT, EmptyEntityRenderer::new);
        EntityRendererRegistry.register(CORE_CHARGE_GRENADE, FlyingItemEntityRenderer::new);
    }
}