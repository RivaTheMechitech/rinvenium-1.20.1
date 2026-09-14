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
import silly.chemthunder.rinvenium.cca.entity.*;
import silly.chemthunder.rinvenium.cca.entity.riva.AscensionPlayerComponent;
import silly.chemthunder.rinvenium.cca.entity.riva.SpearHealComponent;
import silly.chemthunder.rinvenium.cca.item.APMDSCItemComponent;
import silly.chemthunder.rinvenium.cca.item.EnviniumSpearItemComponent;
import silly.chemthunder.rinvenium.cca.item.SpearTextureItemComponent;
import silly.chemthunder.rinvenium.item.APMDSCItem;
import silly.chemthunder.rinvenium.item.EnviniumSpearItem;

public class RinveniumComponents implements ItemComponentInitializer, EntityComponentInitializer {
    public static final ComponentKey<SpearParryComponent> SPEAR_PARRY = ComponentRegistry.getOrCreate(Rinvenium.id("spear_parry"), SpearParryComponent.class);
    public static final ComponentKey<HailOfTheGodComponent> HAIL_OF_THE_GODS = ComponentRegistry.getOrCreate(Rinvenium.id("hail_of_the_gods"), HailOfTheGodComponent.class);
    public static final ComponentKey<EnvixiaFormComponent> ENVIXIA_FORM = ComponentRegistry.getOrCreate(Rinvenium.id("envixia_form"), EnvixiaFormComponent.class);
    public static final ComponentKey<SpearHealComponent> SPEAR_HEAL = ComponentRegistry.getOrCreate(Rinvenium.id("spear_heal"), SpearHealComponent.class);
    public static final ComponentKey<DeathSequenceComponent> DEATH_SEQUENCE = ComponentRegistry.getOrCreate(Rinvenium.id("death_sequence"), DeathSequenceComponent.class);
    public static final ComponentKey<APMDSCComponent> APMDSC_ENTITY = ComponentRegistry.getOrCreate(Rinvenium.id("apmdsc_entity"), APMDSCComponent.class);

    public static final ComponentKey<SpearTextureItemComponent> SPEAR_TEXTURE = ComponentRegistry.getOrCreate(Rinvenium.id("spear_texture"), SpearTextureItemComponent.class);
    public static final ComponentKey<APMDSCItemComponent> APMDSC_ITEM = ComponentRegistry.getOrCreate(Rinvenium.id("apmdsc_item"), APMDSCItemComponent.class);

    public void registerItemComponentFactories(ItemComponentFactoryRegistry registry) {
        registry.register(item -> item instanceof EnviniumSpearItem, EnviniumSpearItemComponent.KEY, EnviniumSpearItemComponent::new);
        registry.register(item -> item instanceof EnviniumSpearItem, SPEAR_TEXTURE, SpearTextureItemComponent::new);
        registry.register(item -> item instanceof APMDSCItem, APMDSC_ITEM, APMDSCItemComponent::new);
    }

    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.beginRegistration(PlayerEntity.class, SPEAR_PARRY).respawnStrategy(RespawnCopyStrategy.NEVER_COPY).end(SpearParryComponent::new);
        registry.beginRegistration(PlayerEntity.class, HAIL_OF_THE_GODS).respawnStrategy(RespawnCopyStrategy.NEVER_COPY).end(HailOfTheGodComponent::new);
        registry.beginRegistration(PlayerEntity.class, SpearDashingComponent.KEY).respawnStrategy(RespawnCopyStrategy.NEVER_COPY).end(SpearDashingComponent::new);
        registry.beginRegistration(PlayerEntity.class, AscensionPlayerComponent.KEY).respawnStrategy(RespawnCopyStrategy.NEVER_COPY).end(AscensionPlayerComponent::new);
        registry.beginRegistration(PlayerEntity.class, ENVIXIA_FORM).respawnStrategy(RespawnCopyStrategy.NEVER_COPY).end(EnvixiaFormComponent::new);
        registry.beginRegistration(PlayerEntity.class, SPEAR_HEAL).respawnStrategy(RespawnCopyStrategy.NEVER_COPY).end(SpearHealComponent::new);
        registry.beginRegistration(PlayerEntity.class, DEATH_SEQUENCE).respawnStrategy(RespawnCopyStrategy.NEVER_COPY).end(DeathSequenceComponent::new);
        registry.beginRegistration(PlayerEntity.class, APMDSC_ENTITY).respawnStrategy(RespawnCopyStrategy.NEVER_COPY).end(APMDSCComponent::new);
    }
}