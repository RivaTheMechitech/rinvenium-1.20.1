package riva.rinvenium.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;
import riva.rinvenium.Rinvenium;
import riva.rinvenium.items.RinveniumItems;

public class RinveniumModels extends FabricModelProvider {
    public RinveniumModels(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {

    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        Models.GENERATED.upload(ModelIds.getItemModelId(RinveniumItems.ENVINIUM_GLAIVE), TextureMap.layer0(Identifier.of(Rinvenium.MOD_ID, "item/glaive_gui_default")), itemModelGenerator.writer);
    }
}
