package silly.chemthunder.rinvenium.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import silly.chemthunder.rinvenium.cca.entity.SpearDashingComponent;
import silly.chemthunder.rinvenium.cca.entity.SpearParryComponent;
import silly.chemthunder.rinvenium.index.RinveniumEnchantments;
import silly.chemthunder.rinvenium.index.RinveniumItems;

@Mixin(DrawContext.class)
public abstract class DrawContextMixin {
    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    public abstract void fill(RenderLayer layer, int x1, int y1, int x2, int y2, int color);

    @Inject(method = "drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fill(Lnet/minecraft/client/render/RenderLayer;IIIII)V", ordinal = 1))
    private void rinvenium$drawSpearParryWindows(TextRenderer textRenderer, ItemStack stack, int x, int y, String countOverride, CallbackInfo ci) {
        if (this.client.player != null && stack.isOf(RinveniumItems.ENVINIUM_SPEAR)) {
            int k2 = x + 2;
            int l2 = y + 13;
            SpearParryComponent spearParryComponent = SpearParryComponent.get(this.client.player);
            SpearDashingComponent spearDashingComponent = SpearDashingComponent.get(this.client.player);
            if (EnchantmentHelper.getLevel(RinveniumEnchantments.RUSH, stack) > 0) {
                int m = (int) (spearDashingComponent.getChargePercent() * 13);
                this.fill(RenderLayer.getGuiOverlay(), k2, l2, k2 + 13, l2 + 2, -16777216);
                this.fill(RenderLayer.getGuiOverlay(), k2, l2, k2 + m, l2 + 1, 0x9cfdff | 0xFF000000);
            } else {
                int i = (int) Math.ceil(spearParryComponent.getDoubleIntValue2() > 0 ? spearParryComponent.getDamageWindowPercentage() * 13 : spearParryComponent.getParryWindowPercentage() * 13);
                int j = spearParryComponent.getDoubleIntValue2() > 0 ? 0x7a1c8c : 0xfdc211;
                this.fill(RenderLayer.getGuiOverlay(), k2, l2, k2 + 13, l2 + 2, -16777216);
                this.fill(RenderLayer.getGuiOverlay(), k2, l2, k2 + i, l2 + 1, j | 0xFF000000);
            }
        }
    }
}
