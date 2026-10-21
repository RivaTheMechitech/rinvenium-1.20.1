package silly.chemthunder.rinvenium.mixin.client;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import silly.chemthunder.rinvenium.cca.entity.EnvixiaFormComponent;
import silly.chemthunder.rinvenium.render.manager.client.ImpactFrameManager;
import silly.chemthunder.rinvenium.util.inject.client.RenderContainer;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {
    @Shadow private @Nullable PostEffectProcessor entityOutlinePostProcessor;
    @Shadow public abstract @Nullable Framebuffer getEntityOutlinesFramebuffer();
    @Shadow @Final private BufferBuilderStorage bufferBuilders;

    @Shadow
    @Final
    private MinecraftClient client;

    @WrapOperation(
        method = "render",
         at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/Entity;getTeamColorValue()I"
        )
    )
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
    
    @ModifyArg(
        method = "render",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/OutlineVertexConsumerProvider;setColor(IIII)V"
        ),
        index = 3
    )
    private int rinvenium$envixiaOutlineColorAlpha(int alpha, @Local Entity entity) {
        if (entity instanceof PlayerEntity player) {
            EnvixiaFormComponent envixiaFormComponent = EnvixiaFormComponent.get(player);
            return envixiaFormComponent.getTripleBoolValue1() ? 80 : alpha;
        } else {
            return alpha;
        }
    }

    @WrapWithCondition(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;renderSky(Lnet/minecraft/client/util/math/MatrixStack;Lorg/joml/Matrix4f;FLnet/minecraft/client/render/Camera;ZLjava/lang/Runnable;)V"))
    private boolean rinvenium$cancelSkyRendering(WorldRenderer instance, MatrixStack matrices, Matrix4f projectionMatrix, float tickDelta, Camera camera, boolean thickFog, Runnable fogCallback) {
        if (client.player != null) {
            ImpactFrameManager impactFrameManager = ((RenderContainer) client.player).getImpactFrameManager();
            return impactFrameManager.get().isEmpty() || !impactFrameManager.shouldShow();
        }
        return true;
    }

    @WrapWithCondition(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/BackgroundRenderer;applyFog(Lnet/minecraft/client/render/Camera;Lnet/minecraft/client/render/BackgroundRenderer$FogType;FZF)V"))
    private boolean rinvenium$cancelFogRendering(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, float tickDelta) {
        if (client.player != null) {
            ImpactFrameManager impactFrameManager = ((RenderContainer) client.player).getImpactFrameManager();
            return impactFrameManager.get().isEmpty() || !impactFrameManager.shouldShow();
        }
        return true;
    }

    @WrapWithCondition(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;renderLayer(Lnet/minecraft/client/render/RenderLayer;Lnet/minecraft/client/util/math/MatrixStack;DDDLorg/joml/Matrix4f;)V"))
    private boolean rinvenium$cancelRenderLayerRendering(WorldRenderer instance, RenderLayer renderLayer, MatrixStack matrices, double cameraX, double cameraY, double cameraZ, Matrix4f positionMatrix) {
        if (client.player != null) {
            ImpactFrameManager impactFrameManager = ((RenderContainer) client.player).getImpactFrameManager();
            return impactFrameManager.get().isEmpty() || !impactFrameManager.shouldShow();
        }
        return true;
    }

    @WrapWithCondition(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/DiffuseLighting;enableForLevel(Lorg/joml/Matrix4f;)V"))
    private boolean rinvenium$cancelTerrainRendering4(Matrix4f positionMatrix) {
        if (client.player != null) {
            ImpactFrameManager impactFrameManager = ((RenderContainer) client.player).getImpactFrameManager();
            return impactFrameManager.get().isEmpty() || !impactFrameManager.shouldShow();
        }
        return true;
    }

    @WrapWithCondition(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/DiffuseLighting;disableForLevel(Lorg/joml/Matrix4f;)V"))
    private boolean rinvenium$cancelTerrainRendering5(Matrix4f positionMatrix) {
        if (client.player != null) {
            ImpactFrameManager impactFrameManager = ((RenderContainer) client.player).getImpactFrameManager();
            return impactFrameManager.get().isEmpty() || !impactFrameManager.shouldShow();
        }
        return true;
    }

    @WrapWithCondition(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;renderEntity(Lnet/minecraft/entity/Entity;DDDFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;)V"))
    private boolean rinvenium$cancelEntityRendering(WorldRenderer instance, Entity entity, double cameraX, double cameraY, double cameraZ, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers) {
        if (client.player != null) {
            ImpactFrameManager impactFrameManager = ((RenderContainer) client.player).getImpactFrameManager();
            return impactFrameManager.get().isEmpty() || !impactFrameManager.shouldShow();
        }
        return true;
    }

    @WrapWithCondition(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;drawCurrentLayer()V"))
    private boolean rinvenium$cancelImmediateRendering(VertexConsumerProvider.Immediate instance) {
        if (client.player != null) {
            ImpactFrameManager impactFrameManager = ((RenderContainer) client.player).getImpactFrameManager();
            return impactFrameManager.get().isEmpty() || !impactFrameManager.shouldShow();
        }
        return true;
    }

    @WrapWithCondition(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;draw()V"))
    private boolean rinvenium$cancelImmediateDrawRendering(VertexConsumerProvider.Immediate instance) {
        if (client.player != null) {
            ImpactFrameManager impactFrameManager = ((RenderContainer) client.player).getImpactFrameManager();
            return impactFrameManager.get().isEmpty() || !impactFrameManager.shouldShow();
        }
        return true;
    }

    @WrapWithCondition(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;draw(Lnet/minecraft/client/render/RenderLayer;)V"))
    private boolean rinvenium$cancelImmediateDrawRendering(VertexConsumerProvider.Immediate instance, RenderLayer layer) {
        if (client.player != null) {
            ImpactFrameManager impactFrameManager = ((RenderContainer) client.player).getImpactFrameManager();
            return impactFrameManager.get().isEmpty() || !impactFrameManager.shouldShow();
        }
        return true;
    }

    @WrapWithCondition(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/block/entity/BlockEntityRenderDispatcher;render(Lnet/minecraft/block/entity/BlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;)V", ordinal = 0))
    private <E extends BlockEntity> boolean rinvenium$cancelBlockEntityRendering1(BlockEntityRenderDispatcher instance, E blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers) {
        if (client.player != null) {
            ImpactFrameManager impactFrameManager = ((RenderContainer) client.player).getImpactFrameManager();
            return impactFrameManager.get().isEmpty() || !impactFrameManager.shouldShow();
        }
        return true;
    }

    @WrapWithCondition(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/block/entity/BlockEntityRenderDispatcher;render(Lnet/minecraft/block/entity/BlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;)V", ordinal = 1))
    private <E extends BlockEntity> boolean rinvenium$cancelBlockEntityRendering2(BlockEntityRenderDispatcher instance, E blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers) {
        if (client.player != null) {
            ImpactFrameManager impactFrameManager = ((RenderContainer) client.player).getImpactFrameManager();
            return impactFrameManager.get().isEmpty() || !impactFrameManager.shouldShow();
        }
        return true;
    }

    /*@WrapWithCondition(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;draw(Lnet/minecraft/client/render/RenderLayer;)V", ordinal = 4))
    private boolean rinvenium$cancelBlockEntityRendering3(VertexConsumerProvider.Immediate instance, RenderLayer layer) {
        if (client.player != null) {
            ImpactFrameManager impactFrameManager = ((RenderContainer) client.player).getImpactFrameManager();
            return impactFrameManager.get().isEmpty() || !impactFrameManager.shouldShow();
        }
        return true;
    }*/

    @WrapWithCondition(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;drawBlockOutline(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/entity/Entity;DDDLnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V"))
    private boolean rinvenium$cancelOutlineRendering(WorldRenderer instance, MatrixStack matrices, VertexConsumer vertexConsumer, Entity entity, double cameraX, double cameraY, double cameraZ, BlockPos pos, BlockState state) {
        if (client.player != null) {
            ImpactFrameManager impactFrameManager = ((RenderContainer) client.player).getImpactFrameManager();
            return impactFrameManager.get().isEmpty() || !impactFrameManager.shouldShow();
        }
        return true;
    }

    @WrapWithCondition(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/debug/DebugRenderer;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;DDD)V"))
    private boolean rinvenium$cancelDebugRendering(DebugRenderer instance, MatrixStack matrices, VertexConsumerProvider.Immediate vertexConsumers, double cameraX, double cameraY, double cameraZ) {
        if (client.player != null) {
            ImpactFrameManager impactFrameManager = ((RenderContainer) client.player).getImpactFrameManager();
            return impactFrameManager.get().isEmpty() || !impactFrameManager.shouldShow();
        }
        return true;
    }

    @WrapWithCondition(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/ParticleManager;renderParticles(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;Lnet/minecraft/client/render/LightmapTextureManager;Lnet/minecraft/client/render/Camera;F)V"))
    private boolean rinvenium$cancelParticleRendering(ParticleManager instance, MatrixStack matrices, VertexConsumerProvider.Immediate vertexConsumers, LightmapTextureManager lightmapTextureManager, Camera camera, float tickDelta) {
        if (client.player != null) {
            ImpactFrameManager impactFrameManager = ((RenderContainer) client.player).getImpactFrameManager();
            return impactFrameManager.get().isEmpty() || !impactFrameManager.shouldShow();
        }
        return true;
    }

    @WrapWithCondition(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;renderClouds(Lnet/minecraft/client/util/math/MatrixStack;Lorg/joml/Matrix4f;FDDD)V"))
    private boolean rinvenium$cancelCloudRendering(WorldRenderer instance, MatrixStack matrices, Matrix4f projectionMatrix, float tickDelta, double cameraX, double cameraY, double cameraZ) {
        if (client.player != null) {
            ImpactFrameManager impactFrameManager = ((RenderContainer) client.player).getImpactFrameManager();
            return impactFrameManager.get().isEmpty() || !impactFrameManager.shouldShow();
        }
        return true;
    }

    @WrapWithCondition(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;renderWeather(Lnet/minecraft/client/render/LightmapTextureManager;FDDD)V"))
    private boolean rinvenium$cancelWeatherRendering(WorldRenderer instance, LightmapTextureManager manager, float tickDelta, double cameraX, double cameraY, double cameraZ) {
        if (client.player != null) {
            ImpactFrameManager impactFrameManager = ((RenderContainer) client.player).getImpactFrameManager();
            return impactFrameManager.get().isEmpty() || !impactFrameManager.shouldShow();
        }
        return true;
    }

    @WrapWithCondition(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;renderWorldBorder(Lnet/minecraft/client/render/Camera;)V"))
    private boolean rinvenium$cancelWorldBorderRendering(WorldRenderer instance, Camera camera) {
        if (client.player != null) {
            ImpactFrameManager impactFrameManager = ((RenderContainer) client.player).getImpactFrameManager();
            return impactFrameManager.get().isEmpty() || !impactFrameManager.shouldShow();
        }
        return true;
    }

    @WrapWithCondition(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gl/PostEffectProcessor;render(F)V"))
    private boolean rinvenium$cancelPostProcessorRendering(PostEffectProcessor instance, float tickDelta) {
        if (client.player != null) {
            ImpactFrameManager impactFrameManager = ((RenderContainer) client.player).getImpactFrameManager();
            return impactFrameManager.get().isEmpty() || !impactFrameManager.shouldShow();
        }
        return true;
    }


}