package silly.chemthunder.rinvenium.mixin.model;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import silly.chemthunder.rinvenium.index.RinveniumItems;
import silly.chemthunder.rinvenium.render.EnviniumSpearItemRenderer;


@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    @Inject(
        method = "renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/world/World;III)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/item/ItemRenderer;getModel(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;I)Lnet/minecraft/client/render/model/BakedModel;"
        )
    )
    private void rinvenium$storeEntity(LivingEntity entity, ItemStack item, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, World world, int light, int overlay, int seed, CallbackInfo ci) {
        if (item.isOf(RinveniumItems.ENVINIUM_SPEAR) && entity instanceof PlayerEntity player) {
            EnviniumSpearItemRenderer.holder = player;
        }
    }

    /*
      This class was created by Vowxky.
      All rights reserved to the developer.

     */
    /*
    @ModifyVariable(method = "renderItem", at = @At(value = "HEAD"), argsOnly = true)
    public BakedModel useModel(BakedModel value, ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        PlayerEntity player = MinecraftClient.getInstance().player;
        if (player != null && entity != null) {
            if (stack.isOf(RinveniumItems.ENVINIUM_SPEAR)) {
                EnviniumSpearItem.Texture texture = EnviniumSpearItem.Texture.DEFAULT;
                if (stack.getItem() instanceof EnviniumSpearItem spearItem) {
                    texture = spearItem.getTexture(stack);
                }
                if (renderMode != ModelTransformationMode.GUI && renderMode != ModelTransformationMode.GROUND) {
                    SpearParryComponent spearParryComponent = SpearParryComponent.get(entity);
                    String blocking;
                    if (spearParryComponent.getDoubleBoolValue2()) {
                        blocking = "_blocking";
                    } else {
                        blocking = "";
                    }
                    String append = getSpearTexture(texture);
                    return ((ItemRendererAccessor) this).renderer$getModels().getModelManager().getModel(new ModelIdentifier(Rinvenium.MOD_ID, "spear_handheld_2d" + blocking + append, "inventory"));
                }/* else {
                    String append = getSpearTexture(texture);
                    return ((ItemRendererAccessor) this).renderer$getModels().getModelManager().getModel(new ModelIdentifier(Rinvenium.MOD_ID, "envinium_spear" + append, "inventory"));
                }
            }
        }
        return value;
    }*/
/*
    @Unique
    private static @NotNull String getSpearTexture(EnviniumSpearItem.Texture texture) {
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
        };
        append = "_" + append;
        return append;
    }*/
}