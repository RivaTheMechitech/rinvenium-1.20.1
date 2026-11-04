package silly.chemthunder.rinvenium.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectCategory;
import silly.chemthunder.rinvenium.index.RinveniumDamageSources;

public class IonizedStatusEffect extends PublicStatusEffect{
    public IonizedStatusEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        entity.damage(RinveniumDamageSources.ionized(entity), 0.15f);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}
