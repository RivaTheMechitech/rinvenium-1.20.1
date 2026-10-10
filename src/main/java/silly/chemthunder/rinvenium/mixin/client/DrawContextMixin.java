package silly.chemthunder.rinvenium.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import silly.chemthunder.rinvenium.cca.entity.EnvixiaFormComponent;
import silly.chemthunder.rinvenium.cca.entity.HailOfTheGodComponent;
import silly.chemthunder.rinvenium.cca.entity.GlaiveDashingComponent;
import silly.chemthunder.rinvenium.cca.entity.GlaiveParryComponent;
import silly.chemthunder.rinvenium.index.RinveniumEnchantments;
import silly.chemthunder.rinvenium.index.RinveniumItems;

@Mixin(DrawContext.class)
public abstract class DrawContextMixin {
    @Shadow @Final private MinecraftClient client;
    @Shadow public abstract void fill(RenderLayer layer, int x1, int y1, int x2, int y2, int color);

    @Inject(
        method = "drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/DrawContext;fill(Lnet/minecraft/client/render/RenderLayer;IIIII)V",
            ordinal = 1
        )
    )
    private void rinvenium$drawCustomItemBars(TextRenderer textRenderer, ItemStack stack, int x, int y, String countOverride, CallbackInfo ci) {
        if (this.client.player != null) {
            int k2 = x + 2;
            int l2 = y + 13;

            if (stack.isOf(RinveniumItems.ENVINIUM_GLAIVE)) {
                GlaiveParryComponent glaiveParryComponent = GlaiveParryComponent.get(this.client.player);
                GlaiveDashingComponent glaiveDashingComponent = GlaiveDashingComponent.get(this.client.player);

                if (EnchantmentHelper.getLevel(RinveniumEnchantments.RUSH, stack) > 0) {
                    int m = (int) (glaiveDashingComponent.getChargePercent() * 13);

                    this.fill(RenderLayer.getGuiOverlay(), k2, l2, k2 + 13, l2 + 2, -16777216);
                    this.fill(RenderLayer.getGuiOverlay(), k2, l2, k2 + m, l2 + 1, 0x9cfdff | 0xFF000000);
                } else {
                    int i = (int) Math.ceil(glaiveParryComponent.getDoubleIntValue2() > 0 ? glaiveParryComponent.getDamageWindowPercentage() * 13 : glaiveParryComponent.getParryWindowPercentage() * 13);
                    int j = glaiveParryComponent.getDoubleIntValue2() > 0 ? 0x7a1c8c : 0xfdc211;

                    this.fill(RenderLayer.getGuiOverlay(), k2, l2, k2 + 13, l2 + 2, -16777216);
                    this.fill(RenderLayer.getGuiOverlay(), k2, l2, k2 + i, l2 + 1, j | 0xFF000000);
                }
            } else if (stack.isOf(RinveniumItems.HAIL_OF_THE_GODS)) {
                HailOfTheGodComponent hailOfTheGodComponent = HailOfTheGodComponent.get(this.client.player);
                int useTime = (int) Math.floor((double) hailOfTheGodComponent.getDoubleIntValue1() / HailOfTheGodComponent.MAX_USE_TIME * 13);
                int overheatTime = (int) Math.floor((double) hailOfTheGodComponent.getDoubleIntValue2() / HailOfTheGodComponent.MAX_OVERHEAT_TIME * 13);

                this.fill(RenderLayer.getGuiOverlay(), k2, l2, k2 + 13, l2 + 2, -16777216);
                this.fill(RenderLayer.getGuiOverlay(), k2, l2, k2 + useTime, l2 + 1, 0x9cfdff | 0xFF000000);
                this.fill(RenderLayer.getGuiOverlay(), k2, l2, k2 + overheatTime, l2 + 1, 0xfdc211 | 0xFF000000);
            } else if (stack.isOf(RinveniumItems.ENVIXIA_CHESTPLATE)) {
                EnvixiaFormComponent envixiaFormComponent = EnvixiaFormComponent.get(this.client.player);
                int flyTime = envixiaFormComponent.getInt();
                int timeRemaining = (int) MathHelper.clamp(Math.ceil((double) (60 - flyTime) / 60 * 13), 0, 60);
                int cooldownTime = (int) Math.floor(13 - this.client.player.getItemCooldownManager().getCooldownProgress(RinveniumItems.ENVIXIA_CHESTPLATE, 0.0f) * 13);

                this.fill(RenderLayer.getGuiOverlay(), k2, l2, k2 + 13, l2 + 2, -16777216);
                if (this.client.player.getItemCooldownManager().isCoolingDown(RinveniumItems.ENVIXIA_CHESTPLATE)) {
                    this.fill(RenderLayer.getGuiOverlay(), k2, l2, k2 + cooldownTime, l2 + 1, 0x1a4b3a | 0xFF000000);
                } else {
                    this.fill(RenderLayer.getGuiOverlay(), k2, l2, k2 + timeRemaining, l2 + 1, 0x5af6bf | 0xFF000000);
                }
            }
        }
    }
}