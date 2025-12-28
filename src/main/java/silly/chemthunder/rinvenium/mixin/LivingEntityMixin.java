package silly.chemthunder.rinvenium.mixin;

import net.minecraft.client.particle.Particle;
import net.minecraft.entity.Attackable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import silly.chemthunder.rinvenium.index.RinveniumItems;
import silly.chemthunder.rinvenium.index.RinveniumStatusEffects;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Attackable {
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "attackLivingEntity", at = @At("HEAD"))
    private void rin$parry(LivingEntity target, CallbackInfo ci) {
        LivingEntity me = (LivingEntity) (Object) this;

        if (target instanceof PlayerEntity player) {
            if (player.getMainHandStack().isOf(RinveniumItems.ENVINIUM_SPEAR) && player.isUsingItem()) {
                triggerSillies(me, me.getWorld());
            }
        }
    }

    @Unique
    public void triggerSillies(LivingEntity me, World world) {
        double x = me.getX();
        double y = me.getY();
        double z = me.getZ();

        me.damage(me.getDamageSources().magic(), 5.0f);
        me.addStatusEffect(new StatusEffectInstance(RinveniumStatusEffects.SPARKED, 600));

        if (world instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(ParticleTypes.END_ROD,
                    x, y, z,
                    15,
                    0.01,
                    0.01,
                    0.01,
                    0.3
            );
        }
    }
}
