package silly.chemthunder.rinvenium.index;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import silly.chemthunder.rinvenium.Rinvenium;
import silly.chemthunder.rinvenium.effect.SparkedStatusEffect;

import java.util.LinkedHashMap;
import java.util.Map;

public interface RinveniumStatusEffects {
    Map<StatusEffect, Identifier> EFFECTS = new LinkedHashMap<>();

    StatusEffect SPARKED = create("sparked", new SparkedStatusEffect(StatusEffectCategory.HARMFUL, 0x87f9ff));

    static void init() {
        EFFECTS.keySet().forEach(effect -> Registry.register(Registries.STATUS_EFFECT, EFFECTS.get(effect), effect));
    }

    private static <T extends StatusEffect> T create(String name, T effect) {
        EFFECTS.put(effect, Rinvenium.id(name));
        return effect;
    }
}
