package silly.chemthunder.rinvenium.index;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import org.jetbrains.annotations.Nullable;
import silly.chemthunder.rinvenium.Rinvenium;

public interface RinveniumDamageSources {
    RegistryKey<DamageType> BAP = of("bap");
    static DamageSource bap(Entity entity) {
        return entity.getDamageSources().create(BAP); }

    RegistryKey<DamageType> BOOP = of("boop");
    static DamageSource boop(Entity entity) {
        return entity.getDamageSources().create(BOOP); }
    static DamageSource boop(Entity entity, @Nullable Entity attacker) {
        if (attacker != null && attacker instanceof LivingEntity livingEntity) {
            return entity.getDamageSources().create(BOOP, livingEntity);
        } else {
            return boop(entity);
        }
    }

    RegistryKey<DamageType> ELECTRICITY = of("electricity");
    static DamageSource electricity(LivingEntity entity) {
        return entity.getDamageSources().create(ELECTRICITY);
    }
    static DamageSource electricity(LivingEntity entity, LivingEntity attacker) {
        return entity.getDamageSources().create(ELECTRICITY, attacker);
    }

    RegistryKey<DamageType> ELECTRICITY_WITH_CD = of("electricity_with_cooldown");
    static DamageSource electricityWithCD(LivingEntity entity) {
        return entity.getDamageSources().create(ELECTRICITY);
    }

    static DamageSource electricityWithCD(LivingEntity entity, LivingEntity attacker) {
        return entity.getDamageSources().create(ELECTRICITY, attacker);
    }

    RegistryKey<DamageType> NIKI = of("niki");
    static DamageSource niki(LivingEntity entity) {
        return entity.getDamageSources().create(NIKI); }

    RegistryKey<DamageType> ORCHID = of("orchid");
    static DamageSource orchid(LivingEntity entity) {
        return entity.getDamageSources().create(ORCHID);
    }

    RegistryKey<DamageType> IONIZED = of("ionized");
    static DamageSource ionized(LivingEntity entity) {
        return entity.getDamageSources().create(IONIZED);
    }
    static DamageSource ionized(LivingEntity entity, LivingEntity attacker) {
        return entity.getDamageSources().create(IONIZED, attacker);
    }


    private static RegistryKey<DamageType> of(String name) {
        return RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Rinvenium.id(name));
    }
}