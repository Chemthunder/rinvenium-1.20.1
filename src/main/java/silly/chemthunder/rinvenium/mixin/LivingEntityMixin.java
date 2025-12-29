package silly.chemthunder.rinvenium.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import silly.chemthunder.rinvenium.Rinvenium;
import silly.chemthunder.rinvenium.index.RinveniumEnchantments;
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
                                living.addStatusEffect(new StatusEffectInstance(RinveniumStatusEffects.SPARKED, 600));
                            } else {
                                player.addStatusEffect(new StatusEffectInstance(RinveniumStatusEffects.SPARKED, 1200));

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
}
