package silly.chemthunder.rinvenium.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import silly.chemthunder.rinvenium.Rinvenium;
import silly.chemthunder.rinvenium.cca.entity.SpearParryComponent;
import silly.chemthunder.rinvenium.index.RinveniumEnchantments;
import silly.chemthunder.rinvenium.index.RinveniumItems;
import silly.chemthunder.rinvenium.index.RinveniumStatusEffects;

import java.util.List;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Attackable {
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
                                living.addStatusEffect(new StatusEffectInstance(RinveniumStatusEffects.SPARKED, 20));
                            } else {
                                player.addStatusEffect(new StatusEffectInstance(RinveniumStatusEffects.SPARKED, 40));

                                if (player.getWorld() instanceof ServerWorld serverWorld) {
                                    LightningEntity bolt = new LightningEntity(EntityType.LIGHTNING_BOLT, serverWorld);

                                    bolt.updatePosition(player.getX(), player.getY(), player.getZ());

                                    serverWorld.spawnEntity(bolt);
                                }
                            }
                            player.playSound(SoundEvents.ITEM_TRIDENT_THUNDER, SoundCategory.PLAYERS, 1, 1);
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
            if (spearParryComponent.getDoubleIntValue2() > 0) {
                spearParryComponent.setDoubleIntValue2(0);
                spearParryComponent.setDoubleIntValue1(SpearParryComponent.MAX_PARRY_WINDOW);
                spearParryComponent.setDoubleBoolValue1(true);
                return base * 1.5f;
            }
        }
        return base;
    }
}
