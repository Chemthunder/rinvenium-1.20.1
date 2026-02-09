package silly.chemthunder.rinvenium.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import silly.chemthunder.rinvenium.cca.item.EnviniumSpearItemComponent;
import silly.chemthunder.rinvenium.index.RinveniumEnchantments;
import silly.chemthunder.rinvenium.index.RinveniumItems;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }


    @Inject(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/player/PlayerEntity;DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V"))
    private void customHitSound(Entity target, CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object)this;
        if (player.getStackInHand(Hand.MAIN_HAND).isOf(RinveniumItems.ENVINIUM_SPEAR)) {
            this.getWorld().playSound(target.getX(), target.getY(), target.getZ(), SoundEvents.BLOCK_COPPER_BREAK, SoundCategory.PLAYERS, 1, 1, true);
        }
    }

    @Inject(method = "takeShieldHit", at = @At("HEAD"))
    private void parry(LivingEntity attacker, CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object)this;
        ItemStack stack = player.getStackInHand(player.getActiveHand());
        boolean noRush = !(EnchantmentHelper.getLevel(RinveniumEnchantments.RUSH, stack) > 0);
        EnviniumSpearItemComponent component = EnviniumSpearItemComponent.KEY.get(stack);

        if (!player.getItemCooldownManager().isCoolingDown(stack.getItem())) {
            if (stack.isOf(RinveniumItems.ENVINIUM_SPEAR) && noRush && player.isUsingItem()) {
                attacker.velocityModified = true;

                if (component.getParry() < 5) {
                    component.setParry(component.getParry() + 1);
                } else if (component.getParry() == 5) {
                    component.setParry(0);
                    player.getItemCooldownManager().set(stack.getItem(), 600);
                    player.disableShield(false);
                }
            }
        }
    }
}
