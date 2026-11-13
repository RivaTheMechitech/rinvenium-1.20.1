package silly.chemthunder.rinvenium.mixin.client;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import silly.chemthunder.rinvenium.entity.client.CustomPosing;
import silly.chemthunder.rinvenium.index.RinveniumItems;

@Mixin(BipedEntityModel.class)
public abstract class BipedEntityModelMixin<T extends LivingEntity> extends AnimalModel<T> {
    @Shadow
    @Final
    public ModelPart rightArm;

    @Shadow
    @Final
    public ModelPart leftArm;

    @Shadow
    @Final
    public ModelPart head;

    @Inject(method = "positionRightArm", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/CrossbowPosing;hold(Lnet/minecraft/client/model/ModelPart;Lnet/minecraft/client/model/ModelPart;Lnet/minecraft/client/model/ModelPart;Z)V"))
    private void rinvenium$apmsdcHoldRH(T entity, CallbackInfo ci) {
        if (entity.getMainHandStack() != null && entity.getMainHandStack().isOf(RinveniumItems.APMSDC)) {
            CustomPosing.holdAPMSDC(this.rightArm, this.leftArm, this.head, true);
        }
    }

    @Inject(method = "positionLeftArm", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/CrossbowPosing;hold(Lnet/minecraft/client/model/ModelPart;Lnet/minecraft/client/model/ModelPart;Lnet/minecraft/client/model/ModelPart;Z)V"))
    private void rinvenium$apmsdcHoldLH(T entity, CallbackInfo ci) {
        if (entity.getMainHandStack() != null && entity.getMainHandStack().isOf(RinveniumItems.APMSDC)) {
            CustomPosing.holdAPMSDC(this.rightArm, this.leftArm, this.head, false);
        }
    }
}
