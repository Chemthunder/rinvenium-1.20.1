package silly.chemthunder.rinvenium.mixin.client;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import silly.chemthunder.rinvenium.cca.entity.EnvixiaFormComponent;
import silly.chemthunder.rinvenium.render.manager.ImpactFrameManager;
import silly.chemthunder.rinvenium.util.inject.RenderContainer;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {
    @Shadow private @Nullable PostEffectProcessor entityOutlinePostProcessor;
    @Shadow public abstract @Nullable Framebuffer getEntityOutlinesFramebuffer();
    @Shadow @Final private BufferBuilderStorage bufferBuilders;

    @Shadow
    @Final
    private MinecraftClient client;

    @WrapOperation(
        method = "render",
         at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/Entity;getTeamColorValue()I"
        )
    )
    private int rinvenium$envixiaOutlineColor(Entity instance, Operation<Integer> original, @Local Entity entity) {
        if (entity instanceof PlayerEntity player) {
            EnvixiaFormComponent envixiaFormComponent = EnvixiaFormComponent.get(player);
            if (envixiaFormComponent.getTripleBoolValue1()) {
                return 0x33f6b3 - 16777215;
            } else {
                return original.call(instance);
            }
        } else {
            return original.call(instance);
        }
    }
    
    @ModifyArg(
        method = "render",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/OutlineVertexConsumerProvider;setColor(IIII)V"
        ),
        index = 3
    )
    private int rinvenium$envixiaOutlineColorAlpha(int alpha, @Local Entity entity) {
        if (entity instanceof PlayerEntity player) {
            EnvixiaFormComponent envixiaFormComponent = EnvixiaFormComponent.get(player);
            return envixiaFormComponent.getTripleBoolValue1() ? 80 : alpha;
        } else {
            return alpha;
        }
    }

    @WrapWithCondition(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;renderSky(Lnet/minecraft/client/util/math/MatrixStack;Lorg/joml/Matrix4f;FLnet/minecraft/client/render/Camera;ZLjava/lang/Runnable;)V"))
    private boolean rinvenium$cancelSkyRendering(WorldRenderer instance, MatrixStack matrices, Matrix4f projectionMatrix, float tickDelta, Camera camera, boolean thickFog, Runnable fogCallback) {
        if (client.player != null) {
            ImpactFrameManager impactFrameManager = ((RenderContainer) client.player).getImpactFrameManager();
            return impactFrameManager.get().isEmpty();
        }
        return true;
    }


}