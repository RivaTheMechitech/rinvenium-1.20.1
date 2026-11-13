package silly.chemthunder.rinvenium.index;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import silly.chemthunder.rinvenium.Rinvenium;
import silly.chemthunder.rinvenium.effect.IonizedStatusEffect;
import silly.chemthunder.rinvenium.effect.MeltedStatusEffect;
import silly.chemthunder.rinvenium.effect.PlasmafiedStatusEffect;
import silly.chemthunder.rinvenium.effect.PublicStatusEffect;
import silly.chemthunder.rinvenium.effect.SparkedStatusEffect;
import silly.chemthunder.rinvenium.item.APMSDCItem;

import java.util.LinkedHashMap;
import java.util.Map;

public interface RinveniumStatusEffects {
    Map<StatusEffect, Identifier> EFFECTS = new LinkedHashMap<>();

    StatusEffect SPARKED = create("sparked", new SparkedStatusEffect(StatusEffectCategory.HARMFUL, 0x87f9ff, false));
    StatusEffect SPARKED_WITH_CD = create("sparked_with_cooldown", new SparkedStatusEffect(StatusEffectCategory.HARMFUL, 0x87f9ff, true));
    StatusEffect SWISS_CHEESE = create("swiss_cheese", new PublicStatusEffect(StatusEffectCategory.NEUTRAL, 0xd6c127));
    StatusEffect WATCHED = create("watched", new PublicStatusEffect(StatusEffectCategory.NEUTRAL, 0x880000));
    StatusEffect IONIZED = create("ionized", new IonizedStatusEffect(StatusEffectCategory.HARMFUL, APMSDCItem.BeamType.DEFAULT.getColor()));
    StatusEffect MELTED = create("melted", new MeltedStatusEffect(StatusEffectCategory.HARMFUL, APMSDCItem.BeamType.LOW_POWER.getColor()));
    StatusEffect PLASMAFIED = create("plasmafied", new PlasmafiedStatusEffect(StatusEffectCategory.HARMFUL, APMSDCItem.BeamType.HIGH_POWER.getColor()));

    static void init() {
        EFFECTS.keySet().forEach(effect -> Registry.register(Registries.STATUS_EFFECT, EFFECTS.get(effect), effect));
    }

    private static <T extends StatusEffect> T create(String name, T effect) {
        EFFECTS.put(effect, Rinvenium.id(name));
        return effect;
    }
}