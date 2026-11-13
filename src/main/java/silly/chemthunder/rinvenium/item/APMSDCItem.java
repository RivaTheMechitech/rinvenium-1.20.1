package silly.chemthunder.rinvenium.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import silly.chemthunder.rinvenium.cca.RinveniumComponents;
import silly.chemthunder.rinvenium.cca.entity.APMSDCComponent;
import silly.chemthunder.rinvenium.cca.item.APMSDCItemComponent;
import silly.chemthunder.rinvenium.index.RinveniumEnchantments;
import silly.chemthunder.rinvenium.index.RinveniumItems;
import silly.chemthunder.rinvenium.index.RinveniumStatusEffects;
import silly.chemthunder.rinvenium.util.RinveniumUtil;
import silly.chemthunder.rinvenium.util.inject.SyncedRenderContainer;

import java.util.List;

import static silly.chemthunder.rinvenium.cca.item.APMSDCItemComponent.*;
import static silly.chemthunder.rinvenium.item.APMSDCItem.BeamType.*;

public class APMSDCItem extends Item {
    public static final int RELOAD_ICD = 10;
    public static final int SHOOT_COOLDOWN = 60;

    public APMSDCItem(Settings settings) {
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

        APMSDCComponent entityComponent = APMSDCComponent.get(user);
        APMSDCItemComponent itemComponent = RinveniumComponents.APMSDC_ITEM.get(stack);

        boolean hasStarstruck = EnchantmentHelper.getLevel(RinveniumEnchantments.STARSTRUCK, stack) > 0;

        // Recharging
        if (user.getOffHandStack().isOf(RinveniumItems.ION_CELL) && user.isSneaking()) {
            if (!itemComponent.isCharging() && itemComponent.getIonCellCount() == 0) {
                itemComponent.setIsCharging(true);
            } else if (itemComponent.getIonCellCount() >= MAX_ION_CELL_COUNT) {
                itemComponent.setIsCharging(false);
                return TypedActionResult.fail(stack);
            }
            if (itemComponent.isCharging()) {
                itemComponent.addIonCellCount(1);
                if (itemComponent.getIonCellCount() >= MAX_ION_CELL_COUNT) {
                    itemComponent.setIsCharging(false);
                }
                user.getOffHandStack().decrement(1);
                user.getItemCooldownManager().set(this, RELOAD_ICD); // ICD on the recharge so it doesn't suffer from bad ping and possibly bug out due to ping issues and cuz it just feels cooler to slowly load charges
                return TypedActionResult.success(stack);
            }
        }

        // Starstruck Shooting
        if (!world.isClient && hasStarstruck) {
            if (!itemComponent.isCharging()) {
                if (itemComponent.getIonCellCount() > 0) {
                    if (itemComponent.getStarstruckCount() >= MAX_STARSTRUCK_COUNT) {
                        itemComponent.setStarstruckCount(0);
                        itemComponent.addIonCellCount(-1);
                    } else {
                        itemComponent.addStarstruckCount(1);
                    }
                    // todo implement starstruck
                    SnowballEntity snowball = new SnowballEntity(world, user);
                    snowball.setPosition(user.getEyePos().add(user.getRotationVector().normalize().multiply(0.5)));
                    snowball.setVelocity(user, user.getPitch(), user.getYaw(), 0.0f, 1.5f, 0.0f);
                    world.spawnEntity(snowball);
                    user.getItemCooldownManager().set(this, RELOAD_ICD);
                    return TypedActionResult.success(stack, false);
                } else {
                    return TypedActionResult.fail(stack);
                }
            }
            return TypedActionResult.fail(stack);
        }

        // Beam Shooting
        if (!world.isClient) {
            if (!itemComponent.isCharging()) {
                if (entityComponent.getInt() >= calculateMaxShootTimeForEnchant(stack)) {
                    user.getItemCooldownManager().set(this, SHOOT_COOLDOWN);
                    entityComponent.setBool(false);
                } else if (itemComponent.getIonCellCount() > 0 && !user.getItemCooldownManager().isCoolingDown(this)) {
                    user.setCurrentHand(hand);
                    entityComponent.setBool(true);
                }
                return itemComponent.getIonCellCount() > 0 ? TypedActionResult.success(stack, false) : TypedActionResult.fail(stack);
            }
        }

        return TypedActionResult.fail(stack);
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        PlayerEntity player = (PlayerEntity) user;
        APMSDCComponent entityComponent = APMSDCComponent.get(player);
        APMSDCItemComponent itemComponent = RinveniumComponents.APMSDC_ITEM.get(stack);
        if (user.getActiveHand() == Hand.OFF_HAND) {
            user.stopUsingItem();
            entityComponent.setBool(false);
            ((PlayerEntity) user).getItemCooldownManager().set(this, SHOOT_COOLDOWN);
            ((SyncedRenderContainer) player).setShouldRenderAPMSDCBeam(false);
        }

        // Shoot time
        if (entityComponent.getInt() >= calculateMaxShootTimeForEnchant(stack)) {
            player.stopUsingItem();
        } else if (entityComponent.getBool()){
            entityComponent.incrementInt();
        }

        // Damaging entities
        Vec3d startPos = player.getEyePos().add(player.getRotationVector().normalize().multiply(0.5));
        HitResult hitResult = player.raycast(6.0, 0.0f, false);
        Vec3d endPos;
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockHitResult = (BlockHitResult) hitResult;
            endPos = blockHitResult.getPos();
        } else {
            endPos = hitResult.getPos();
        }
        if (entityComponent.getInt() > calculateWindUpTimeForEnchant(stack)) {
            Box box = new Box(startPos, endPos).expand(0.375);
            List<LivingEntity> targets = world.getEntitiesByClass(LivingEntity.class, box, living -> living.isAlive() && living.canHit() && !living.isSpectator());
            targets.remove(player);
            for (LivingEntity target : targets) {
                target.addStatusEffect(new StatusEffectInstance(this.getStatusEffectForEnchant(stack), 10, 0));
            }
        }

        // Wind up particles
        if (entityComponent.getInt() < calculateWindUpTimeForEnchant(stack)) {
            world.addParticle(ParticleTypes.PORTAL, player.getX(), player.getY(), player.getZ(), 0.0D, 0.0D, 0.0D);
        }

        // Shoot beam
        if (entityComponent.getInt() == calculateWindUpTimeForEnchant(stack)) {
            if (!((SyncedRenderContainer) player).shouldRenderAPMSDCBeam()) {
                ((SyncedRenderContainer) player).setShouldRenderAPMSDCBeam(true);
            }
        }
    }
    public final float balls = (float) 1 / 2;
    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        PlayerEntity player = (PlayerEntity) user;
        APMSDCComponent entityComponent = APMSDCComponent.get(player);
        APMSDCItemComponent itemComponent = RinveniumComponents.APMSDC_ITEM.get(stack);
        boolean lowPower = EnchantmentHelper.getLevel(RinveniumEnchantments.LOW_POWER_OUTPUT, stack) > 0;

        if (entityComponent.getInt() > calculateWindUpTimeForEnchant(stack)) {
            player.getItemCooldownManager().set(this, SHOOT_COOLDOWN);
            itemComponent.addIonCellCount(calculateIonCellDecrementForEnchant(stack));
            if (lowPower) {
                if (itemComponent.getLowPowerCount() >= MAX_LOW_POWER_COUNT) {
                    itemComponent.setLowPowerCount(0);
                } else {
                    itemComponent.addLowPowerCount(1);
                }
            }
            ((SyncedRenderContainer) player).setShouldRenderAPMSDCBeam(false);
        }
        ((SyncedRenderContainer) player).setShouldRenderAPMSDCBeam(false);
        entityComponent.setBool(false);
    }

    public int calculateMaxShootTimeForEnchant(ItemStack stack) {
        return calculateWindUpTimeForEnchant(stack) + calculateShootTimeForEnchant(stack);
    }

    public int calculateShootTimeForEnchant(ItemStack stack) {
        boolean highPower = EnchantmentHelper.getLevel(RinveniumEnchantments.HIGH_POWER_OUTPUT, stack) > 0;
        boolean lowPower = EnchantmentHelper.getLevel(RinveniumEnchantments.LOW_POWER_OUTPUT, stack) > 0;

        if (lowPower) {
            return 60;
        }
        if (highPower) {
            return 40;
        }
        return 50;
    }

    public int calculateWindUpTimeForEnchant(ItemStack stack) {
        boolean highPower = EnchantmentHelper.getLevel(RinveniumEnchantments.HIGH_POWER_OUTPUT, stack) > 0;
        boolean lowPower = EnchantmentHelper.getLevel(RinveniumEnchantments.LOW_POWER_OUTPUT, stack) > 0;

        if (lowPower) {
            return 10;
        }
        if (highPower) {
            return 30;
        }
        return 20;
    }

    public int calculateIonCellDecrementForEnchant(ItemStack stack) {
        boolean highPower = EnchantmentHelper.getLevel(RinveniumEnchantments.HIGH_POWER_OUTPUT, stack) > 0;
        boolean lowPower = EnchantmentHelper.getLevel(RinveniumEnchantments.LOW_POWER_OUTPUT, stack) > 0;
        APMSDCItemComponent itemComponent = RinveniumComponents.APMSDC_ITEM.get(stack);

        if (lowPower) {
            if (itemComponent.getLowPowerCount() >= MAX_LOW_POWER_COUNT) {
                return -1;
            } else {
                return 0;
            }
        }

        if (highPower) {
            return -2;
        }

        return -1;
    }

    public StatusEffect getStatusEffectForEnchant(ItemStack stack) {
        boolean highPower = EnchantmentHelper.getLevel(RinveniumEnchantments.HIGH_POWER_OUTPUT, stack) > 0;
        boolean lowPower = EnchantmentHelper.getLevel(RinveniumEnchantments.LOW_POWER_OUTPUT, stack) > 0;

        if (lowPower) {
            return RinveniumStatusEffects.MELTED;
        }
        if (highPower) {
            return RinveniumStatusEffects.PLASMAFIED;
        }
        return RinveniumStatusEffects.IONIZED;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return true;
    }
    @Override
    public int getItemBarColor(ItemStack stack) {
        if (EnchantmentHelper.getLevel(RinveniumEnchantments.HIGH_POWER_OUTPUT, stack) > 0) {
            return HIGH_POWER.getColor();
        }
        if (EnchantmentHelper.getLevel(RinveniumEnchantments.LOW_POWER_OUTPUT, stack) > 0) {
            return LOW_POWER.getColor();
        }
        if (EnchantmentHelper.getLevel(RinveniumEnchantments.STARSTRUCK, stack) > 0) {
            return STARSTRUCK.getColor();
        }
        return DEFAULT.getColor();
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        APMSDCItemComponent itemComponent = RinveniumComponents.APMSDC_ITEM.get(stack);
        return MathHelper.ceil((double) (itemComponent.getIonCellCount() * 13 / 10));
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        RinveniumUtil.addExpandableTooltip(
                EnchantmentHelper.getLevel(RinveniumEnchantments.STARSTRUCK, stack) > 0
                        ? Text.translatable("item.rinvenium.apmsdc_starstruck.desc").formatted(Formatting.GRAY)
                        : Text.translatable("item.rinvenium.apmsdc.desc").formatted(Formatting.GRAY),
                Text.translatable("item.rinvenium.expand_toolip"),
                tooltip,
                true
        );
        tooltip.add(Text.empty());
        super.appendTooltip(stack, world, tooltip, context);
    }

    public enum BeamType implements StringIdentifiable {
        DEFAULT("default", 0x2CFBD7),
        LOW_POWER("low_power", 0xFB902C),
        HIGH_POWER("high_power", 0xAF2CFB),
        STARSTRUCK("starstruck", 0xFBD92C);

        private final String name;
        private final int color;

        BeamType(String name, int color) {
            this.name = name;
            this.color = color;
        }

        @Override
        public String asString() {
            return name;
        }

        @Override
        public String toString() {
            return name;
        }

        public int getColor() {
            return color;
        }
    }
}
