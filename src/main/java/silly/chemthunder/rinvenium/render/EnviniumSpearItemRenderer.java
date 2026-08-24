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
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import silly.chemthunder.rinvenium.Rinvenium;
import silly.chemthunder.rinvenium.cca.entity.SpearParryComponent;
import silly.chemthunder.rinvenium.item.EnviniumSpearItem;

public class EnviniumSpearItemRenderer implements BuiltinItemRendererRegistry.DynamicItemRenderer {
    private final Identifier spearId;

    public EnviniumSpearItemRenderer(Identifier spearId) {
        this.spearId = spearId;
    }

    @Override
    public void render(ItemStack stack, ModelTransformationMode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {

        MinecraftClient client = MinecraftClient.getInstance();
        ItemRenderer itemRenderer = client.getItemRenderer();
        ModelIdentifier modelId;
        if (itemRenderer == null) {
            return;
        }
        assert this.spearId != null;
        PlayerEntity player = MinecraftClient.getInstance().player;
        assert player != null;
        boolean leftHanded = player.getMainArm().equals(Arm.LEFT);
        EnviniumSpearItem.Texture texture = EnviniumSpearItem.Texture.DEFAULT;
        if (stack.getItem() instanceof EnviniumSpearItem spearItem) {
            texture = spearItem.getTexture(stack);
        }
        String append = getSpearTexture(texture);
        if (mode != ModelTransformationMode.GUI && mode != ModelTransformationMode.GROUND) {
            SpearParryComponent spearParryComponent = SpearParryComponent.get(MinecraftClient.getInstance().player);
            String blocking;
            if (spearParryComponent.getDoubleBoolValue2()) {
                blocking = "_blocking";
            } else {
                blocking = "";
            }
            modelId = new ModelIdentifier(Rinvenium.MOD_ID, "spear_handheld_2d" + blocking + append, "inventory");
        } else {
            modelId = new ModelIdentifier(Rinvenium.MOD_ID, "envinium_spear" + append, "inventory");
        }
        BakedModel spearModel = MinecraftClient.getInstance().getBakedModelManager().getModel(modelId);
        matrices.push();
        if (mode == ModelTransformationMode.GUI) {
            matrices.translate(0.5, 0.5, 0);
        } else {
            matrices.translate(0.5, 0.5, 0.5);
        }
        itemRenderer.renderItem(stack, mode, leftHanded, matrices, vertexConsumers, light, overlay, spearModel);
        matrices.pop();
    }

    private static @NotNull String getSpearTexture(EnviniumSpearItem.Texture texture) {
        String append = switch (texture) {
            case REMAKE -> "remake";
            case HSTAR -> "hstar";
            case MIDGET -> "midget";
            case CREATURE -> "creature";
            case INVIS -> "invis";
            case HEARTLESS -> "heartless";
            case PLACEHOLDER -> "placeholder";
            case SCARLET -> "scarlet";
            case HEARTTECH -> "hearttech";
            case DEFAULT -> "default";
        };
        append = "_" + append;
        return append;
    }
}
