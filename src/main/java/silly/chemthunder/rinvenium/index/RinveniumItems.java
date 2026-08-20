package silly.chemthunder.rinvenium.index;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import silly.chemthunder.rinvenium.Rinvenium;
import silly.chemthunder.rinvenium.item.*;
import silly.chemthunder.rinvenium.item.tool.EnviniumToolMaterial;

import java.util.LinkedHashMap;
import java.util.Map;

public interface RinveniumItems {
    Map<Item, Identifier> ITEMS = new LinkedHashMap<>();

    Item EMPTY = create("empty", new Item(new FabricItemSettings()));
    Item DEBUGGER = create("debugger", new DebuggerItem(new Item.Settings().maxCount(1).fireproof()));
    Item ENVINIUM_SPEAR = create("envinium_spear", new EnviniumSpearItem(EnviniumToolMaterial.ENVINIUM, 3, -2.4f, new Item.Settings().maxCount(1)));
    Item HAIL_OF_THE_GODS = create("hail_of_the_gods", new HotGItem(new FabricItemSettings().maxCount(1)));
    Item DAEDALUS_STORMSHOT = create("daedalus_stormshot", new DaedalusStormshotItem(new FabricItemSettings().maxCount(1)));

    Item AURIO_INGOT = create("aurio_ingot", new DescriptionItem(new FabricItemSettings(), "aurio_ingot", 2));
    Item ENVINIA_INGOT = create("envinia_ingot", new DescriptionItem(new FabricItemSettings().fireproof(), "envinia_ingot", 2));
    Item ENVIXIUS_INGOT = create("envixius_ingot", new DescriptionItem(new FabricItemSettings().fireproof(), "envixius_ingot", 2));
    Item ENVIXIUS_PLATE = create("envixius_plate", new DescriptionItem(new FabricItemSettings().fireproof(), "envixius_plate", 2));
    Item SUPERHEATED_AURIO_INGOT = create("superheated_aurio_ingot", new HotItem(new FabricItemSettings(), AURIO_INGOT, "superheated_aurio_ingot", 2));
    Item SUPERHEATED_ENVINIA_INGOT = create("superheated_envinia_ingot", new HotItem(new FabricItemSettings(), ENVINIA_INGOT, "superheated_envinia_ingot", 2));
    Item SUPERHEATED_ENVIXIUS_INGOT = create("superheated_envixius_ingot", new HotItem(new FabricItemSettings().fireproof(), ENVIXIUS_INGOT, "superheated_envixius_ingot", 2));
    Item SUPERHEATED_ENVIXIUS_PLATE = create("superheated_envixius_plate", new HotItem(new FabricItemSettings().fireproof(), ENVIXIUS_PLATE, "superheated_envixius_plate", 1));
    Item BATTERY = create("battery", new Item(new FabricItemSettings().food(RinveniumFoodComponents.BATTERY)));
    Item ION_CELL = create("ion_cell", new DescriptionItem(new FabricItemSettings().food(RinveniumFoodComponents.ION_CELL), "ion_cell", 1));

    Item ENVIXIA_CORE = create("envixia_core", new EnvixiaCoreItem(new FabricItemSettings().maxCount(1).fireproof()));
    Item ENVIXIA_HELMET = create("envixia_helmet", new EnvixiaArmorItem(RinveniumArmorMaterials.ENIVIXIA, ArmorItem.Type.HELMET, new FabricItemSettings().maxCount(1).fireproof()));
    Item ENVIXIA_CHESTPLATE = create("envixia_chestplate", new EnvixiaArmorItem(RinveniumArmorMaterials.ENIVIXIA, ArmorItem.Type.CHESTPLATE, new FabricItemSettings().maxCount(1).fireproof()));
    Item ENVIXIA_LEGGINGS = create("envixia_leggings", new EnvixiaArmorItem(RinveniumArmorMaterials.ENIVIXIA, ArmorItem.Type.LEGGINGS, new FabricItemSettings().maxCount(1).fireproof()));
    Item ENVIXIA_BOOTS = create("envixia_boots", new EnvixiaArmorItem(RinveniumArmorMaterials.ENIVIXIA, ArmorItem.Type.BOOTS, new FabricItemSettings().maxCount(1).fireproof()));

    static <T extends Item> T create(String name, T item) {
        ITEMS.put(item, Rinvenium.id(name));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(RinveniumItems::addCombatEntries);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(RinveniumItems::addIngredientEntries);
        return item;
    }

    private static void addCombatEntries(FabricItemGroupEntries fabricItemGroupEntries) {
        fabricItemGroupEntries.addAfter(Items.TOTEM_OF_UNDYING,
                ENVINIUM_SPEAR,
                HAIL_OF_THE_GODS,
                ENVIXIA_CORE,
                ENVIXIA_HELMET,
                ENVIXIA_CHESTPLATE,
                ENVIXIA_LEGGINGS,
                ENVIXIA_BOOTS
        );
    }

    private static void addIngredientEntries(FabricItemGroupEntries fabricItemGroupEntries) {
        fabricItemGroupEntries.addAfter(Items.NETHERITE_INGOT,
                AURIO_INGOT,
                SUPERHEATED_AURIO_INGOT,
                ENVINIA_INGOT,
                SUPERHEATED_ENVINIA_INGOT,
                ENVIXIUS_INGOT,
                SUPERHEATED_ENVIXIUS_INGOT,
                ENVIXIUS_PLATE,
                SUPERHEATED_ENVIXIUS_PLATE,
                BATTERY,
                ION_CELL
        );
    }

    static void init() {
        ITEMS.forEach((item, id) -> Registry.register(Registries.ITEM, id, item));
    }
}