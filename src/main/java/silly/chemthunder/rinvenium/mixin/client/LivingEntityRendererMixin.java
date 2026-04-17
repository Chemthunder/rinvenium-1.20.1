package silly.chemthunder.rinvenium.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import silly.chemthunder.rinvenium.render.manager.ImpactFrameManager;
import silly.chemthunder.rinvenium.util.inject.RenderContainer;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> implements FeatureRendererContext<T, M> {
    @Shadow protected M model;

    protected LivingEntityRendererMixin(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    /*@WrapOperation(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/EntityModel;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;IIFFFF)V"))
    private void rinvenium$envixiaCyanHighlight(EntityModel instance, MatrixStack matrixStack, VertexConsumer vertexConsumer, int light, int overlay, float r, float g, float b, float a, Operation<Void> original, @Local(argsOnly = true) LivingEntity livingEntity) {
        if (livingEntity instanceof PlayerEntity player) {
            EnvixiaFormComponent envixiaFormComponent = EnvixiaFormComponent.get(player);
            if (envixiaFormComponent.getTripleBoolValue1() && MinecraftClient.getInstance().hasOutline(livingEntity)) {
                this.model.render(matrixStack, vertexConsumer, light, overlay, 0.2f, 0.96f, 0.83f, 0.8f);
            } else {
                original.call(instance, matrixStack, vertexConsumer, light, overlay, r, g, b, a);
            }
        } else {
            original.call(instance, matrixStack, vertexConsumer, light, overlay, r, g, b, a);
        }
    }*/

    @Inject(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;getOverlay(Lnet/minecraft/entity/LivingEntity;F)I"))
    private void rinvenium$derenderForImpactFrame(T livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci, @Local VertexConsumer vertexConsumer) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            ImpactFrameManager impactFrameManager = ((RenderContainer) client.player).getImpactFrameManager();
            impactFrameManager.get().forEach(impactFrame -> {
                if (impactFrame.uuid.equals(livingEntity.getUuid())) {

                }
            });
        }
    }
}