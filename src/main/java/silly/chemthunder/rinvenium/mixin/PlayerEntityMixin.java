package silly.chemthunder.rinvenium.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import silly.chemthunder.rinvenium.cca.entity.SpearParryComponent;
import silly.chemthunder.rinvenium.cca.item.EnviniumSpearItemComponent;
import silly.chemthunder.rinvenium.index.RinveniumEnchantments;
import silly.chemthunder.rinvenium.index.RinveniumItems;
import silly.chemthunder.rinvenium.index.RinveniumStatusEffects;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/player/PlayerEntity;DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V"))
    private void customHitSound(Entity target, CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) ((Object)this);
        if (player.getStackInHand(Hand.MAIN_HAND).isOf(RinveniumItems.ENVINIUM_SPEAR)) {
            SpearParryComponent spearParryComponent = SpearParryComponent.get(player);
            if (spearParryComponent.getDoubleIntValue2() > 0) {
                this.getWorld().playSound(target.getX(), target.getY(), target.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, SoundCategory.PLAYERS, 2.0f, 1.0f, true);
            } else {
                this.getWorld().playSound(target.getX(), target.getY(), target.getZ(), SoundEvents.BLOCK_COPPER_BREAK, SoundCategory.PLAYERS, 1.0f, 1.0f, true);
            }
        }
    }

    @WrapOperation(method = "applyDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;modifyAppliedDamage(Lnet/minecraft/entity/damage/DamageSource;F)F"))
    private float rinvenium$handleParry(PlayerEntity player, DamageSource source, float amount, Operation<Float> original) {
        float base = original.call(player, source, amount);
        if (player.getStackInHand(Hand.MAIN_HAND).isOf(RinveniumItems.ENVINIUM_SPEAR)) {
            SpearParryComponent spearParryComponent = SpearParryComponent.get(player);
            if (!source.isIn(DamageTypeTags.BYPASSES_EFFECTS)) {
                if (spearParryComponent.getDoubleBoolValue2() && spearParryComponent.getDoubleBoolValue1()) {
                    Vec3d damagePos = source.getPosition();
                    Vec3d rotVec;
                    Vec3d difference;
                    double angle;
                    ServerWorld serverWorld;
                    World var13;
                    if (damagePos != null) {
                        rotVec = this.getRotationVector();
                        difference = damagePos.relativize(this.getEyePos()).normalize();
                        angle = difference.dotProduct(rotVec);
                        if (angle < -1.0 || angle > 1.0) {
                            return base;
                        }
                        if (angle < -0.35) {
                            var13 = this.getEntityWorld();
                            if (var13 instanceof ServerWorld) {
                                serverWorld = (ServerWorld) var13;
                                serverWorld.playSoundFromEntity(null, this, SoundEvents.BLOCK_ANVIL_PLACE, SoundCategory.PLAYERS, 0.25f, 0.70f + this.getEntityWorld().random.nextFloat() * 0.2f);
                            }
                            spearParryComponent.setDoubleIntValue2(SpearParryComponent.MAX_DAMAGE_WINDOW);
                            spearParryComponent.setDoubleBoolValue1(false);
                            if (source.getAttacker() instanceof LivingEntity livingEntity) {
                                Vec3d launchVec = rotVec;
                                launchVec = launchVec.add(new Vec3d(0.0, 0.7, 0.0));
                                launchVec = launchVec.normalize();
                                livingEntity.setVelocity(launchVec.multiply(1.5));
                                livingEntity.velocityDirty = true;
                            }
                            return 0.0f;
                        }
                    }
                }
            }
        }
        if (source.getAttacker() instanceof PlayerEntity attacker && attacker.getStackInHand(Hand.MAIN_HAND).isOf(RinveniumItems.ENVINIUM_SPEAR)) {
            SpearParryComponent attackerSpearParryComponent = SpearParryComponent.get(attacker);
            if (attackerSpearParryComponent.getDoubleIntValue2() > 0) {
                attackerSpearParryComponent.setDoubleIntValue2(0);
                attackerSpearParryComponent.setDoubleBoolValue1(true);
                return base * 1.5f;
            }
        }
        return base;
    }

    @Override
    public void onPlayerCollision(PlayerEntity player) {
        if (this.isWet() && player.isWet() && !player.getUuid().toString().equals("4a58221e-97d0-40cd-9425-01e0ff15ecea")) {
            player.addStatusEffect(new StatusEffectInstance(RinveniumStatusEffects.SPARKED, 10, 0));
        }
        super.onPlayerCollision(player);
    }
}
