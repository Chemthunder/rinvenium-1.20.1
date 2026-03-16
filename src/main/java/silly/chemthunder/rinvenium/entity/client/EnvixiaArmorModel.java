// Made with Blockbench 5.0.7
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports

package silly.chemthunder.rinvenium.entity.client;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;

public class EnvixiaArmorModel<T extends LivingEntity> extends BipedEntityModel<T> {
	public final ModelPart head;
	public final ModelPart body;
	public final ModelPart jetpack;
	public final ModelPart right_wing;
	public final ModelPart left_wing;
	public final ModelPart right_arm;
	public final ModelPart left_arm;
	public final ModelPart right_leg;
	public final ModelPart left_leg;
	public final ModelPart right_boot;
	public final ModelPart left_boot;
	public EnvixiaArmorModel(ModelPart root) {
        super(root);
        this.head = root.getChild("head");
		this.body = root.getChild("body");
		this.jetpack = root.getChild("jetpack");
		this.right_wing = root.getChild("right_wing");
		this.left_wing = root.getChild("left_wing");
		this.right_arm = root.getChild("right_arm");
		this.left_arm = root.getChild("left_arm");
		this.right_leg = root.getChild("right_leg");
		this.left_leg = root.getChild("left_leg");
		this.right_boot = root.getChild("right_boot");
		this.left_boot = root.getChild("left_boot");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = BipedEntityModel.getModelData(Dilation.NONE, 0);
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData head = modelPartData.addChild("head", ModelPartBuilder.create().uv(0, 53).cuboid(-5.0F, -7.0F, -3.0F, 1.0F, 6.0F, 6.0F, new Dilation(0.0F))
		.uv(14, 61).cuboid(4.0F, -7.0F, -3.0F, 1.0F, 6.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 1.0F, 0.0F));

		ModelPartData cube_r1 = head.addChild("cube_r1", ModelPartBuilder.create().uv(44, 0).cuboid(-1.0F, -2.0F, 0.0F, 1.0F, 4.0F, 9.0F, new Dilation(0.0F))
		.uv(0, 40).cuboid(-10.0F, -2.0F, 0.0F, 1.0F, 4.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(5.0F, -4.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create().uv(0, 26).cuboid(-4.0F, -10.0F, -2.0F, 8.0F, 10.0F, 4.0F, new Dilation(0.15F))
		.uv(60, 62).cuboid(-4.0F, -11.0F, 1.25F, 8.0F, 6.0F, 1.0F, new Dilation(0.25F))
		.uv(24, 26).cuboid(-4.0F, -11.0F, -2.0F, 8.0F, 6.0F, 4.0F, new Dilation(0.25F))
		.uv(52, 45).cuboid(-4.0F, -3.0F, -2.0F, 8.0F, 3.0F, 4.0F, new Dilation(0.25F))
		.uv(60, 69).cuboid(-1.0F, -1.0F, -2.2F, 2.0F, 3.0F, 3.0F, new Dilation(0.07F))
		.uv(64, 40).cuboid(-1.0F, -1.0F, 0.2F, 2.0F, 3.0F, 2.0F, new Dilation(0.07F)), ModelTransform.pivot(0.0F, 12.0F, 0.0F));

		ModelPartData cube_r2 = body.addChild("cube_r2", ModelPartBuilder.create().uv(24, 36).cuboid(-4.0F, -0.25F, -3.75F, 8.0F, 5.0F, 4.0F, new Dilation(-0.25F)), ModelTransform.of(0.0F, -10.75F, -2.25F, 0.6981F, 0.0F, 0.0F));

		ModelPartData jetpack = body.addChild("jetpack", ModelPartBuilder.create().uv(52, 52).cuboid(-3.0F, -23.0F, 2.0F, 6.0F, 7.0F, 3.0F, new Dilation(0.25F)), ModelTransform.pivot(0.0F, 12.0F, 0.0F));

		ModelPartData cube_r3 = jetpack.addChild("cube_r3", ModelPartBuilder.create().uv(0, 65).cuboid(-3.0F, 0.0F, -0.5F, 3.0F, 7.0F, 3.0F, new Dilation(-0.15F)), ModelTransform.of(3.0F, -20.5F, 2.5F, 0.1745F, 0.0F, -0.3927F));

		ModelPartData cube_r4 = jetpack.addChild("cube_r4", ModelPartBuilder.create().uv(64, 30).cuboid(0.0F, 0.0F, -0.5F, 3.0F, 7.0F, 3.0F, new Dilation(-0.15F)), ModelTransform.of(-3.0F, -20.5F, 2.5F, 0.1745F, 0.0F, 0.3927F));

		ModelPartData right_wing = jetpack.addChild("right_wing", ModelPartBuilder.create().uv(20, 40).cuboid(-1.0F, -0.5F, 0.0F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F))
		.uv(0, 0).cuboid(-23.0F, -6.5F, 0.0F, 22.0F, 13.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-2.0F, -20.5F, 5.25F, 0.0F, 0.3927F, -0.3927F));

		ModelPartData left_wing = jetpack.addChild("left_wing", ModelPartBuilder.create().uv(22, 40).cuboid(0.0F, -0.5F, 0.0F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F))
		.uv(0, 13).cuboid(1.0F, -6.5F, 0.0F, 22.0F, 13.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(2.0F, -20.5F, 5.25F, 0.0F, -0.3927F, 0.3927F));

		ModelPartData right_arm = modelPartData.addChild("right_arm", ModelPartBuilder.create().uv(48, 13).cuboid(0.0F, -2.0F, -2.0F, 4.0F, 11.0F, 4.0F, new Dilation(0.1F))
		.uv(64, 0).cuboid(0.0F, 3.5F, -2.0F, 4.0F, 4.0F, 4.0F, new Dilation(0.2F)), ModelTransform.pivot(4.0F, 3.0F, 0.0F));

		ModelPartData left_arm = modelPartData.addChild("left_arm", ModelPartBuilder.create().uv(48, 29).cuboid(-4.0F, -2.0F, -2.0F, 4.0F, 11.0F, 4.0F, new Dilation(0.1F))
		.uv(64, 8).cuboid(-4.0F, 3.5F, -2.0F, 4.0F, 4.0F, 4.0F, new Dilation(0.2F)), ModelTransform.pivot(-4.0F, 3.0F, 0.0F));

		ModelPartData right_leg = modelPartData.addChild("right_leg", ModelPartBuilder.create().uv(36, 45).cuboid(-4.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F))
		.uv(44, 62).cuboid(-4.0F, -2.0F, -2.0F, 4.0F, 5.0F, 4.0F, new Dilation(0.1F)), ModelTransform.pivot(0.0F, 14.0F, 0.0F));

		ModelPartData left_leg = modelPartData.addChild("left_leg", ModelPartBuilder.create().uv(20, 45).cuboid(0.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F))
		.uv(28, 61).cuboid(0.0F, -2.0F, -2.0F, 4.0F, 5.0F, 4.0F, new Dilation(0.1F)), ModelTransform.pivot(0.0F, 14.0F, 0.0F));

		ModelPartData right_boot = modelPartData.addChild("right_boot", ModelPartBuilder.create().uv(64, 23).cuboid(-1.0F, -3.0F, -2.0F, 4.0F, 3.0F, 4.0F, new Dilation(0.1F)), ModelTransform.pivot(-3.0F, 24.0F, 0.0F));

		ModelPartData left_boot = modelPartData.addChild("left_boot", ModelPartBuilder.create().uv(64, 16).cuboid(-1.0F, -3.0F, -2.0F, 4.0F, 3.0F, 4.0F, new Dilation(0.1F)), ModelTransform.pivot(1.0F, 24.0F, 0.0F));
		return TexturedModelData.of(modelData, 128, 128);
	}
	@Override
	public void setAngles(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		super.setAngles(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
	}
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		head.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		body.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		right_arm.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		left_arm.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		right_leg.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		left_leg.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		right_boot.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		left_boot.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}
}