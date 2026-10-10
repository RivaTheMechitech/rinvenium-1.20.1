package silly.chemthunder.rinvenium.mixin.client;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.TridentRiptideFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import silly.chemthunder.rinvenium.Rinvenium;
import silly.chemthunder.rinvenium.index.RinveniumItems;

@Mixin(TridentRiptideFeatureRenderer.class)
public abstract class TridentFeatureRendererMixin {
    @Unique private static final Identifier GLAIVE_RIPTIDE_TEXTURE = Rinvenium.id("textures/entity/glaive_riptide.png");

    @ModifyVariable(method = "render", at = @At("STORE"))
    private VertexConsumer rinvenium$glaiveRiptide(VertexConsumer orig, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, LivingEntity livingEntity) {
        if (livingEntity instanceof PlayerEntity && livingEntity.isUsingRiptide() && (livingEntity.getMainHandStack().getItem() == RinveniumItems.ENVINIUM_GLAIVE || (livingEntity.getOffHandStack().getItem() == RinveniumItems.ENVINIUM_GLAIVE))) {
            return vertexConsumerProvider.getBuffer(RenderLayer.getEntityTranslucentEmissive(GLAIVE_RIPTIDE_TEXTURE));
        }
        return orig;
    }
}