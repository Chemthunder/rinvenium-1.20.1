package silly.chemthunder.rinvenium.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import silly.chemthunder.rinvenium.Rinvenium;
import silly.chemthunder.rinvenium.index.RinveniumDamageSources;

public class SparkedStatusEffect extends StatusEffect {
    public SparkedStatusEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (Rinvenium.haters.contains(entity.getUuid())) {
            entity.damage(RinveniumDamageSources.niki(entity), 6.0f);
        } else {
            entity.damage(RinveniumDamageSources.electricity(entity), 0.5f);
        }
        super.applyUpdateEffect(entity, amplifier);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        int i = 2;
        return duration % i == 0;
    }
}
