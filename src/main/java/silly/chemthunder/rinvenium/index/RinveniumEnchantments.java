package silly.chemthunder.rinvenium.index;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import silly.chemthunder.rinvenium.Rinvenium;
import silly.chemthunder.rinvenium.enchant.HighPowerOutputEnchantment;
import silly.chemthunder.rinvenium.enchant.LowPowerOutputEnchantment;
import silly.chemthunder.rinvenium.enchant.RushEnchantment;
import silly.chemthunder.rinvenium.enchant.StarstruckEnchantment;

import java.util.LinkedHashMap;
import java.util.Map;

public interface RinveniumEnchantments {
    Map<Enchantment, Identifier> ENCHANTMENTS = new LinkedHashMap<>();

    Enchantment RUSH = createEnchantment("rush", new RushEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND));
    Enchantment HIGH_POWER_OUTPUT = createEnchantment("high_power_output", new HighPowerOutputEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND));
    Enchantment LOW_POWER_OUTPUT = createEnchantment("low_power_output", new LowPowerOutputEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND));
    Enchantment STARSTRUCK = createEnchantment("starstruck", new StarstruckEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND));

    private static Enchantment createEnchantment(String name, Enchantment enchantment) {
        ENCHANTMENTS.put(enchantment, new Identifier(Rinvenium.MOD_ID, name));
        return enchantment;
    }

    static void init() {
        ENCHANTMENTS.keySet().forEach(enchantment -> Registry.register(Registries.ENCHANTMENT, ENCHANTMENTS.get(enchantment), enchantment));
    }
}