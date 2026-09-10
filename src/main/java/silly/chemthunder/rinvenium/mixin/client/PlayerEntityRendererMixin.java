package silly.chemthunder.rinvenium.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import silly.chemthunder.rinvenium.entity.client.EnvixiaArmorRenderer;
import silly.chemthunder.rinvenium.entity.client.EnvixiaArmorModel;
import silly.chemthunder.rinvenium.index.RinveniumEnchantments;
import silly.chemthunder.rinvenium.index.RinveniumItems;
import silly.chemthunder.rinvenium.index.client.RinveniumEntityModelLayers;
import silly.chemthunder.rinvenium.item.EnvixiaArmorItem;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
    public PlayerEntityRendererMixin(EntityRendererFactory.Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    @Inject(method = "getArmPose", at = @At("HEAD"), cancellable = true)
    private static void armPose(AbstractClientPlayerEntity player, Hand hand, CallbackInfoReturnable<BipedEntityModel.ArmPose> cir) {
        if (player.getStackInHand(hand).isOf(RinveniumItems.ENVINIUM_SPEAR) && !(EnchantmentHelper.getLevel(RinveniumEnchantments.RUSH, player.getStackInHand(hand)) > 0) && player.isUsingItem()) {
            cir.setReturnValue(BipedEntityModel.ArmPose.BOW_AND_ARROW);
        }
        if (player.getStackInHand(hand).isOf(RinveniumItems.HAIL_OF_THE_GODS) || player.getStackInHand(hand).isOf(RinveniumItems.APMDSC)) {
            cir.setReturnValue(BipedEntityModel.ArmPose.BOW_AND_ARROW);
        }
    }

    @Inject(method = "setModelPose", at = @At(value = "TAIL"))
    private void rinvenium$derenderPlayerModel(AbstractClientPlayerEntity player, CallbackInfo ci, @Local PlayerEntityModel<AbstractClientPlayerEntity> model) {
        if (!player.isSpectator()) {
            if (player.getInventory().getArmorStack(2).isOf(RinveniumItems.ENVIXIA_CHESTPLATE)) {
                model.body.visible = false;
                model.jacket.visible = false;
                model.rightSleeve.visible = false;
                model.leftSleeve.visible = false;
            }
            if (player.getInventory().getArmorStack(1).isOf(RinveniumItems.ENVIXIA_LEGGINGS) || player.getInventory().getArmorStack(0).isOf(RinveniumItems.ENVIXIA_BOOTS)) {
                model.rightPants.visible = false;
                model.leftPants.visible = false;
                if (player.getInventory().getArmorStack(1).isOf(RinveniumItems.ENVIXIA_LEGGINGS) && player.getInventory().getArmorStack(0).isOf(RinveniumItems.ENVIXIA_BOOTS)) {
                    model.rightLeg.visible = false;
                    model.leftLeg.visible = false;
                }
            }
        }
    }
}