package silly.chemthunder.rinvenium.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import silly.chemthunder.rinvenium.entity.CoreChargeGrenadeEntity;
import silly.chemthunder.rinvenium.index.RinveniumEntities;

public class CoreChargeGrenadeItem extends Item {
    public CoreChargeGrenadeItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (!world.isClient) {
            CoreChargeGrenadeEntity coreChargeGrenadeEntity = new CoreChargeGrenadeEntity(RinveniumEntities.CORE_CHARGE_GRENADE, world);
            coreChargeGrenadeEntity.setOwner(user);
            coreChargeGrenadeEntity.setItem(itemStack);
            coreChargeGrenadeEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 1.5F, 1.0F);
            coreChargeGrenadeEntity.updatePosition(user.getX(), user.getEyeY() - 0.15, user.getZ());
            world.spawnEntity(coreChargeGrenadeEntity);
        }

        user.incrementStat(Stats.USED.getOrCreateStat(this));
        if (!user.getAbilities().creativeMode) {
            itemStack.decrement(1);
        }

        return TypedActionResult.success(itemStack, world.isClient());
    }
}
