package silly.chemthunder.rinvenium.enchant;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import silly.chemthunder.rinvenium.index.RinveniumItems;

public class HighPowerOutputEnchantment extends Enchantment {

    public HighPowerOutputEnchantment(Rarity weight, EquipmentSlot... slots) {
        super(weight, EnchantmentTarget.WEAPON, slots);
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.isOf(RinveniumItems.APMDSC) || stack.isOf(Items.BOOK);
    }

    @Override
    public int getMaxPower(int level) {
        return 50;
    }

    @Override
    public int getMinPower(int level) {
        return 20;
    }

    @Override
    protected boolean canAccept(Enchantment other) {
        return false;
    }
}
