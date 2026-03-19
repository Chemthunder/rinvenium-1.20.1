package silly.chemthunder.rinvenium.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import silly.chemthunder.rinvenium.cca.entity.EnvixiaFormComponent;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getTeamColorValue()I"))
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
    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/OutlineVertexConsumerProvider;setColor(IIII)V"), index = 3)
    private int rinvenium$envixiaOutlineColorAlpha(int alpha, @Local Entity entity) {
        if (entity instanceof PlayerEntity player) {
            EnvixiaFormComponent envixiaFormComponent = EnvixiaFormComponent.get(player);
            if (envixiaFormComponent.getTripleBoolValue1()) {
                return 80;
            } else {
                return alpha;
            }
        } else {
            return alpha;
        }
    }
}
