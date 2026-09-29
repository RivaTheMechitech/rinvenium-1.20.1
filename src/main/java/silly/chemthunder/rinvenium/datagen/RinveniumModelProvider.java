package silly.chemthunder.rinvenium.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.item.Items;

import static net.minecraft.data.client.Models.GENERATED;
import static net.minecraft.data.client.Models.HANDHELD;
import static silly.chemthunder.rinvenium.index.RinveniumItems.APMDSC;
import static silly.chemthunder.rinvenium.index.RinveniumItems.AURIO_INGOT;
import static silly.chemthunder.rinvenium.index.RinveniumItems.BATTERY;
import static silly.chemthunder.rinvenium.index.RinveniumItems.DEBUGGER;
import static silly.chemthunder.rinvenium.index.RinveniumItems.ENVINIA_INGOT;
import static silly.chemthunder.rinvenium.index.RinveniumItems.ENVIXIA_BOOTS;
import static silly.chemthunder.rinvenium.index.RinveniumItems.ENVIXIA_CHESTPLATE;
import static silly.chemthunder.rinvenium.index.RinveniumItems.ENVIXIA_HELMET;
import static silly.chemthunder.rinvenium.index.RinveniumItems.ENVIXIA_LEGGINGS;
import static silly.chemthunder.rinvenium.index.RinveniumItems.ENVIXIUS_INGOT;
import static silly.chemthunder.rinvenium.index.RinveniumItems.ENVIXIUS_PLATE;
import static silly.chemthunder.rinvenium.index.RinveniumItems.ION_CELL;
import static silly.chemthunder.rinvenium.index.RinveniumItems.SUPERHEATED_AURIO_INGOT;
import static silly.chemthunder.rinvenium.index.RinveniumItems.SUPERHEATED_ENVINIA_INGOT;
import static silly.chemthunder.rinvenium.index.RinveniumItems.SUPERHEATED_ENVIXIUS_INGOT;
import static silly.chemthunder.rinvenium.index.RinveniumItems.SUPERHEATED_ENVIXIUS_PLATE;

public class RinveniumModelProvider extends FabricModelProvider {
    public RinveniumModelProvider(FabricDataOutput output) {
        super(output);
    }

    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {}

    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(DEBUGGER, Items.STICK, HANDHELD);

        itemModelGenerator.register(APMDSC, Items.PRISMARINE_SHARD, GENERATED);

        itemModelGenerator.register(AURIO_INGOT, GENERATED);
        itemModelGenerator.register(ENVINIA_INGOT, GENERATED);
        itemModelGenerator.register(ENVIXIUS_INGOT, GENERATED);
        itemModelGenerator.register(ENVIXIUS_PLATE, GENERATED);

        itemModelGenerator.register(SUPERHEATED_AURIO_INGOT, GENERATED);
        itemModelGenerator.register(SUPERHEATED_ENVINIA_INGOT, GENERATED);
        itemModelGenerator.register(SUPERHEATED_ENVIXIUS_INGOT, GENERATED);
        itemModelGenerator.register(SUPERHEATED_ENVIXIUS_PLATE, GENERATED);
        
        itemModelGenerator.register(BATTERY, GENERATED);
        itemModelGenerator.register(ION_CELL, GENERATED);

        //itemModelGenerator.register(ENVIXIA_CORE, GENERATED);
        itemModelGenerator.register(ENVIXIA_HELMET, GENERATED);
        itemModelGenerator.register(ENVIXIA_CHESTPLATE, GENERATED);
        itemModelGenerator.register(ENVIXIA_LEGGINGS, GENERATED);
        itemModelGenerator.register(ENVIXIA_BOOTS, GENERATED);


        // for (Item value : RinveniumItems.ITEMS) {
        //     if (value != HAIL_OF_THE_GODS && value != DEBUGGER) {
        //             itemModelGenerator.register(value, Models.GENERATED);
        //     }
        // }
        // maybe quick thing to optimize this idfk
        // nah cuz we dont have that many items that warrant groupings like that. Most of the custom items have custom gui models anyway
    }
}