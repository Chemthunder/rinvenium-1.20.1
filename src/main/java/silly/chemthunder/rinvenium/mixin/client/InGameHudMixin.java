package silly.chemthunder.rinvenium.mixin.client;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import silly.chemthunder.rinvenium.cca.entity.EnvixiaFormComponent;
import silly.chemthunder.rinvenium.index.RinveniumItems;
import silly.chemthunder.rinvenium.render.manager.FlashManager;
import silly.chemthunder.rinvenium.util.RinveniumTextureUtils;
import silly.chemthunder.rinvenium.util.inject.FlashContainer;

@SuppressWarnings("DiscouragedShift")
@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Shadow @Final private MinecraftClient client;
    @Shadow protected abstract int getHeartCount(LivingEntity entity);
    @Shadow protected abstract int getHeartRows(int heartCount);
    @Shadow protected abstract void renderOverlay(DrawContext context, Identifier texture, float opacity);

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getFrozenTicks()I", shift = At.Shift.BEFORE))
    private void rinvenium$renderCustomCrosshair(DrawContext context, float tickDelta, CallbackInfo ci) {
        if (this.client != null && this.client.player != null && this.client.options.getPerspective().isFirstPerson()) {
            if (this.client.player.getStackInHand(Hand.MAIN_HAND).isOf(RinveniumItems.HAIL_OF_THE_GODS)) {
                int i = context.getScaledWindowWidth() / 2;
                int j = context.getScaledWindowHeight() / 2;
                context.drawTexture(RinveniumTextureUtils.HAIL_OF_THE_GODS_CROSSHAIR, i - 7, j - 8, 0, 0, 0, 15, 15, 15, 15);
            }
        }
    }

    @WrapWithCondition(method = "renderCrosshair", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V", ordinal = 0))
    private boolean rinvenium$derenderCrosshair(DrawContext instance, Identifier texture, int x, int y, int u, int v, int width, int height) {
        return this.client != null && this.client.player != null && !this.client.player.getStackInHand(Hand.MAIN_HAND).isOf(RinveniumItems.HAIL_OF_THE_GODS);
    }

    @WrapOperation(method = "renderStatusBars", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/hud/InGameHud;ICONS:Lnet/minecraft/util/Identifier;", ordinal = 3, opcode = Opcodes.GETSTATIC))
    private Identifier rinvenium$envixiaHungerBarBG(Operation<Identifier> original) {
        ClientPlayerEntity clientPlayer = MinecraftClient.getInstance().player;
        if (clientPlayer != null) {
            EnvixiaFormComponent envixiaFormComponent = EnvixiaFormComponent.get(clientPlayer);
            if (envixiaFormComponent.getTripleBoolValue1()) {
                return RinveniumTextureUtils.ICONS;
            }
        }
        return original.call();
    }

    @WrapOperation(method = "renderStatusBars", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/hud/InGameHud;ICONS:Lnet/minecraft/util/Identifier;", ordinal = 4, opcode = Opcodes.GETSTATIC))
    private Identifier rinvenium$envixiaHungerBarHalf(Operation<Identifier> original) {
        ClientPlayerEntity clientPlayer = MinecraftClient.getInstance().player;
        if (clientPlayer != null) {
            EnvixiaFormComponent envixiaFormComponent = EnvixiaFormComponent.get(clientPlayer);
            if (envixiaFormComponent.getTripleBoolValue1()) {
                return RinveniumTextureUtils.ICONS;
            }
        }
        return original.call();
    }

    @WrapOperation(method = "renderStatusBars", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/hud/InGameHud;ICONS:Lnet/minecraft/util/Identifier;", ordinal = 5, opcode = Opcodes.GETSTATIC))
    private Identifier rinvenium$envixiaHungerBarFull(Operation<Identifier> original) {
        ClientPlayerEntity clientPlayer = MinecraftClient.getInstance().player;
        if (clientPlayer != null) {
            EnvixiaFormComponent envixiaFormComponent = EnvixiaFormComponent.get(clientPlayer);
            if (envixiaFormComponent.getTripleBoolValue1()) {
                return RinveniumTextureUtils.ICONS;
            }
        }
        return original.call();
    }


    /*@Inject(method = "renderStatusBars", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;getProfiler()Lnet/minecraft/util/profiler/Profiler;", ordinal = 2))
    private void rinvenium$flightBar(DrawContext context, CallbackInfo ci, @Local PlayerEntity playerEntity, @Local LivingEntity livingEntity, @Local(ordinal = 11) int t, @Local(ordinal = 5) int n) {
        int maxTime = 60;
        EnvixiaFormComponent envixiaFormComponent = EnvixiaFormComponent.get(playerEntity);
        int time = envixiaFormComponent.getInt();
        int rowOffset = this.getHeartRows(this.getHeartCount(livingEntity)) - 1;
        t -= rowOffset * 10;
        if (envixiaFormComponent.getTripleBoolValue1()) {
            context.drawTexture(RinveniumTextureUtils.FLY_BAR_BG, n - 80, t, 0, 0, 80, 8);
        }
    }*/


    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getFrozenTicks()I", shift = At.Shift.BEFORE))
    private void rinvenium$renderOverlays(DrawContext context, float tickDelta, CallbackInfo ci) {
        if (this.client != null && this.client.player != null) {
            FlashManager flashManager = ((FlashContainer) this.client.player).getFlashManager();
            flashManager.tick();
            flashManager.get().forEach(flash -> {
                if (flash.age >= flash.maxAge + flash.fadeTime) {
                    flash.fadeTicks--;
                    this.renderOverlay(
                            context,
                            RinveniumTextureUtils.SCREEN_FLASH,
                            (float) Math.min(flash.fadeTicks, flash.fadeTime) * flash.maxOpacity / flash.fadeTime
                    );
                } else if (flash.age <= flash.fadeTime) {
                    flash.fadeTicks++;
                    this.renderOverlay(
                            context,
                            RinveniumTextureUtils.SCREEN_FLASH,
                            (float) Math.min(flash.fadeTicks, flash.fadeTime) * flash.maxOpacity / flash.fadeTime
                    );
                } else {
                    this.renderOverlay(
                            context,
                            RinveniumTextureUtils.SCREEN_FLASH,
                            flash.maxOpacity
                    );
                }
            });
        }
    }
}