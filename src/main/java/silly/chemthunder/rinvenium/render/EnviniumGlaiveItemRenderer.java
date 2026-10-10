package silly.chemthunder.rinvenium.render;

import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import org.jetbrains.annotations.NotNull;
import silly.chemthunder.rinvenium.Rinvenium;
import silly.chemthunder.rinvenium.cca.entity.GlaiveParryComponent;
import silly.chemthunder.rinvenium.item.EnviniumGlaiveItem;

public class EnviniumGlaiveItemRenderer implements BuiltinItemRendererRegistry.DynamicItemRenderer {
    private final Identifier glaiveId;
    public static PlayerEntity holder;

    public EnviniumGlaiveItemRenderer(Identifier glaiveId) {
        this.glaiveId = glaiveId;
    }

    @Override
    public void render(ItemStack stack, ModelTransformationMode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {

        MinecraftClient client = MinecraftClient.getInstance();
        ItemRenderer itemRenderer = client.getItemRenderer();
        ModelIdentifier modelId;
        if (holder == null) {
            holder = client.player;
        }
        if (itemRenderer == null || holder == null) {
            return;
        }
        assert this.glaiveId != null;
        EnviniumGlaiveItem.Texture texture = EnviniumGlaiveItem.Texture.DEFAULT;
        if (stack.getItem() instanceof EnviniumGlaiveItem glaiveItem) {
            texture = glaiveItem.getTexture(stack);
        }
        String append = getGlaiveTexture(texture);
        String blocking = "";
        if (mode != ModelTransformationMode.GUI && mode != ModelTransformationMode.GROUND) {
            GlaiveParryComponent glaiveParryComponent = GlaiveParryComponent.get(holder);
            if (glaiveParryComponent.getDoubleBoolValue2()) {
                blocking = "_blocking";
            }
            modelId = new ModelIdentifier(Rinvenium.MOD_ID, "glaive_handheld_2d" + blocking + append, "inventory");
        } else {
            modelId = new ModelIdentifier(Rinvenium.MOD_ID, "envinium_glaive" + append, "inventory");
        }
        BakedModel glaiveModel = MinecraftClient.getInstance().getBakedModelManager().getModel(modelId);
        matrices.push();

        if (mode == ModelTransformationMode.GUI) {
            matrices.translate(0.5, 0.5, 0);
        } else if (mode == ModelTransformationMode.THIRD_PERSON_LEFT_HAND) {
            if (blocking.isBlank()) {
                matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(90));
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));
                matrices.translate(-0.5, -1.35, -0.8);
            } else {
                matrices.translate(1.5, 0.5, 0.5);
            }
        } else if (mode == ModelTransformationMode.FIRST_PERSON_LEFT_HAND) {
            if (blocking.isBlank()) {
                matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(45));
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));
                matrices.translate(-0.5, 0, -0.9);
            } else {
                matrices.translate(1.5, 0.5, 0.5);
            }
        } else {
            matrices.translate(0.5, 0.5, 0.5);
        }

        if (texture.equals(EnviniumGlaiveItem.Texture.CREATURE)) {
            if (mode == ModelTransformationMode.THIRD_PERSON_RIGHT_HAND) {
                matrices.scale(1.25f, 1.25f, 1.25f);
                matrices.translate(0, 0, 0);
            } else if (mode == ModelTransformationMode.THIRD_PERSON_LEFT_HAND) {
                matrices.scale(1.25f, 1.25f, 1.25f);
                matrices.translate(0, -0.1875, -0.25);
            }
        } else if (texture.equals(EnviniumGlaiveItem.Texture.INVIS)) {
            if (mode == ModelTransformationMode.THIRD_PERSON_RIGHT_HAND) {
                matrices.scale(1.25f, 1.25f, 1.25f);
                matrices.translate(0, -0.325, 0);
            } else if (mode == ModelTransformationMode.THIRD_PERSON_LEFT_HAND) {
                matrices.scale(1.25f, 1.25f, 1.25f);
                matrices.translate(0, -0.125, 0.125);
            }
        }

        itemRenderer.renderItem(stack, mode, false, matrices, vertexConsumers, light, overlay, glaiveModel);
        matrices.pop();
    }

    private static @NotNull String getGlaiveTexture(EnviniumGlaiveItem.Texture texture) {
        String append = switch (texture) {
            case REMAKE -> "remake";
            case HSTAR -> "hstar";
            case MIDGET -> "midget";
            case CREATURE -> "creature";
            case INVIS -> "invis";
            case HEARTLESS -> "heartless";
            case SHIJAJI -> "shijaji";
            case SCARLET -> "scarlet";
            case HEARTTECH -> "hearttech";
            case DEFAULT -> "default";
            case AVALI -> "avali";
            case ASCENT -> "ascent";
            case KNIGHT -> "knight";
            case RIFTSHATTER -> "riftshatter";
        };
        append = "_" + append;
        return append;
    }
}
