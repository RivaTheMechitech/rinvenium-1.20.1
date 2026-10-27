package silly.chemthunder.rinvenium.mixin.client;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.resource.SynchronousResourceReloader;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import silly.chemthunder.rinvenium.render.manager.client.ImpactFrameManager;
import silly.chemthunder.rinvenium.util.inject.client.RenderContainer;

@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin implements SynchronousResourceReloader {
    @WrapWithCondition(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/EntityRenderDispatcher;renderShadow(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/entity/Entity;FFLnet/minecraft/world/WorldView;F)V"))
    private boolean rinvenium$cancelShadowRendering(MatrixStack matrices, VertexConsumerProvider vertexConsumers, Entity entity, float opacity, float tickDelta, WorldView world, float radius) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            ImpactFrameManager impactFrameManager = ((RenderContainer) client.player).getImpactFrameManager();
            return impactFrameManager.get().isEmpty() || !impactFrameManager.shouldShow();
        }
        return true;
    }

    @WrapWithCondition(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/EntityRenderDispatcher;renderFire(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/entity/Entity;)V"))
    private boolean rinvenium$cancelFireRendering(EntityRenderDispatcher instance, MatrixStack matrices, VertexConsumerProvider vertexConsumers, Entity entity) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            ImpactFrameManager impactFrameManager = ((RenderContainer) client.player).getImpactFrameManager();
            return impactFrameManager.get().isEmpty() || !impactFrameManager.shouldShow();
        }
        return true;
    }

    @WrapWithCondition(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/EntityRenderDispatcher;renderHitbox(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/entity/Entity;F)V"))
    private boolean rinvenium$cancelHitboxRendering(MatrixStack matrices, VertexConsumer vertices, Entity entity, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            ImpactFrameManager impactFrameManager = ((RenderContainer) client.player).getImpactFrameManager();
            return impactFrameManager.get().isEmpty() || !impactFrameManager.shouldShow();
        }
        return true;
    }
}
