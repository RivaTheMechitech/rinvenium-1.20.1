package silly.chemthunder.rinvenium.mixin.model;

import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import silly.chemthunder.rinvenium.Rinvenium;

import java.util.Map;

/**
 * This class was created by Vowxky.
 * All rights reserved to the developer.
 */

@Mixin(ModelLoader.class)
public abstract class ModelLoaderMixin {
    @Shadow protected abstract void addModel(ModelIdentifier modelId);

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/model/ModelLoader;addModel(Lnet/minecraft/client/util/ModelIdentifier;)V", ordinal = 3, shift = At.Shift.AFTER))
    public void addModels(BlockColors blockColors, Profiler profiler, Map jsonUnbakedModels, Map blockStates, CallbackInfo ci) {
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "envinium_spear_default", "inventory"));
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "envinium_spear_remake", "inventory"));
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "envinium_spear_hstar", "inventory"));
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "envinium_spear_midget", "inventory"));
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "envinium_spear_creature", "inventory"));
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "envinium_spear_invis", "inventory"));
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "envinium_spear_heartless", "inventory"));
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "envinium_spear_shijaji", "inventory"));
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "envinium_spear_scarlet", "inventory"));
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "envinium_spear_hearttech", "inventory"));
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "spear_handheld_2d_default", "inventory"));
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "spear_handheld_2d_remake", "inventory"));
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "spear_handheld_2d_hstar", "inventory"));
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "spear_handheld_2d_midget", "inventory"));
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "spear_handheld_2d_creature", "inventory"));
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "spear_handheld_2d_invis", "inventory"));
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "spear_handheld_2d_heartless", "inventory"));
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "spear_handheld_2d_shijaji", "inventory"));
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "spear_handheld_2d_scarlet", "inventory"));
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "spear_handheld_2d_hearttech", "inventory"));
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "spear_handheld_2d_blocking_default", "inventory"));
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "spear_handheld_2d_blocking_remake", "inventory"));
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "spear_handheld_2d_blocking_hstar", "inventory"));
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "spear_handheld_2d_blocking_midget", "inventory"));
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "spear_handheld_2d_blocking_creature", "inventory"));
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "spear_handheld_2d_blocking_invis", "inventory"));
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "spear_handheld_2d_blocking_heartless", "inventory"));
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "spear_handheld_2d_blocking_shijaji", "inventory"));
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "spear_handheld_2d_blocking_scarlet", "inventory"));
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "spear_handheld_2d_blocking_hearttech", "inventory"));
    }
}