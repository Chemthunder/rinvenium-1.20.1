package silly.chemthunder.rinvenium.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import silly.chemthunder.rinvenium.cca.entity.EnvixiaFormComponent;
import silly.chemthunder.rinvenium.datagen.RinveniumItemTagProvider;

@Mixin(Item.class)
public abstract class ItemMixin {

    @Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;canConsume(Z)Z"), cancellable = true)
    private void rinvenium$shouldEatBatteries(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir, @Local ItemStack itemStack) {
        EnvixiaFormComponent envixiaFormComponent = EnvixiaFormComponent.get(user);
        if (itemStack.isIn(RinveniumItemTagProvider.ENVIXIA_MUNCHIES)) {
            if (!envixiaFormComponent.getTripleBoolValue1()) {
                cir.setReturnValue(TypedActionResult.fail(itemStack));
            }
        }/* else {
            if (envixiaFormComponent.getTripleBoolValue1()) {
                cir.setReturnValue(TypedActionResult.fail(itemStack));
            }
        }*/
    }
}
