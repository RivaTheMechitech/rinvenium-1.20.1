package riva.rinvenium.items;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import riva.rinvenium.Rinvenium;
import riva.rinvenium.items.tool_materials.EnviniumToolMaterial;

public class RinveniumItems {
    public static EnviniumGlaiveItem ENVINIUM_GLAIVE = (EnviniumGlaiveItem) register("envinium_glaive", new EnviniumGlaiveItem(EnviniumToolMaterial.ENVINIUM, 3, -2.4f, new FabricItemSettings().maxCount(1).rarity(Rarity.RARE)));

    private static Item register(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(Rinvenium.MOD_ID, name), item);
    }

    public static void initialise() {}
}
