package silly.chemthunder.rinvenium.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import silly.chemthunder.rinvenium.render.manager.client.ImpactFrameManager;
import silly.chemthunder.rinvenium.render.manager.server.CustomFogManager;
import silly.chemthunder.rinvenium.util.inject.client.RenderContainer;

@Mixin(BackgroundRenderer.class)
public abstract class BackgroundRendererMixin {
    @Shadow
    private static float red;

    @Shadow
    private static float green;

    @Shadow
    private static float blue;

    @Shadow
    public static void setFogBlack() {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Inject(method = "render", at = @At(value = "TAIL"))
    private static void rinvenium$whiteoutBackground(Camera camera, float tickDelta, ClientWorld world, int viewDistance, float skyDarkness, CallbackInfo ci) {
        MinecraftClient client =  MinecraftClient.getInstance();
        if (client.player != null) {
            ImpactFrameManager impactFrameManager = ((RenderContainer) client.player).getImpactFrameManager();
            if (!impactFrameManager.get().isEmpty() && impactFrameManager.shouldShow()) {
                red = 1.0f;
                green = 1.0f;
                blue = 1.0f;
                RenderSystem.clearColor(red, green, blue, 1.0f);
            } else if (!CustomFogManager.get().isEmpty()){
                CustomFogManager.get().forEach(customFog -> {
                    red = customFog.red;
                    green = customFog.green;
                    blue = customFog.blue;
                    setFogBlack();
                    RenderSystem.clearColor(red, green, blue, 0.0f);
                });
            }
        }
    }
}
