package silly.chemthunder.rinvenium.item;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import silly.chemthunder.rinvenium.cca.RinveniumComponents;
import silly.chemthunder.rinvenium.cca.entity.APMDSCComponent;
import silly.chemthunder.rinvenium.cca.item.APMDSCItemComponent;
import silly.chemthunder.rinvenium.index.RinveniumEnchantments;
import silly.chemthunder.rinvenium.index.RinveniumItems;
import silly.chemthunder.rinvenium.index.RinveniumPackets;
import silly.chemthunder.rinvenium.render.APMDSCBeamRender;
import silly.chemthunder.rinvenium.render.manager.client.APMDSCBeamManager;
import silly.chemthunder.rinvenium.util.RinveniumUtil;
import silly.chemthunder.rinvenium.util.inject.RenderContainer;

import java.lang.reflect.Type;
import java.util.UUID;

import static silly.chemthunder.rinvenium.cca.item.APMDSCItemComponent.*;

public class APMDSCItem extends Item {
    public static final int RELOAD_ICD = 10;
    public static final int SHOOT_COOLDOWN = 60;

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
        APMDSCComponent entityComponent = APMDSCComponent.get(player);
        APMDSCItemComponent itemComponent = RinveniumComponents.APMDSC_ITEM.get(stack);

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
            EntityHitResult entityHitResult = RinveniumUtil.raycastWithDivergenceBox(player, startPos, player.getRotationVecClient(), 6.0, 0.375f, false);
            if (entityHitResult != null) {
                Entity entity = entityHitResult.getEntity();
                if (entity instanceof LivingEntity target && !target.hasStatusEffect(StatusEffects.POISON)) {
                    target.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 600, 2));
                }
            }
        }

        // Wind up particles
        if (entityComponent.getInt() < calculateWindUpTimeForEnchant(stack)) {
            world.addParticle(ParticleTypes.PORTAL, player.getX(), player.getY(), player.getZ(), 0.0D, 0.0D, 0.0D);
        }

        // Shoot beam
        if (entityComponent.getInt() == calculateWindUpTimeForEnchant(stack)) {
            if (player instanceof ServerPlayerEntity serverPlayer) {
                UUID uuid = UUID.randomUUID();
                itemComponent.setBeamRenderUuid(uuid);
                PacketByteBuf buf = PacketByteBufs.create();
                buf.writeUuid(uuid);
                buf.writeDouble(startPos.getX());
                buf.writeDouble(startPos.getY());
                buf.writeDouble(startPos.getZ());
                buf.writeDouble(endPos.getX());
                buf.writeDouble(endPos.getY());
                buf.writeDouble(endPos.getZ());
                buf.writeInt(calculateShootTimeForEnchant(stack) + APMDSCBeamRender.FADE_IN_DURATION + APMDSCBeamRender.FADE_OUT_DURATION);
                ServerPlayNetworking.send(serverPlayer, RinveniumPackets.ADD_APMDSC_BEAM, buf);
            }
        }
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        PlayerEntity player = (PlayerEntity) user;
        APMDSCComponent entityComponent = APMDSCComponent.get(player);
        APMDSCItemComponent itemComponent = RinveniumComponents.APMDSC_ITEM.get(stack);
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
            if (player instanceof ServerPlayerEntity serverPlayer) {
                UUID uuid = itemComponent.getBeamRenderUuid();
                if (uuid != null) {
                    PacketByteBuf buf = PacketByteBufs.create();
                    buf.writeUuid(uuid);
                    ServerPlayNetworking.send(serverPlayer, RinveniumPackets.STOP_APMDSC_BEAM, buf);
                }
            }
        }
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
        APMDSCItemComponent itemComponent = RinveniumComponents.APMDSC_ITEM.get(stack);

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
        return 0x00FFFF;
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        APMDSCItemComponent itemComponent = RinveniumComponents.APMDSC_ITEM.get(stack);
        return MathHelper.ceil((double) (itemComponent.getIonCellCount() * 13 / 10));
    }
}
