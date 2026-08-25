package silly.chemthunder.rinvenium.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import silly.chemthunder.rinvenium.cca.entity.EnvixiaFormComponent;
import silly.chemthunder.rinvenium.datagen.RinveniumItemTagProvider;
import silly.chemthunder.rinvenium.index.RinveniumItems;
import silly.chemthunder.rinvenium.index.RinveniumSoundEvents;
import silly.chemthunder.rinvenium.util.ModParticleUtil;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class EnvixiaCoreItem extends Item {
    public static final String INGREDIENTS_KEY = "Ingredients";
    public static final Map<Item, Integer> INGREDIENT_GOAL = Map.of(
            RinveniumItems.ION_CELL, 16,
            RinveniumItems.ENVIXIUS_PLATE, 12,
            Items.BEACON, 1
    );

    public EnvixiaCoreItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (isComplete(stack)) {
            if (EnvixiaArmorItem.hasFullSuit(user)) {
                activateEnvixiaTransformation(world, user);
                user.getItemCooldownManager().set(this, 10);
                return TypedActionResult.success(stack);
            }
        }
        return TypedActionResult.fail(stack);
    }

    public boolean isComplete(ItemStack stack) {
        if (!stack.isOf(this)) return false;
        NbtCompound nbtCompound = stack.getNbt();
        if (nbtCompound != null) {
            NbtList nbtList = nbtCompound.getList(INGREDIENTS_KEY, NbtElement.COMPOUND_TYPE);
            List<ItemStack> ingredients = nbtList.stream().map(NbtCompound.class::cast).map(ItemStack::fromNbt).toList();
            Map<Item, Integer> ingredientMap = new java.util.HashMap<>(Map.of());
            ingredients.forEach(stack1 -> ingredientMap.put(stack1.getItem(), stack1.getCount()));
            return ingredientMap.equals(INGREDIENT_GOAL);
        }
        return false;
    }

    public static boolean armorSlotsEmpty(PlayerEntity user) {
        return user.getInventory().getArmorStack(0).isEmpty()
                && user.getInventory().getArmorStack(1).isEmpty()
                && user.getInventory().getArmorStack(2).isEmpty()
                && user.getInventory().getArmorStack(3).isEmpty();
    }

    private void activateEnvixiaTransformation(World world, PlayerEntity player) {
        /*if (armorSlotsEmpty(player)) {
            player.getInventory().armor.set(PlayerInventory.ARMOR_SLOTS[3], new ItemStack(RinveniumItems.ENVIXIA_HELMET));
            player.getInventory().armor.set(PlayerInventory.ARMOR_SLOTS[2], new ItemStack(RinveniumItems.ENVIXIA_CHESTPLATE));
            player.getInventory().armor.set(PlayerInventory.ARMOR_SLOTS[1], new ItemStack(RinveniumItems.ENVIXIA_LEGGINGS));
            player.getInventory().armor.set(PlayerInventory.ARMOR_SLOTS[0], new ItemStack(RinveniumItems.ENVIXIA_BOOTS));
        }*/
        EnvixiaFormComponent envixiaFormComponent = EnvixiaFormComponent.get(player);

        if (player.getAbilities().flying && envixiaFormComponent.getTripleBoolValue1()) player.getAbilities().flying = false;
        envixiaFormComponent.setTripleBoolValue1(!envixiaFormComponent.getTripleBoolValue1());
        if (world instanceof ServerWorld serverWorld) {
            serverWorld.playSound(null, player.getX(), player.getY(), player.getZ(), envixiaFormComponent.getTripleBoolValue1() ? RinveniumSoundEvents.ENVIXIA_CORE_USE : SoundEvents.BLOCK_BEACON_DEACTIVATE, SoundCategory.PLAYERS,  envixiaFormComponent.getTripleBoolValue1() ? 0.8f : 1.0f, 1.0f);
        }
        if (envixiaFormComponent.getTripleBoolValue1()) {
            ModParticleUtil.addExpandingRingOfParticles(
                    world,
                    player.getPos().add(0, player.getHeight() / 2, 0),
                    0.0,
                    1,
                    100,
                    1.0,
                    ParticleTypes.END_ROD
            );
        }
    }

    @Override
    public boolean onStackClicked(ItemStack stack, Slot slot, ClickType clickType, PlayerEntity player) {
        if (clickType != ClickType.RIGHT) {
            return false;
        } else {
            ItemStack stackInSlot = slot.getStack();
            if (stackInSlot.isEmpty()) {
                return false;
            } else if (stackInSlot.getItem().canBeNested()) {
                if (INGREDIENT_GOAL.containsKey(stackInSlot.getItem())) {
                    int countToAdd = INGREDIENT_GOAL.get(stackInSlot.getItem()) - getStoredStackCount(stack, stackInSlot);
                    int amountAdded = addToCore(stack, slot.takeStackRange(stackInSlot.getCount(), countToAdd, player));

                    if (amountAdded > 0) {
                        this.playInsertSound(player);
                    }
                } else {
                    return false;
                }
            }
            return true;
        }
    }

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        if (clickType == ClickType.RIGHT && slot.canTakePartial(player)) {
            if (!otherStack.isEmpty()) {
                int amountAdded = addToCore(stack, otherStack);

                if (amountAdded > 0) {
                    this.playInsertSound(player);
                    otherStack.decrement(amountAdded);
                }
            }
            return true;
        } else {
            return false;
        }
    }

    private static int addToCore(ItemStack core, ItemStack stack) {
        if (!stack.isEmpty() && stack.isIn(RinveniumItemTagProvider.ENVIXIA_CORE_INGREDIENTS) && stack.getItem().canBeNested()) {
            NbtCompound nbtCompound = core.getOrCreateNbt();
            if (!nbtCompound.contains(INGREDIENTS_KEY)) {
                nbtCompound.put(INGREDIENTS_KEY, new NbtList());
            }

            int currentStackInCoreCount = getStoredStackCount(core, stack);
            int countToAdd = Math.min(stack.getCount(), INGREDIENT_GOAL.get(stack.getItem()) - currentStackInCoreCount);

            if (countToAdd == 0) {
                return 0;
            } else {
                NbtList ingredientList = nbtCompound.getList(INGREDIENTS_KEY, NbtElement.COMPOUND_TYPE);
                Optional<NbtCompound> optional = canMergeStack(stack, ingredientList);

                if (optional.isPresent()) {
                    NbtCompound optionalCompound = optional.get();
                    ItemStack optionalStack = ItemStack.fromNbt(optionalCompound);

                    optionalStack.increment(countToAdd);
                    optionalStack.writeNbt(optionalCompound);
                    ingredientList.remove(optionalCompound);
                    ingredientList.add(0, optionalCompound);
                } else {
                    ItemStack inputStack = stack.copyWithCount(countToAdd);
                    NbtCompound inputCompound = new NbtCompound();

                    inputStack.writeNbt(inputCompound);
                    ingredientList.add(0, inputCompound);
                }
                return countToAdd;
            }
        } else {
            return 0;
        }
    }

    private static int getStoredStackCount(ItemStack core, ItemStack stack) {
        List<ItemStack> list = getStoredStacks(core).toList();
        List<Item> listToItem = new java.util.ArrayList<>(List.of());

        list.forEach(itemStack -> listToItem.add(itemStack.getItem()));
        if (listToItem.contains(stack.getItem())) {
            int index = listToItem.indexOf(stack.getItem());
            return list.get(index).getCount();
        }
        return 0;
    }

    private static Optional<NbtCompound> canMergeStack(ItemStack stack, NbtList ingredientList) {
        return !stack.isIn(RinveniumItemTagProvider.ENVIXIA_CORE_INGREDIENTS)
                ? Optional.empty()
                : ingredientList.stream()
                .filter(NbtCompound.class::isInstance)
                .map(NbtCompound.class::cast)
                .filter(item -> ItemStack.canCombine(ItemStack.fromNbt(item), stack))
                .findFirst();
    }

    @Override
    public void onItemEntityDestroyed(ItemEntity entity) {
        ItemUsage.spawnItemContents(entity, getStoredStacks(entity.getStack()));
    }

    private static Stream<ItemStack> getStoredStacks(ItemStack stack) {
        NbtCompound nbtCompound = stack.getNbt();

        if (nbtCompound == null) {
            return Stream.empty();
        } else {
            NbtList ingredientList = nbtCompound.getList(INGREDIENTS_KEY, NbtElement.COMPOUND_TYPE);
            return ingredientList.stream().map(NbtCompound.class::cast).map(ItemStack::fromNbt);
        }
    }

    private void playInsertSound(Entity entity) {
        entity.playSound(SoundEvents.BLOCK_BEACON_POWER_SELECT, 1.0F, 0.8F + entity.getWorld().getRandom().nextFloat() * 0.4F);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        NbtCompound nbtCompound = stack.getNbt();
        int cellCount = 0;
        int plateCount = 0;
        int beaconCount = 0;
        
        if (nbtCompound != null) {
            NbtList nbtList = nbtCompound.getList(INGREDIENTS_KEY, NbtElement.COMPOUND_TYPE);
            List<ItemStack> ingredients = nbtList.stream().map(NbtCompound.class::cast).map(ItemStack::fromNbt).toList();
            List<Item> ingredientItems = new java.util.ArrayList<>(List.of());
            ingredients.forEach(itemStack -> ingredientItems.add(itemStack.getItem()));
            if (ingredientItems.contains(RinveniumItems.ION_CELL)) {
                cellCount = ingredients.get(ingredientItems.indexOf(RinveniumItems.ION_CELL)).getCount();
            }
            if (ingredientItems.contains(RinveniumItems.ENVIXIUS_PLATE)) {
                plateCount = ingredients.get(ingredientItems.indexOf(RinveniumItems.ENVIXIUS_PLATE)).getCount();
            }
            if (ingredientItems.contains(Items.BEACON)) {
                beaconCount = ingredients.get(ingredientItems.indexOf(Items.BEACON)).getCount();
            }
        }
        tooltip.add(Text.literal("- ").append(RinveniumItems.ION_CELL.getName()).append(Text.literal(": " + cellCount + "/" + INGREDIENT_GOAL.get(RinveniumItems.ION_CELL))).formatted(cellCount == INGREDIENT_GOAL.get(RinveniumItems.ION_CELL) ? Formatting.GREEN : Formatting.RED));
        tooltip.add(Text.literal("- ").append(RinveniumItems.ENVIXIUS_PLATE.getName()).append(Text.literal(": " + plateCount + "/" + INGREDIENT_GOAL.get(RinveniumItems.ENVIXIUS_PLATE))).formatted(plateCount == INGREDIENT_GOAL.get(RinveniumItems.ENVIXIUS_PLATE) ? Formatting.GREEN : Formatting.RED));
        tooltip.add(Text.literal("- ").append(Items.BEACON.getName()).append(Text.literal(": " + beaconCount + "/" + INGREDIENT_GOAL.get(Items.BEACON))).formatted(beaconCount == INGREDIENT_GOAL.get(Items.BEACON) ? Formatting.GREEN : Formatting.RED));
    }
}