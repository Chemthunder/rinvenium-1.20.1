package silly.chemthunder.rinvenium.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import silly.chemthunder.rinvenium.Rinvenium;
import silly.chemthunder.rinvenium.cca.entity.EnvixiaFormComponent;
import silly.chemthunder.rinvenium.cca.entity.SpearParryComponent;
import silly.chemthunder.rinvenium.index.RinveniumDamageSources;
import silly.chemthunder.rinvenium.index.RinveniumEnchantments;
import silly.chemthunder.rinvenium.index.RinveniumEntities;
import silly.chemthunder.rinvenium.index.RinveniumItems;
import silly.chemthunder.rinvenium.index.RinveniumSoundEvents;
import silly.chemthunder.rinvenium.index.RinveniumStatusEffects;
import silly.chemthunder.rinvenium.item.EnvixiaArmorItem;

import java.util.List;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Attackable {
    @Shadow
    public abstract boolean hasStatusEffect(StatusEffect effect);

    @Shadow
    public abstract ItemStack getEquippedStack(EquipmentSlot slot);

    @Shadow
    protected int roll;

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "tickRiptide", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;attackLivingEntity(Lnet/minecraft/entity/LivingEntity;)V"))
    private void electrifySHIPFGHERFTIOPGIPRTHGPIRTHNPIHJRTYPIJIPTY(Box a, Box b, CallbackInfo ci) {
        Box box = a.union(b);
        List<Entity> list = this.getWorld().getOtherEntities(this, box);
        if (!list.isEmpty()) {
            for (Entity entity : list) {
                if (entity instanceof LivingEntity living) {
                    LivingEntity you = (LivingEntity) (Object) this;
                    if (you instanceof PlayerEntity player) {
                        ItemStack stack = player.getStackInHand(player.getActiveHand());
                        var hasChanneling = EnchantmentHelper.hasChanneling(stack);
                        var hasSpear = EnchantmentHelper.getLevel(RinveniumEnchantments.RUSH, stack) > 0;
                        if (hasChanneling || hasSpear) {
                            if (!Rinvenium.haters.contains(player.getUuid())) {
                                living.addStatusEffect(new StatusEffectInstance(RinveniumStatusEffects.SPARKED, 10));
                            } else {
                                player.addStatusEffect(new StatusEffectInstance(RinveniumStatusEffects.SPARKED, 20));

                                if (player.getWorld() instanceof ServerWorld serverWorld) {
                                    LightningEntity bolt = new LightningEntity(EntityType.LIGHTNING_BOLT, serverWorld);

                                    bolt.updatePosition(player.getX(), player.getY(), player.getZ());

                                    serverWorld.spawnEntity(bolt);
                                }
                            }
                            player.getWorld().playSound(null, player.getBlockPos(), RinveniumSoundEvents.SPEAR_DASH_IMPACT, SoundCategory.PLAYERS, 1, 1);
                        }
                    }
                }
            }
        }
    }

    @WrapOperation(method = "applyDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;modifyAppliedDamage(Lnet/minecraft/entity/damage/DamageSource;F)F"))
    private float rinvenium$spearParryDamage(LivingEntity entity, DamageSource source, float amount, Operation<Float> original) {
        float base = original.call(entity, source, amount);
        if (source.getAttacker() instanceof PlayerEntity player && player.getStackInHand(Hand.MAIN_HAND).isOf(RinveniumItems.ENVINIUM_SPEAR)) {
            SpearParryComponent spearParryComponent = SpearParryComponent.get(player);
            if (spearParryComponent.getDoubleIntValue2() > 0 && EnchantmentHelper.getLevel(RinveniumEnchantments.RUSH, player.getStackInHand(Hand.MAIN_HAND)) <= 0) {
                spearParryComponent.setDoubleIntValue2(0);
                spearParryComponent.setDoubleIntValue1(SpearParryComponent.MAX_PARRY_WINDOW);
                spearParryComponent.setDoubleBoolValue1(true);
                return base * 1.5f;
            }
        }
        return base;
    }

    @WrapWithCondition(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;takeKnockback(DDD)V"))
    private boolean rinvenium$knockbacknt(LivingEntity instance, double strength, double x, double z, @Local(argsOnly = true) DamageSource damageSource) {
        return !damageSource.isOf(RinveniumDamageSources.BOOP) && !damageSource.isOf(RinveniumDamageSources.ELECTRICITY);
    }

    @Inject(method = "tickFallFlying", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getEquippedStack(Lnet/minecraft/entity/EquipmentSlot;)Lnet/minecraft/item/ItemStack;"))
    private void rinvenium$envixiaElytraFlight(CallbackInfo ci) {
        ItemStack itemStack = this.getEquippedStack(EquipmentSlot.CHEST);
        boolean bl = this.getFlag(Entity.FALL_FLYING_FLAG_INDEX);
        if (((LivingEntity) ((Object)this)) instanceof PlayerEntity player) {
            EnvixiaFormComponent envixiaFormComponent = EnvixiaFormComponent.get(player);
            if (envixiaFormComponent.getTripleBoolValue1() && itemStack.isOf(RinveniumItems.ENVIXIA_CHESTPLATE) && EnvixiaArmorItem.hasFullSuit(player)) {
                bl = true;
                int i = this.roll + 1;
                if (!this.getWorld().isClient && i % 10 == 0) {
                    int j = i / 10;
                    if (j % 2 == 0) {
                        //itemStack.damage(1, (LivingEntity) ((Object)this), player -> player.sendEquipmentBreakStatus(EquipmentSlot.CHEST));
                    }

                    this.emitGameEvent(GameEvent.ELYTRA_GLIDE);
                }
            } else {
                bl = false;
            }
        }
        if (!this.getWorld().isClient) {
            this.setFlag(Entity.FALL_FLYING_FLAG_INDEX, bl);
        }
    }
}
