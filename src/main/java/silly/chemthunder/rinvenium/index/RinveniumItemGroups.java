package silly.chemthunder.rinvenium.index;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import silly.chemthunder.rinvenium.Rinvenium;

import static silly.chemthunder.rinvenium.index.RinveniumItems.*;

public class RinveniumItemGroups {
    public static final ItemGroup RINVENIUM_GROUP = Registry.register(Registries.ITEM_GROUP,
            new Identifier(Rinvenium.MOD_ID, "rinvenium_group"),
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.rinvenium"))
                    .icon(() -> new ItemStack(ENVINIUM_SPEAR)).entries((displayContext, entries) -> {
                        entries.add(ENVINIUM_SPEAR);
                        entries.add(HAIL_OF_THE_GODS);
                        entries.add(ENVIXIA_CORE);
                        entries.add(ENVIXIA_HELMET);
                        entries.add(ENVIXIA_CHESTPLATE);
                        entries.add(ENVIXIA_LEGGINGS);
                        entries.add(ENVIXIA_BOOTS);
                        //entries.add(CORE_CHARGE_GRENADE);
                        //entries.add(APMDSC);
                        entries.add(AURIO_INGOT);
                        entries.add(SUPERHEATED_AURIO_INGOT);
                        entries.add(ENVINIA_INGOT);
                        entries.add(SUPERHEATED_ENVINIA_INGOT);
                        entries.add(ENVIXIUS_INGOT);
                        entries.add(SUPERHEATED_ENVIXIUS_INGOT);
                        entries.add(ENVIXIUS_PLATE);
                        entries.add(SUPERHEATED_ENVIXIUS_PLATE);
                        entries.add(BATTERY);
                        entries.add(ION_CELL);
                    }).build());
    public static void registerItemGroups() {

    }
}
