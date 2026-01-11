package silly.chemthunder.rinvenium.index;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import silly.chemthunder.rinvenium.Rinvenium;

public interface RinveniumDamageSources {
    RegistryKey<DamageType> BAP = of("bap");
    static DamageSource bap(Entity entity) {
        return entity.getDamageSources().create(BAP); }

    RegistryKey<DamageType> BOOP = of("boop");
    static DamageSource boop(Entity entity) {
        return entity.getDamageSources().create(BOOP); }

    RegistryKey<DamageType> ELECTRICITY = of("electricity");
    static DamageSource electricity(LivingEntity entity) {
        return entity.getDamageSources().create(ELECTRICITY); }

    RegistryKey<DamageType> NIKI = of("niki");
    static DamageSource niki(LivingEntity entity) {
        return entity.getDamageSources().create(NIKI); }

    private static RegistryKey<DamageType> of(String name) {
        return RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Rinvenium.id(name));
    }
}
