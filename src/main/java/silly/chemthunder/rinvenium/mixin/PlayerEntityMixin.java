package silly.chemthunder.rinvenium.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import silly.chemthunder.rinvenium.cca.entity.EnvixiaFormComponent;
import silly.chemthunder.rinvenium.cca.entity.SpearParryComponent;
import silly.chemthunder.rinvenium.cca.entity.riva.SpearHealComponent;
import silly.chemthunder.rinvenium.datagen.RinveniumItemTagProvider;
import silly.chemthunder.rinvenium.index.*;
import silly.chemthunder.rinvenium.item.EnviniumSpearItem;
import silly.chemthunder.rinvenium.util.inject.HungerDecrement;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    @Shadow public abstract ItemStack getEquippedStack(EquipmentSlot slot);
    @Shadow public abstract void startFallFlying();
    @Shadow public abstract HungerManager getHungerManager();

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    /*@Inject(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/player/PlayerEntity;DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V"))
    private void customHitSound(Entity target, CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) ((Object)this);
        if (player.getStackInHand(Hand.MAIN_HAND).isOf(RinveniumItems.ENVINIUM_SPEAR)) {
            SpearParryComponent spearParryComponent = SpearParryComponent.get(player);
            if (spearParryComponent.getDoubleIntValue2() > 0) {
                this.getWorld().playSound(target.getX(), target.getY(), target.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, SoundCategory.PLAYERS, 2.0f, 1.0f, true);
            }
            this.getWorld().playSound(target.getX(), target.getY(), target.getZ(), SoundEvents.BLOCK_COPPER_BREAK, SoundCategory.PLAYERS, 1.0f, 1.0f, true);
        }
    }*/
    /*@WrapOperation(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/player/PlayerEntity;DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V"))
    private void rinvenium$customHitSounds(World world, PlayerEntity except, double x, double y, double z, SoundEvent sound, SoundCategory category, float volume, float pitch, Operation<Void> original, @Local(argsOnly = true) Entity target) {
        PlayerEntity player = (PlayerEntity) ((Object)this);
        if (player.getStackInHand(Hand.MAIN_HAND).isOf(RinveniumItems.ENVINIUM_SPEAR)) {
            SpearParryComponent spearParryComponent = SpearParryComponent.get(player);
            if (spearParryComponent.getDoubleIntValue2() > 0) {
                if (world instanceof ServerWorld serverWorld) {
                    serverWorld.playSound(target.getX(), target.getY(), target.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, SoundCategory.PLAYERS, 2.0f, 1.0f, true);
                }
            }
            if (world instanceof ServerWorld serverWorld) {
                serverWorld.playSound(target.getX(), target.getY(), target.getZ(), RinveniumSoundEvents.SPEAR_SLASH, SoundCategory.PLAYERS, 0.8f, 1.0f, true);
            }
        } else {
            original.call(world, except, x, y, z, sound, category, volume, pitch);
        }
    }*/

    @WrapOperation(
        method = "attack",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/sound/SoundEvents;ENTITY_PLAYER_ATTACK_SWEEP:Lnet/minecraft/sound/SoundEvent;"
        )
    )
    private SoundEvent rinvenium$maybeActuallyPlaysCustomSoundsSweep(Operation<SoundEvent> original, @Local (argsOnly = true) Entity target) {
        PlayerEntity player = (PlayerEntity) ((Object)this);
        if (player.getStackInHand(Hand.MAIN_HAND).isOf(RinveniumItems.ENVINIUM_SPEAR)) {
            SpearParryComponent spearParryComponent = SpearParryComponent.get(player);
            if (spearParryComponent.getDoubleIntValue2() > 0) {
                if (this.getEntityWorld() instanceof ServerWorld serverWorld) {
                    serverWorld.playSound(target.getX(), target.getY(), target.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, SoundCategory.PLAYERS, 2.0f, 1.0f, true);
                }
            }
            return RinveniumSoundEvents.SPEAR_SLASH;
        } else {
            return original.call();
        }
    }

    @WrapOperation(
        method = "attack",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/sound/SoundEvents;ENTITY_PLAYER_ATTACK_KNOCKBACK:Lnet/minecraft/sound/SoundEvent;"
        )
    )
    private SoundEvent rinvenium$maybeActuallyPlaysCustomSoundsKB(Operation<SoundEvent> original, @Local (argsOnly = true) Entity target) {
        PlayerEntity player = (PlayerEntity) ((Object)this);
        if (player.getStackInHand(Hand.MAIN_HAND).isOf(RinveniumItems.ENVINIUM_SPEAR)) {
            SpearParryComponent spearParryComponent = SpearParryComponent.get(player);
            if (spearParryComponent.getDoubleIntValue2() > 0) {
                if (this.getEntityWorld() instanceof ServerWorld serverWorld) {
                    serverWorld.playSound(target.getX(), target.getY(), target.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, SoundCategory.PLAYERS, 2.0f, 1.0f, true);
                }
            }
            return RinveniumSoundEvents.SPEAR_SLASH;
        } else {
            return original.call();
        }
    }

    @WrapOperation(
        method = "attack",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/sound/SoundEvents;ENTITY_PLAYER_ATTACK_CRIT:Lnet/minecraft/sound/SoundEvent;"
        )
    )
    private SoundEvent rinvenium$maybeActuallyPlaysCustomSoundsCrit(Operation<SoundEvent> original, @Local (argsOnly = true) Entity target) {
        PlayerEntity player = (PlayerEntity) ((Object)this);
        if (player.getStackInHand(Hand.MAIN_HAND).isOf(RinveniumItems.ENVINIUM_SPEAR)) {
            if (this.getEntityWorld() instanceof ServerWorld serverWorld) {
                serverWorld.playSound(target.getX(), target.getY(), target.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, SoundCategory.PLAYERS, 2.0f, 1.0f, true);
            }
            return RinveniumSoundEvents.SPEAR_SLASH;
        } else {
            return original.call();
        }
    }

    @WrapOperation(
        method = "attack",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/sound/SoundEvents;ENTITY_PLAYER_ATTACK_STRONG:Lnet/minecraft/sound/SoundEvent;"
        )
    )
    private SoundEvent rinvenium$maybeActuallyPlaysCustomSoundsStrong(Operation<SoundEvent> original, @Local (argsOnly = true) Entity target) {
        PlayerEntity player = (PlayerEntity) ((Object)this);
        if (player.getStackInHand(Hand.MAIN_HAND).isOf(RinveniumItems.ENVINIUM_SPEAR)) {
            SpearParryComponent spearParryComponent = SpearParryComponent.get(player);
            if (spearParryComponent.getDoubleIntValue2() > 0) {
                if (this.getEntityWorld() instanceof ServerWorld serverWorld) {
                    serverWorld.playSound(target.getX(), target.getY(), target.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, SoundCategory.PLAYERS, 2.0f, 1.0f, true);
                }
            }
            return RinveniumSoundEvents.SPEAR_SLASH;
        } else {
            return original.call();
        }
    }

    @WrapOperation(
        method = "attack",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/sound/SoundEvents;ENTITY_PLAYER_ATTACK_WEAK:Lnet/minecraft/sound/SoundEvent;"
        )
    )
    private SoundEvent rinvenium$maybeActuallyPlaysCustomSoundsWeak(Operation<SoundEvent> original, @Local (argsOnly = true) Entity target) {
        PlayerEntity player = (PlayerEntity) ((Object)this);
        if (player.getStackInHand(Hand.MAIN_HAND).isOf(RinveniumItems.ENVINIUM_SPEAR)) {
            SpearParryComponent spearParryComponent = SpearParryComponent.get(player);
            if (spearParryComponent.getDoubleIntValue2() > 0) {
                if (this.getEntityWorld() instanceof ServerWorld serverWorld) {
                    serverWorld.playSound(target.getX(), target.getY(), target.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, SoundCategory.PLAYERS, 2.0f, 1.0f, true);
                }
            }
            return RinveniumSoundEvents.SPEAR_SLASH;
        } else {
            return original.call();
        }
    }

    @WrapOperation(
        method = "applyDamage",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/player/PlayerEntity;modifyAppliedDamage(Lnet/minecraft/entity/damage/DamageSource;F)F"
        )
    )
    private float rinvenium$handleParry(PlayerEntity player, DamageSource source, float amount, Operation<Float> original) {
        float base = original.call(player, source, amount);
        if (player.getStackInHand(Hand.MAIN_HAND).isOf(RinveniumItems.ENVINIUM_SPEAR)) {
            SpearParryComponent spearParryComponent = SpearParryComponent.get(player);
            if (!source.isIn(DamageTypeTags.BYPASSES_EFFECTS)) {
                if (spearParryComponent.getDoubleBoolValue2() && spearParryComponent.getDoubleBoolValue1()) {
                    Vec3d damagePos = source.getPosition();
                    Vec3d rotVec;
                    Vec3d difference;
                    double angle;
                    ServerWorld serverWorld;
                    World var13;
                    if (damagePos != null) {
                        rotVec = this.getRotationVector();
                        difference = damagePos.relativize(this.getEyePos()).normalize();
                        angle = difference.dotProduct(rotVec);
                        if (angle < -1.0 || angle > 1.0) {
                            return base;
                        }
                        if (angle < -0.35) {
                            EnviniumSpearItem.parried = true;
                            var13 = this.getEntityWorld();
                            if (var13 instanceof ServerWorld) {
                                serverWorld = (ServerWorld) var13;
                                serverWorld.playSoundFromEntity(null, this, RinveniumSoundEvents.SPEAR_PARRY, SoundCategory.PLAYERS, 1.0f, 1.0f + this.getEntityWorld().random.nextFloat() * 0.2f);
                                serverWorld.playSoundFromEntity(null, this, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1.0f, 1.0f + this.getEntityWorld().random.nextFloat() * 0.2f);
                            }
                            PacketByteBuf buf = PacketByteBufs.create();
                            buf.writeInt(8);
                            buf.writeInt(0xFFFFFF);
                            buf.writeInt(4);
                            buf.writeFloat(0.5f);
                            if (player instanceof ServerPlayerEntity serverPlayerEntity) {
                                ServerPlayNetworking.send(serverPlayerEntity, RinveniumPackets.ADD_SCREEN_FLASH, buf);
                                serverPlayerEntity.heal(2.0f);
                            }
                            if (source.getAttacker() != null && source.getAttacker() instanceof ServerPlayerEntity serverPlayerEntity) {
                                ServerPlayNetworking.send(serverPlayerEntity, RinveniumPackets.ADD_SCREEN_FLASH, buf);
                                serverPlayerEntity.sendMessage(Text.literal("Your attack was parried").formatted(Formatting.RED), true);
                            }
                            spearParryComponent.setDoubleIntValue2(SpearParryComponent.MAX_DAMAGE_WINDOW);
                            spearParryComponent.setDoubleBoolValue1(false);
                            if (source.getAttacker() instanceof PlayerEntity playerEntity) {
                                Vec3d launchVec = rotVec;
                                launchVec = launchVec.add(new Vec3d(0.0, 0.7, 0.0));
                                launchVec = launchVec.normalize();
                                playerEntity.setVelocity(launchVec.multiply(1.5));
                                playerEntity.velocityDirty = true;
                            } else if (source.getAttacker() instanceof LivingEntity livingEntity) {
                                Vec3d launchVec = rotVec;
                                launchVec = launchVec.add(new Vec3d(0.0, 0.7, 0.0));
                                launchVec = launchVec.normalize();
                                livingEntity.setVelocity(launchVec.multiply(1.5));
                                livingEntity.velocityDirty = true;
                            }
                            return 0.0f;
                        }
                    }
                }
            }
        }

        if (source.getAttacker() instanceof PlayerEntity attacker && attacker.getStackInHand(Hand.MAIN_HAND).isOf(RinveniumItems.ENVINIUM_SPEAR)) {
            SpearParryComponent attackerSpearParryComponent = SpearParryComponent.get(attacker);
            if (attackerSpearParryComponent.getDoubleIntValue2() > 0 && EnchantmentHelper.getLevel(RinveniumEnchantments.RUSH, attacker.getStackInHand(Hand.MAIN_HAND)) <= 0) {
                attackerSpearParryComponent.setDoubleIntValue2(0);
                attackerSpearParryComponent.setDoubleBoolValue1(true);
                return base * 1.5f;
            }
        }
        return base;
    }

    @Override
    public void onPlayerCollision(PlayerEntity player) {
        if (this.isWet()
                && player.isWet()
                && !player.getUuid().toString().equals("4a58221e-97d0-40cd-9425-01e0ff15ecea")
                && this.hasStatusEffect(RinveniumStatusEffects.SPARKED)
                && !player.hasStatusEffect(RinveniumStatusEffects.SPARKED)
        ) {
            player.addStatusEffect(new StatusEffectInstance(RinveniumStatusEffects.SPARKED, 5, 0));
        }
        super.onPlayerCollision(player);
    }

    @Inject(
        method = "checkFallFlying",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/player/PlayerEntity;getEquippedStack(Lnet/minecraft/entity/EquipmentSlot;)Lnet/minecraft/item/ItemStack;"
        ),
        cancellable = true
    )
    private void rinvenium$envixiaFlight(CallbackInfoReturnable<Boolean> cir) {
        ItemStack itemStack = this.getEquippedStack(EquipmentSlot.CHEST);
        EnvixiaFormComponent envixiaFormComponent = EnvixiaFormComponent.get((PlayerEntity) ((Object) this));
        if ((itemStack.isOf(RinveniumItems.ENVIXIA_CHESTPLATE) && envixiaFormComponent.getTripleBoolValue1()) || (itemStack.isOf(Items.ELYTRA) && ElytraItem.isUsable(itemStack))) {
            this.startFallFlying();
            cir.setReturnValue(true);
        }
    }

    @WrapOperation(
        method = "eatFood",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/player/HungerManager;eat(Lnet/minecraft/item/Item;Lnet/minecraft/item/ItemStack;)V"
        )
    )
    private void rinvenium$decreaseHungerFromNormalFoods(HungerManager instance, Item item, ItemStack stack, Operation<Void> original) {
        EnvixiaFormComponent envixiaFormComponent = EnvixiaFormComponent.get((PlayerEntity) ((Object)this));
        if (envixiaFormComponent.getTripleBoolValue1() && !stack.isIn(RinveniumItemTagProvider.ENVIXIA_MUNCHIES)) {
            ((HungerDecrement) this.getHungerManager()).rinvenium$eatSubtract(stack.getItem(), stack);
        } else {
            original.call(instance, item, stack);
        }
    }

    @Inject(
        method = "attack",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/enchantment/EnchantmentHelper;getKnockback(Lnet/minecraft/entity/LivingEntity;)I"
        )
    )
    private void rinvenium$spearHeal(Entity target, CallbackInfo ci, @Local(ordinal = 0) boolean bl) {
        if (bl) {
            SpearHealComponent spearHealComponent = SpearHealComponent.get((PlayerEntity) ((Object) this));
            //spearHealComponent.incrementInt();
        }
    }

    /*@Inject(method = "attackLivingEntity", at = @At("HEAD"))
    private void rinvenium$riptideAttack(LivingEntity target, CallbackInfo ci) {
    }*/

    @Inject(method = "damage", at = @At(value = "RETURN", ordinal = 3), cancellable = true)
    private void rinvenium$damageTickOrchidSource(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (source.isOf(RinveniumDamageSources.ORCHID) && amount == 0) {
            cir.setReturnValue(super.damage(source, amount));
        }
    }

    @Inject(method = "onDeath", at = @At("HEAD"), cancellable = true)
    private void rinvenium$startDeathSequence(DamageSource source, CallbackInfo ci) {
        /*
        if (this.getServer() != null && !source.isOf(RinveniumDamageSources.ORCHID)) {
            DeathSequenceState deathSequenceState = DeathSequenceState.getServerState(this.getServer());
            ServerPlayerEntity storedPlayer = this.getServer().getPlayerManager().getPlayer(deathSequenceState.playerUuid);
            if (deathSequenceState.canSequence) {
                if (storedPlayer != null && this.getServer().getPlayerManager().getPlayerList().contains(storedPlayer)) {
                    DeathSequenceComponent deathSequenceComponent = DeathSequenceComponent.get(storedPlayer);
                    deathSequenceComponent.setBool(true);
                    this.getServer().getPlayerManager().getPlayerList().forEach(serverPlayerEntity -> {
                        if (serverPlayerEntity.squaredDistanceTo(storedPlayer) <= 128 * 128 && !serverPlayerEntity.equals(storedPlayer)) {
                            serverPlayerEntity.addStatusEffect(new StatusEffectInstance(RinveniumStatusEffects.WATCHED, 20 * 38, 0, false, false, false));
                        }
                    });
                    ci.cancel();
                    return;
                }
            }
        }
        */
    }
}