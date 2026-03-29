package silly.chemthunder.rinvenium.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import silly.chemthunder.rinvenium.render.ScreenFlash;
import silly.chemthunder.rinvenium.render.manager.FlashManager;
import silly.chemthunder.rinvenium.util.RinveniumTextureUtils;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    /*@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/GameOptions;getDistortionEffectScale()Lnet/minecraft/client/option/SimpleOption;"))
    private void rinvenium$renderFlash(float tickDelta, long startTime, boolean tick, CallbackInfo ci, @Local DrawContext drawContext) {

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            client.player.sendMessage(Text.literal("tickDelta: " + tickDelta), true);
        }
        FlashManager.tick();
        FlashManager.FLASHES.forEach(flash -> this.renderAMPEFADFlash(drawContext, flash, tickDelta));

    }

    @Unique
    private void renderAMPEFADFlash(DrawContext context, ScreenFlash flash, float tickDelta) {
        int i = context.getScaledWindowWidth();
        int j = context.getScaledWindowHeight();
        context.getMatrices().push();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ONE, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ONE);
        float r = (flash.color >> 16 & 0xFF) / 255.0f;
        float g = (flash.color >> 8 & 0xFF) / 255.0f;
        float b = (flash.color & 0xFF) / 255.0f;
        float alpha = MathHelper.lerp(tickDelta, 0.0f, 1.0f);

        context.setShaderColor(r, g, b, alpha);
        context.drawTexture(RinveniumTextureUtils.SCREEN_FLASH, 0, 0, -90, 0, 0, i, j, i, j);
        context.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        *//*RenderSystem.setShaderColor(r, g, b, alpha);
        RenderSystem.setShaderTexture(0, RinveniumTextureUtils.SCREEN_FLASH);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);*//*

        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        context.getMatrices().pop();
    }*/
}
