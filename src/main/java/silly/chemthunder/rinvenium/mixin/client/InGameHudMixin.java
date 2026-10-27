package silly.chemthunder.rinvenium.mixin.client;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import silly.chemthunder.rinvenium.cca.entity.EnvixiaFormComponent;
import silly.chemthunder.rinvenium.index.RinveniumItems;
import silly.chemthunder.rinvenium.render.manager.client.FlashManager;
import silly.chemthunder.rinvenium.render.manager.client.ImpactFrameManager;
import silly.chemthunder.rinvenium.util.RinveniumTextureUtils;
import silly.chemthunder.rinvenium.util.inject.client.RenderContainer;

@SuppressWarnings("DiscouragedShift")
@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Shadow @Final private MinecraftClient client;
    @Shadow protected abstract int getHeartCount(LivingEntity entity);
    @Shadow protected abstract int getHeartRows(int heartCount);
    @Shadow protected abstract void renderOverlay(DrawContext context, Identifier texture, float opacity);

    @Shadow private int scaledWidth;
    @Shadow private int scaledHeight;

    @Inject(
        method = "render",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/network/ClientPlayerEntity;getFrozenTicks()I",
            shift = At.Shift.BEFORE
        )
    )
    private void rinvenium$renderCustomCrosshair(DrawContext context, float tickDelta, CallbackInfo ci) {
        if (this.client != null && this.client.player != null && this.client.options.getPerspective().isFirstPerson()) {
            if (this.client.player.getStackInHand(Hand.MAIN_HAND).isOf(RinveniumItems.HAIL_OF_THE_GODS)) {
                int i = context.getScaledWindowWidth() / 2;
                int j = context.getScaledWindowHeight() / 2;
                context.drawTexture(RinveniumTextureUtils.HAIL_OF_THE_GODS_CROSSHAIR, i - 7, j - 8, 0, 0, 0, 15, 15, 15, 15);
            }
        }
    }

    @WrapWithCondition(
        method = "renderCrosshair",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V",
            ordinal = 0
        )
    )
    private boolean rinvenium$derenderCrosshair(DrawContext instance, Identifier texture, int x, int y, int u, int v, int width, int height) {
        return this.client != null && this.client.player != null && !this.client.player.getStackInHand(Hand.MAIN_HAND).isOf(RinveniumItems.HAIL_OF_THE_GODS);
    }

    @WrapOperation(
        method = "renderStatusBars",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/client/gui/hud/InGameHud;ICONS:Lnet/minecraft/util/Identifier;",
            ordinal = 3,
            opcode = Opcodes.GETSTATIC
        )
    )
    private Identifier rinvenium$envixiaHungerBarBG(Operation<Identifier> original) {
        ClientPlayerEntity clientPlayer = MinecraftClient.getInstance().player;
        if (clientPlayer != null) {
            EnvixiaFormComponent envixiaFormComponent = EnvixiaFormComponent.get(clientPlayer);
            if (envixiaFormComponent.getTripleBoolValue1()) {
                return RinveniumTextureUtils.ICONS;
            }
        }
        return original.call();
    }

    @WrapOperation(
        method = "renderStatusBars",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/client/gui/hud/InGameHud;ICONS:Lnet/minecraft/util/Identifier;",
            ordinal = 4,
            opcode = Opcodes.GETSTATIC
        )
    )
    private Identifier rinvenium$envixiaHungerBarHalf(Operation<Identifier> original) {
        ClientPlayerEntity clientPlayer = MinecraftClient.getInstance().player;
        if (clientPlayer != null) {
            EnvixiaFormComponent envixiaFormComponent = EnvixiaFormComponent.get(clientPlayer);
            if (envixiaFormComponent.getTripleBoolValue1()) {
                return RinveniumTextureUtils.ICONS;
            }
        }
        return original.call();
    }

    @WrapOperation(
        method = "renderStatusBars",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/client/gui/hud/InGameHud;ICONS:Lnet/minecraft/util/Identifier;",
            ordinal = 5,
            opcode = Opcodes.GETSTATIC
        )
    )
    private Identifier rinvenium$envixiaHungerBarFull(Operation<Identifier> original) {
        ClientPlayerEntity clientPlayer = MinecraftClient.getInstance().player;
        if (clientPlayer != null) {
            EnvixiaFormComponent envixiaFormComponent = EnvixiaFormComponent.get(clientPlayer);
            if (envixiaFormComponent.getTripleBoolValue1()) {
                return RinveniumTextureUtils.ICONS;
            }
        }
        return original.call();
    }

    /*@Inject(method = "renderStatusBars", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;getProfiler()Lnet/minecraft/util/profiler/Profiler;", ordinal = 2))
    private void rinvenium$flightBar(DrawContext context, CallbackInfo ci, @Local PlayerEntity playerEntity, @Local LivingEntity livingEntity, @Local(ordinal = 11) int t, @Local(ordinal = 5) int n) {
        int maxTime = 60;
        EnvixiaFormComponent envixiaFormComponent = EnvixiaFormComponent.get(playerEntity);
        int time = envixiaFormComponent.getInt();
        int rowOffset = this.getHeartRows(this.getHeartCount(livingEntity)) - 1;
        t -= rowOffset * 10;
        if (envixiaFormComponent.getTripleBoolValue1()) {
            context.drawTexture(RinveniumTextureUtils.FLY_BAR_BG, n - 80, t, 0, 0, 80, 8);
        }
    }*/

    @Inject(
        method = "render",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/network/ClientPlayerEntity;getFrozenTicks()I",
            shift = At.Shift.BEFORE
        )
    )
    private void rinvenium$renderOverlays(DrawContext context, float tickDelta, CallbackInfo ci) {
        if (this.client != null && this.client.player != null) {
            FlashManager flashManager = ((RenderContainer) this.client.player).getFlashManager();
            flashManager.tick();
            flashManager.get().forEach(flash -> {
                if (flash.age >= flash.maxAge + flash.fadeTime) {
                    flash.fadeTicks--;
                    this.renderOverlay(
                            context,
                            flash.color,
                            RinveniumTextureUtils.SCREEN_FLASH,
                            (float) Math.min(flash.fadeTicks, flash.fadeTime) * flash.maxOpacity / flash.fadeTime
                    );
                } else if (flash.age <= flash.fadeTime) {
                    flash.fadeTicks++;
                    this.renderOverlay(
                            context,
                            flash.color,
                            RinveniumTextureUtils.SCREEN_FLASH,
                            (float) Math.min(flash.fadeTicks, flash.fadeTime) * flash.maxOpacity / flash.fadeTime
                    );
                } else {
                    this.renderOverlay(
                            context,
                            flash.color,
                            RinveniumTextureUtils.SCREEN_FLASH,
                            flash.maxOpacity
                    );
                }
            });

            ImpactFrameManager impactFrameManager = ((RenderContainer) this.client.player).getImpactFrameManager();
            impactFrameManager.tick();
            /*impactFrameManager.get().forEach(impactFrame -> {
                if (impactFrame.age >= impactFrame.maxAge + impactFrame.fadeTime) {
                    impactFrame.fadeTicks--;
                    this.renderOverlay(
                            context,
                            impactFrame.color,
                            RinveniumTextureUtils.SCREEN_FLASH,
                            (float) Math.min(impactFrame.fadeTicks, impactFrame.fadeTime) * impactFrame.maxOpacity / impactFrame.fadeTime
                    );
                } else if (impactFrame.age <= impactFrame.fadeTime) {
                    impactFrame.fadeTicks++;
                    this.renderOverlay(
                            context,
                            impactFrame.color,
                            RinveniumTextureUtils.SCREEN_FLASH,
                            (float) Math.min(impactFrame.fadeTicks, impactFrame.fadeTime) * impactFrame.maxOpacity / impactFrame.fadeTime
                    );
                } else {
                    this.renderOverlay(
                            context,
                            impactFrame.color,
                            RinveniumTextureUtils.SCREEN_FLASH,
                            impactFrame.maxOpacity
                    );
                }
            });*/
        }
    }

    @Unique
    private void renderOverlay(DrawContext context, int color, Identifier texture, float opacity) {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        /* Bit shifting hex colors into that fuckass 256^3 ratio */
        float r = (color >> 16 & 0xFF) / 255.0f;
        float g = (color >> 8 & 0xFF) / 255.0f;
        float b = (color & 0xFF) / 255.0f;
        context.setShaderColor(r, g, b, opacity);
        context.drawTexture(texture, 0, 0, -90, 0.0F, 0.0F, this.scaledWidth, this.scaledHeight, this.scaledWidth, this.scaledHeight);
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        context.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
}