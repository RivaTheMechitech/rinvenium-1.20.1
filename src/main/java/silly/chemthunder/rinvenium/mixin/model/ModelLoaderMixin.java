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
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "envinium_glaive_default", "inventory"));
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "envinium_glaive_remake", "inventory"));
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "envinium_glaive_hstar", "inventory"));
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "envinium_glaive_midget", "inventory"));
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "envinium_glaive_creature", "inventory"));
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "envinium_glaive_invis", "inventory"));
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "envinium_glaive_heartless", "inventory"));
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "envinium_glaive_shijaji", "inventory"));
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "envinium_glaive_scarlet", "inventory"));
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "envinium_glaive_hearttech", "inventory"));
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "envinium_glaive_ascent", "inventory"));
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "envinium_glaive_knight", "inventory"));
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "envinium_glaive_avali", "inventory"));
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "envinium_glaive_riftshatter", "inventory"));
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "glaive_handheld_2d_default", "inventory"));
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "glaive_handheld_2d_remake", "inventory"));
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "glaive_handheld_2d_hstar", "inventory"));
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "glaive_handheld_2d_midget", "inventory"));
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "glaive_handheld_2d_creature", "inventory"));
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "glaive_handheld_2d_invis", "inventory"));
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "glaive_handheld_2d_heartless", "inventory"));
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "glaive_handheld_2d_shijaji", "inventory"));
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "glaive_handheld_2d_scarlet", "inventory"));
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "glaive_handheld_2d_hearttech", "inventory"));
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "glaive_handheld_2d_ascent", "inventory"));
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "glaive_handheld_2d_knight", "inventory"));
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "glaive_handheld_2d_avali", "inventory"));
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "glaive_handheld_2d_riftshatter", "inventory"));
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "glaive_handheld_2d_blocking_default", "inventory"));
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "glaive_handheld_2d_blocking_remake", "inventory"));
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "glaive_handheld_2d_blocking_hstar", "inventory"));
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "glaive_handheld_2d_blocking_midget", "inventory"));
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "glaive_handheld_2d_blocking_creature", "inventory"));
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "glaive_handheld_2d_blocking_invis", "inventory"));
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "glaive_handheld_2d_blocking_heartless", "inventory"));
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "glaive_handheld_2d_blocking_shijaji", "inventory"));
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "glaive_handheld_2d_blocking_scarlet", "inventory"));
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "glaive_handheld_2d_blocking_hearttech", "inventory"));
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "glaive_handheld_2d_blocking_ascent", "inventory"));
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "glaive_handheld_2d_blocking_knight", "inventory"));
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "glaive_handheld_2d_blocking_avali", "inventory"));
        this.addModel(new ModelIdentifier(Rinvenium.MOD_ID, "glaive_handheld_2d_blocking_riftshatter", "inventory"));
    }
}