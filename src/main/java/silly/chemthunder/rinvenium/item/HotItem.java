package silly.chemthunder.rinvenium.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import silly.chemthunder.rinvenium.util.RinveniumUtil;

public class HotItem extends DescriptionItem {
    public final Item quenchedItem;

    public HotItem(Settings settings, Item quenchedItem, String itemName) {
        super(settings, itemName);
        this.quenchedItem = quenchedItem;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (!entity.isOnFire() && !entity.isWet()) {
            entity.setOnFire(true);
            entity.setOnFireFor(1);
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        BlockHitResult blockHitResult = raycast(world, user, RaycastContext.FluidHandling.SOURCE_ONLY);

        if (blockHitResult.getType() == HitResult.Type.MISS) {
            return TypedActionResult.pass(itemStack);
        } else {
            if (blockHitResult.getType() == HitResult.Type.BLOCK) {
                BlockPos blockPos = blockHitResult.getBlockPos();
                if (!world.canPlayerModifyAt(user, blockPos)) {
                    return TypedActionResult.pass(itemStack);
                }
                if (world.getFluidState(blockPos).isIn(FluidTags.WATER)) {
                    world.playSound(user, user.getX(), user.getY(), user.getZ(), SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.NEUTRAL, 1.0f, 1.0f);
                    Item item = itemStack.getItem();
                    user.setStackInHand(hand, RinveniumUtil.exchangeWholeStack(itemStack, user, new ItemStack(quenchedItem)));
                    user.incrementStat(Stats.USED.getOrCreateStat(item));
                    return TypedActionResult.success(itemStack);
                }
            }
            return TypedActionResult.pass(itemStack);
        }
    }
}