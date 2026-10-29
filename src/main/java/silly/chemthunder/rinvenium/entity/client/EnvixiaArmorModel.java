package silly.chemthunder.rinvenium.entity.client;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;

// Made with Blockbench 5.2.2
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports
public class EnvixiaArmorModel<T extends LivingEntity> extends BipedEntityModel<T> {
	public ModelPart headpiece;
	public ModelPart clearGlassHelmet;
	public ModelPart chestplate;
	public ModelPart jetpack;
	public ModelPart right_wing;
	public ModelPart left_wing;
	public ModelPart right_arm;
	public ModelPart left_arm;
	public ModelPart right_leg;
	public ModelPart right_boot;
	public ModelPart left_leg;
	public ModelPart left_boot;
	public EnvixiaArmorModel(ModelPart root) {
        super(root);
        this.headpiece = root.getChild("headpiece");
		this.clearGlassHelmet = root.getChild("clearGlassHelmet");
		this.chestplate = root.getChild("chestplate");
		this.jetpack = this.chestplate.getChild("jetpack");
		this.right_wing = this.jetpack.getChild("right_wing");
		this.left_wing = this.jetpack.getChild("left_wing");
		this.right_arm = root.getChild("right_arm");
		this.left_arm = root.getChild("left_arm");
		this.right_leg = root.getChild("right_leg");
		this.right_boot = this.right_leg.getChild("right_boot");
		this.left_leg = root.getChild("left_leg");
		this.left_boot = this.left_leg.getChild("left_boot");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = BipedEntityModel.getModelData(Dilation.NONE, 0);
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData headpiece = modelPartData.addChild("headpiece", ModelPartBuilder.create().uv(0, 53).cuboid(-5.0F, -7.0F, -3.0F, 1.0F, 6.0F, 6.0F, new Dilation(0.0F))
			.uv(14, 61).cuboid(4.0F, -7.0F, -3.0F, 1.0F, 6.0F, 6.0F, new Dilation(0.0F))
			.uv(37, 93).cuboid(4.0F, -4.0F, 4.0F, 1.0F, 1.0F, -1.0F, new Dilation(0.0F))
			.uv(37, 93).cuboid(-5.0F, -4.0F, 4.0F, 1.0F, 1.0F, -1.0F, new Dilation(0.0F))
			.uv(26, 91).cuboid(-5.0F, -4.0F, 4.0F, 10.0F, 1.0F, 1.0F, new Dilation(0.0F))
			.uv(0, 75).cuboid(-5.0F, -7.0F, -3.0F, 1.0F, 6.0F, 6.0F, new Dilation(0.2F))
			.uv(14, 75).cuboid(4.0F, -7.0F, -3.0F, 1.0F, 6.0F, 6.0F, new Dilation(0.2F)), ModelTransform.pivot(-0.0F, 3.0F, 0.0F));

		ModelPartData clearGlassHelmet = modelPartData.addChild("clearGlassHelmet", ModelPartBuilder.create().uv(0, 112).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.3F)), ModelTransform.pivot(-0.0F, 3.0F, 0.0F));

		ModelPartData cube_r1 = headpiece.addChild("cube_r1", ModelPartBuilder.create().uv(14, 89).cuboid(0.5F, 0.5F, 2.0F, 0.0F, 3.0F, 9.0F, new Dilation(0.0F))
			.uv(0, 81).cuboid(-8.5F, 0.5F, 2.0F, 0.0F, 3.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(4.0F, -6.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		ModelPartData chestplate = modelPartData.addChild("chestplate", ModelPartBuilder.create().uv(0, 26).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.15F))
			.uv(24, 26).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 6.0F, 4.0F, new Dilation(0.25F))
			.uv(52, 45).cuboid(-4.0F, 9.0F, -2.0F, 8.0F, 3.0F, 4.0F, new Dilation(0.25F))
			.uv(59, 69).cuboid(-1.0F, 12.5F, -1.95F, 2.0F, 2.0F, 4.0F, new Dilation(0.29F)), ModelTransform.pivot(0.0F, 13.0F, 0.0F));

		ModelPartData cube_r2 = chestplate.addChild("cube_r2", ModelPartBuilder.create().uv(24, 36).cuboid(-4.0F, 9F, -11F, 8.0F, 5.0F, 4.0F, new Dilation(-0.25F)), ModelTransform.of(0.0F, -11.75F, -2.25F, 0.6981F, 0.0F, 0.0F));

		ModelPartData jetpack = chestplate.addChild("jetpack", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 21.0F, 0.5F));

		ModelPartData cube_r3 = jetpack.addChild("cube_r3", ModelPartBuilder.create().uv(0, 65).cuboid(1.1485F, -2.4196F, -1.6905F, 3.0F, 7.0F, 3.0F, new Dilation(-0.15F)), ModelTransform.of(0.0F, -14.4019F, 3.5926F, 0.1544F, -0.0083F, -0.3926F));

		ModelPartData cube_r4 = jetpack.addChild("cube_r4", ModelPartBuilder.create().uv(64, 30).cuboid(-4.1486F, -2.4196F, -1.6905F, 3.0F, 7.0F, 3.0F, new Dilation(-0.15F)), ModelTransform.of(0.0F, -14.4019F, 3.5926F, 0.1544F, 0.0083F, 0.3926F));

		ModelPartData cube_r5 = jetpack.addChild("cube_r5", ModelPartBuilder.create().uv(52, 52).cuboid(-3.0F, -3.0F, -1.5F, 6.0F, 6.0F, 3.0F, new Dilation(0.3F)), ModelTransform.of(0.0F, -18.5F, 3.0F, 0.0F, 0.0F, -0.7854F));

		ModelPartData right_wing = jetpack.addChild("right_wing", ModelPartBuilder.create().uv(20, 40).cuboid(-1.0F, -0.5F, 0.0F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F))
			.uv(0, 0).cuboid(-23.0F, -6.5F, 0.0F, 22.0F, 13.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-3.0F, -18.5F, 4.75F, 0.1068F, 0.3786F, -0.1104F));

		ModelPartData left_wing = jetpack.addChild("left_wing", ModelPartBuilder.create().uv(22, 40).cuboid(0.0F, -0.5F, 0.0F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F))
			.uv(0, 13).cuboid(1.0F, -6.5F, 0.0F, 22.0F, 13.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(3.0F, -18.5F, 4.75F, 0.1068F, -0.3786F, 0.1104F));

		ModelPartData right_arm = modelPartData.addChild("right_arm", ModelPartBuilder.create().uv(48, 29).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.1F))
			.uv(64, 8).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new Dilation(0.55F)), ModelTransform.pivot(-4.0F, 3.0F, 0.0F));

		ModelPartData cube_r6 = right_arm.addChild("cube_r6", ModelPartBuilder.create().uv(64, 8).cuboid(-7.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new Dilation(0.2F)), ModelTransform.of(4.0F, 8.0F, 0.0F, 3.1416F, 0.0F, 0.0F));

		ModelPartData left_arm = modelPartData.addChild("left_arm", ModelPartBuilder.create().uv(48, 13).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.1F))
			.uv(64, 0).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new Dilation(0.55F)), ModelTransform.pivot(4.0F, 3.0F, 0.0F));

		ModelPartData cube_r7 = left_arm.addChild("cube_r7", ModelPartBuilder.create().uv(64, 0).cuboid(3.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new Dilation(0.2F)), ModelTransform.of(-4.0F, 8.0F, 0.0F, 3.1416F, 0.0F, 0.0F));

		ModelPartData right_leg = modelPartData.addChild("right_leg", ModelPartBuilder.create().uv(36, 45).cuboid(-2.0F, -1.0F, -2.0F, 4.0F, 10.0F, 4.0F, new Dilation(0.0F))
			.uv(44, 62).cuboid(-2.0F, -1.0F, -2.0F, 4.0F, 5.0F, 4.0F, new Dilation(0.1F)), ModelTransform.pivot(-2.0F, 13.0F, 0.0F));

		ModelPartData right_boot = right_leg.addChild("right_boot", ModelPartBuilder.create().uv(64, 23).cuboid(1.0F, -3.0F, -2.0F, 4.0F, 3.0F, 4.0F, new Dilation(0.1F)), ModelTransform.pivot(-3.0F, 11.0F, 0.0F));

		ModelPartData left_leg = modelPartData.addChild("left_leg", ModelPartBuilder.create().uv(20, 45).cuboid(-2.0F, -1.0F, -2.0F, 4.0F, 10.0F, 4.0F, new Dilation(0.0F))
			.uv(28, 61).cuboid(-2.0F, -1.0F, -2.0F, 4.0F, 5.0F, 4.0F, new Dilation(0.1F)), ModelTransform.pivot(2.0F, 13.0F, 0.0F));

		ModelPartData left_boot = left_leg.addChild("left_boot", ModelPartBuilder.create().uv(64, 16).cuboid(-5.0F, -3.0F, -2.0F, 4.0F, 3.0F, 4.0F, new Dilation(0.1F)), ModelTransform.pivot(3.0F, 11.0F, 0.0F));
		return TexturedModelData.of(modelData, 128, 128);
	}
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		headpiece.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		clearGlassHelmet.render(matrices, vertexConsumer, light, overlay, red, green, blue, 0.8f);
		chestplate.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		right_arm.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		left_arm.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		right_leg.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		left_leg.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		right_boot.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		left_boot.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}
}