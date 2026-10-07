package silly.chemthunder.rinvenium.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import silly.chemthunder.rinvenium.index.RinveniumItems;

import java.util.List;
import java.util.UUID;

public class EnvixiaArmorItem extends ArmorItem {
    public static final UUID SPEED_MODIFIER = UUID.fromString("ce533a3f-53cd-4f82-986b-ef22f42475a0");
    public static final String SPEED_MODIFIER_ID = "Envixia suit speed";
    private boolean isFullSuit = false;

    public EnvixiaArmorItem(ArmorMaterial material, Type type, Settings settings) {
        super(material, type, settings);
    }

    public static boolean hasFullSuit(PlayerEntity player) {
        return player.getInventory().getArmorStack(0).isOf(RinveniumItems.ENVIXIA_BOOTS)
                && player.getInventory().getArmorStack(1).isOf(RinveniumItems.ENVIXIA_LEGGINGS)
                && player.getInventory().getArmorStack(2).isOf(RinveniumItems.ENVIXIA_CHESTPLATE)
                && player.getInventory().getArmorStack(3).isOf(RinveniumItems.ENVIXIA_HELMET);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (entity instanceof PlayerEntity player) {
            if (hasFullSuit(player)) {
                if (!player.getAttributes().hasModifierForAttribute(EntityAttributes.GENERIC_MOVEMENT_SPEED, SPEED_MODIFIER)) {
                    player.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).addTemporaryModifier(new EntityAttributeModifier(
                            SPEED_MODIFIER,
                            SPEED_MODIFIER_ID,
                            0.3,
                            EntityAttributeModifier.Operation.MULTIPLY_TOTAL
                    ));
                }
                this.isFullSuit = true;
            } else {
                if (player.getAttributes().hasModifierForAttribute(EntityAttributes.GENERIC_MOVEMENT_SPEED, SPEED_MODIFIER)) {
                    player.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).removeModifier(SPEED_MODIFIER);
                }
                this.isFullSuit = false;
            }
        }
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return stack.isOf(RinveniumItems.ENVIXIA_CHESTPLATE);
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        return 0;
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        return 0x5af6bf;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        tooltip.add(Text.literal("Full suit effect: ").formatted(this.isFullSuit ? Formatting.GRAY : Formatting.DARK_GRAY).append(Text.literal("+30% Speed").formatted(this.isFullSuit ? Formatting.BLUE : Formatting.GRAY)));
        super.appendTooltip(stack, world, tooltip, context);
    }
}