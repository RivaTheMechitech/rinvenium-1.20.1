package silly.chemthunder.rinvenium.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import silly.chemthunder.rinvenium.cca.RinveniumComponents;
import silly.chemthunder.rinvenium.cca.entity.APMDSCComponent;
import silly.chemthunder.rinvenium.cca.item.APMDSCItemComponent;
import silly.chemthunder.rinvenium.index.RinveniumItems;

public class APMDSCItem extends Item {
    public static final int ON_FIRE_TIME = 100; // Time the player is set on fire for upon overheat
    public static final int OVERHEAT_CD_TIME = 100; // Time the item is on cooldown upon overheat

    public APMDSCItem(Settings settings) {
        super(settings);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.NONE;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        // Cancels all if this item is in offhand
        if (hand == Hand.OFF_HAND) return TypedActionResult.fail(stack);

        APMDSCComponent entityComponent = APMDSCComponent.get(user);
        APMDSCItemComponent itemComponent = RinveniumComponents.APMDSC_ITEM.get(stack);

        // Recharging
        if (user.getOffHandStack().isOf(RinveniumItems.ION_CELL) && user.isSneaking()) {
            itemComponent.addIonCellCount(1);
            user.getOffHandStack().decrement(1);
            user.getItemCooldownManager().set(this, 5); // ICD on the recharge so it doesn't suffer from bad ping and possibly bug out due to ping issues
            return TypedActionResult.success(stack);
        }

        if (!world.isClient) {
            entityComponent.incrementInt();
        }

        return TypedActionResult.consume(stack);
    }


    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }
}
