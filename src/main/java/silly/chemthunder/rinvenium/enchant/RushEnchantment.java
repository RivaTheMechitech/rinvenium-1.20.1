package silly.chemthunder.rinvenium.enchant;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import silly.chemthunder.rinvenium.index.RinveniumItems;

public class RushEnchantment extends Enchantment {
    public int getMinPower(int level) {return 20;}
    public int getMaxPower(int level) {return 50;}
    public int getMaxLevel() {return 3;}

    public RushEnchantment(Rarity weight, EquipmentSlot... slot) {
        super(weight, EnchantmentTarget.WEAPON, slot);
    }

    public boolean isAcceptableItem(ItemStack stack) {
        return stack.isOf(RinveniumItems.ENVINIUM_GLAIVE) || stack.isOf(Items.BOOK) || stack.isOf(Items.ENCHANTED_BOOK);
    }
}