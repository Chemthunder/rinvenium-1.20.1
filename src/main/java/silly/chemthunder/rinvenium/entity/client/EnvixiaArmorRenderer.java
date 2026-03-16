package silly.chemthunder.rinvenium.entity.client;

import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import silly.chemthunder.rinvenium.Rinvenium;
import silly.chemthunder.rinvenium.index.client.RinveniumEntityModelLayers;

public class EnvixiaArmorRenderer implements ArmorRenderer {
    public static final Identifier TEXTURE = Rinvenium.id("textures/entity/envixia_armor.png");
    private final MinecraftClient client = MinecraftClient.getInstance();
    private EnvixiaArmorModel<LivingEntity> model;


    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, ItemStack stack, LivingEntity entity, EquipmentSlot slot, int light, BipedEntityModel<LivingEntity> contextModel) {
        if (model == null) {
            model = new EnvixiaArmorModel<>(client.getEntityModelLoader().getModelPart(RinveniumEntityModelLayers.ENVIXIA_ARMOR));
        }

        contextModel.copyBipedStateTo(model);
        model.setVisible(true);
        model.head.visible = slot == EquipmentSlot.HEAD;
        model.body.visible = slot == EquipmentSlot.CHEST;
        model.right_arm.visible = slot == EquipmentSlot.CHEST;
        model.left_arm.visible = slot == EquipmentSlot.CHEST;
        model.right_leg.visible = slot == EquipmentSlot.LEGS;
        model.left_leg.visible = slot == EquipmentSlot.LEGS;
        model.right_boot.visible = slot == EquipmentSlot.FEET;
        model.left_boot.visible = slot == EquipmentSlot.FEET;

        model.render(matrices, ItemRenderer.getArmorGlintConsumer(vertexConsumers, RenderLayer.getArmorCutoutNoCull(TEXTURE), true, true), light, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 1.0f);

    }
}
