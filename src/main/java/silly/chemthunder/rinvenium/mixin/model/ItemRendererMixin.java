package silly.chemthunder.rinvenium.mixin.model;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import silly.chemthunder.rinvenium.Rinvenium;
import silly.chemthunder.rinvenium.cca.entity.SpearParryComponent;
import silly.chemthunder.rinvenium.index.RinveniumItems;
import silly.chemthunder.rinvenium.item.EnviniumSpearItem;


@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    @Inject(method = "renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/world/World;III)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;getModel(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;I)Lnet/minecraft/client/render/model/BakedModel;"))
    private void rinvenium$storeEntity(LivingEntity entity, ItemStack item, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, World world, int light, int overlay, int seed, CallbackInfo ci) {
        if (item.isOf(RinveniumItems.ENVINIUM_SPEAR) && entity instanceof PlayerEntity player) {
            ItemRendererMixin.entity = player;
        }
    }

    private static PlayerEntity entity;

    /**
     * This class was created by Vowxky.
     * All rights reserved to the developer.
     */
    @ModifyVariable(method = "renderItem", at = @At(value = "HEAD"), argsOnly = true)
    public BakedModel useModel(BakedModel value, ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        PlayerEntity player = MinecraftClient.getInstance().player;
        if (player != null && entity != null) {
            if (stack.isOf(RinveniumItems.ENVINIUM_SPEAR) && (renderMode != ModelTransformationMode.GUI) && renderMode != ModelTransformationMode.GROUND) {
                EnviniumSpearItem.Texture texture = EnviniumSpearItem.Texture.DEFAULT;
                if (stack.getItem() instanceof EnviniumSpearItem spearItem) {
                    texture = spearItem.getTexture();
                }
                SpearParryComponent spearParryComponent = SpearParryComponent.get(entity);
                String blocking;
                if (spearParryComponent.getDoubleBoolValue2()) {
                    blocking = "_blocking";
                } else {
                    blocking = "";
                }
                String append = "";
                switch (texture) {
                    case REMAKE ->  append = "remake";
                    case HSTAR ->  append = "hstar";
                    case MIDGET ->  append = "midget";
                    case CREATURE ->  append = "creature";
                    case INVIS ->  append = "invis";
                    case HEARTLESS ->  append = "heartless";
                    case PBGS ->  append = "pbgs";
                    case SCARLET ->  append = "scarlet";
                    case HEARTTECH ->  append = "hearttech";
                    default -> append = "";
                }
                if (!append.isEmpty()) {
                    append = "_" + append;
                }
                return ((ItemRendererAccessor) this).renderer$getModels().getModelManager().getModel(new ModelIdentifier(Rinvenium.MOD_ID, "spear_handheld_2d" + blocking + append, "inventory"));
            }
        }
        return value;
    }
}