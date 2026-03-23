package silly.chemthunder.rinvenium.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import silly.chemthunder.rinvenium.Rinvenium;
import silly.chemthunder.rinvenium.index.RinveniumDamageSources;

public class SparkedStatusEffect extends StatusEffect {
    private final boolean hasCooldown;
    public SparkedStatusEffect(StatusEffectCategory category, int color, boolean hasCooldown) {
        super(category, color);
        this.hasCooldown = hasCooldown;
    }

    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (Rinvenium.haters.contains(entity.getUuid())) {
            entity.damage(RinveniumDamageSources.niki(entity), 6.0f);
        } else {
            if (entity.getAttacker() != null) {
                if (this.hasCooldown) {
                    entity.damage(RinveniumDamageSources.electricityWithCD(entity, entity.getAttacker()), 0.5f);
                } else {
                    entity.damage(RinveniumDamageSources.electricity(entity, entity.getAttacker()), 0.5f);
                }
            } else {
                if (this.hasCooldown) {
                    entity.damage(RinveniumDamageSources.electricityWithCD(entity), 0.5f);
                } else {
                    entity.damage(RinveniumDamageSources.electricity(entity), 0.5f);
                }
            }
        }
        super.applyUpdateEffect(entity, amplifier);
    }

    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        int i = 10;
        return duration % i == 0;
    }
}
