package silly.chemthunder.rinvenium.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.SpriteAtlasHolder;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import silly.chemthunder.rinvenium.cca.entity.EnvixiaFormComponent;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {
    @Shadow private @Nullable PostEffectProcessor entityOutlinePostProcessor;
    @Shadow public abstract @Nullable Framebuffer getEntityOutlinesFramebuffer();
    @Shadow @Final private BufferBuilderStorage bufferBuilders;

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
            // if (envixiaFormComponent.getTripleBoolValue1()) {
            //     return 80;
            // } else {
            //     return alpha;
            // }
            return envixiaFormComponent.getTripleBoolValue1() ? 80 : alpha;
        } else {
            return alpha;
        }
    }

    @Inject(method = "render", at = @At(value = "TAIL"))
    private void rinvenium$impactFrame(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f projectionMatrix, CallbackInfo ci) {
        //
    }
}