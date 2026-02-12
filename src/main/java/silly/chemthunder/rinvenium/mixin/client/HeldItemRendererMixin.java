package silly.chemthunder.rinvenium.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import silly.chemthunder.rinvenium.datagen.RinveniumItemTagProvider;

@Mixin(HeldItemRenderer.class)
public abstract class HeldItemRendererMixin {
    @WrapOperation(method = "renderItem(FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;Lnet/minecraft/client/network/ClientPlayerEntity;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderFirstPersonItem(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/util/Hand;FLnet/minecraft/item/ItemStack;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"))
    private void gitsnshiggles$renderItems(HeldItemRenderer instance, AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, Operation<Void> original) {
        ItemStack stack = player.getStackInHand(hand);
        original.call(instance, player, tickDelta, pitch, hand, swingProgress, stack, equipProgress, matrices, vertexConsumers, light);
    }

    @Inject(method = "resetEquipProgress", at = @At("HEAD"), cancellable = true)
    private void gitsnshiggles$bigItemPickup(Hand hand, CallbackInfo ci) {
        if (MinecraftClient.getInstance().player != null) {
            ItemStack stack = MinecraftClient.getInstance().player.getStackInHand(hand);
            if (stack.isIn(RinveniumItemTagProvider.BIG_ITEM_RENDERING)) {
                ci.cancel();
            }
        }
    }
}
