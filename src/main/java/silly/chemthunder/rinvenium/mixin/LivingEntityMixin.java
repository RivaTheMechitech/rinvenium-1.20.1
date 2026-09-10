package silly.chemthunder.rinvenium.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import silly.chemthunder.rinvenium.Rinvenium;
import silly.chemthunder.rinvenium.cca.entity.DeathSequenceComponent;
import silly.chemthunder.rinvenium.cca.entity.EnvixiaFormComponent;
import silly.chemthunder.rinvenium.cca.entity.SpearParryComponent;
import silly.chemthunder.rinvenium.index.RinveniumDamageSources;
import silly.chemthunder.rinvenium.index.RinveniumEnchantments;
import silly.chemthunder.rinvenium.index.RinveniumItems;
import silly.chemthunder.rinvenium.index.RinveniumSoundEvents;
import silly.chemthunder.rinvenium.index.RinveniumStatusEffects;
import silly.chemthunder.rinvenium.item.EnvixiaArmorItem;
import silly.chemthunder.rinvenium.util.persistent.DeathSequenceState;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Attackable {
    @Shadow public abstract boolean hasStatusEffect(StatusEffect effect);
    @Shadow public abstract ItemStack getEquippedStack(EquipmentSlot slot);
    @Shadow protected int roll;

    @Shadow
    public abstract void setHealth(float health);

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @WrapOperation(
        method = "tickRiptide",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/math/Box;union(Lnet/minecraft/util/math/Box;)Lnet/minecraft/util/math/Box;"
        )
    )
    private Box rinvenium$envixiaLargerHitbox(Box instance, Box box, Operation<Box> original) {
        LivingEntity user = (LivingEntity) (Object) this;
        if (user instanceof PlayerEntity player) {
            EnvixiaFormComponent envixiaFormComponent = EnvixiaFormComponent.get(player);
            if (envixiaFormComponent.getTripleBoolValue1()) {
                return original.call(instance, box).expand(1.5);
            } else if (user.getStackInHand(user.getActiveHand()).isOf(RinveniumItems.ENVINIUM_SPEAR)) {
                return original.call(instance, box).expand(0.5);
            }
        }
        return original.call(instance, box);
    }

    @Inject(
        method = "tickRiptide",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/LivingEntity;attackLivingEntity(Lnet/minecraft/entity/LivingEntity;)V"
        )
    )
    private void electrifySHIPFGHERFTIOPGIPRTHGPIRTHNPIHJRTYPIJIPTY(Box a, Box b, CallbackInfo ci, @Local Entity entity) {
        LivingEntity you = (LivingEntity) (Object) this;
        LivingEntity living = (LivingEntity) entity;
        if (you instanceof PlayerEntity player) {
            ItemStack stack = player.getStackInHand(player.getActiveHand());
            var hasChanneling = EnchantmentHelper.hasChanneling(stack);
            var hasSpear = EnchantmentHelper.getLevel(RinveniumEnchantments.RUSH, stack) > 0;
            if (hasChanneling || hasSpear) {
                if (!Rinvenium.haters.contains(player.getUuid())) {
                    living.addStatusEffect(new StatusEffectInstance(RinveniumStatusEffects.SPARKED, 10));

                    if (player.getWorld() instanceof ServerWorld serverWorld) {
                        LightningEntity lightningEntity = new LightningEntity(EntityType.LIGHTNING_BOLT, player.getWorld());
                        lightningEntity.setCosmetic(true);
                        lightningEntity.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(entity.getBlockPos()));
                        serverWorld.spawnEntity(lightningEntity);
                    }
                } else {
                    player.addStatusEffect(new StatusEffectInstance(RinveniumStatusEffects.SPARKED, 20));

                    if (player.getWorld() instanceof ServerWorld serverWorld) {
                        LightningEntity bolt = new LightningEntity(EntityType.LIGHTNING_BOLT, serverWorld);

                        bolt.updatePosition(player.getX(), player.getY(), player.getZ());

                        serverWorld.spawnEntity(bolt);
                    }
                }
                if (player.getWorld() instanceof ServerWorld serverWorld) {
                    serverWorld.playSound(null, player.getBlockPos(), RinveniumSoundEvents.SPEAR_DASH_IMPACT, SoundCategory.PLAYERS, 1, 1);
                }
            }
        }
    }

    @WrapOperation(
        method = "applyDamage",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/LivingEntity;modifyAppliedDamage(Lnet/minecraft/entity/damage/DamageSource;F)F"
        )
    )
    private float rinvenium$spearParryDamage(LivingEntity entity, DamageSource source, float amount, Operation<Float> original) {
        float base = original.call(entity, source, amount);
        if (source.getAttacker() instanceof PlayerEntity player && player.getStackInHand(Hand.MAIN_HAND).isOf(RinveniumItems.ENVINIUM_SPEAR)) {
            SpearParryComponent spearParryComponent = SpearParryComponent.get(player);
            if (spearParryComponent.getDoubleIntValue2() > 0 && EnchantmentHelper.getLevel(RinveniumEnchantments.RUSH, player.getStackInHand(Hand.MAIN_HAND)) <= 0) {
                spearParryComponent.setDoubleIntValue2(0);
                spearParryComponent.setDoubleIntValue1(SpearParryComponent.MAX_PARRY_WINDOW);
                spearParryComponent.setDoubleBoolValue1(true);
                return base * 1.5f;
            }
        }
        return base;
    }

    @WrapWithCondition(
        method = "damage",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/LivingEntity;takeKnockback(DDD)V"
        )
    )
    private boolean rinvenium$knockbacknt(LivingEntity instance, double strength, double x, double z, @Local(argsOnly = true) DamageSource damageSource) {
        return !damageSource.isOf(RinveniumDamageSources.BOOP) && !damageSource.isOf(RinveniumDamageSources.ELECTRICITY);
    }

    @ModifyArg(
        method = "damage",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/LivingEntity;takeKnockback(DDD)V"
        ),
        index = 0
    )
    private double rinvenium$damageDoesNoKB(double strength, @Local(argsOnly = true) DamageSource damageSource) {
        if (damageSource.isOf(RinveniumDamageSources.BOOP) || damageSource.isOf(RinveniumDamageSources.ELECTRICITY)) {
            return 0.0;
        } else {
            return strength;
        }
    }

    @Inject(method = "tickFallFlying", at = @At(value = "HEAD"), cancellable = true)
    private void rinvenium$envixiaElytraFlight(CallbackInfo ci) {
        boolean bl = this.getFlag(Entity.FALL_FLYING_FLAG_INDEX);
        if (((LivingEntity) ((Object)this)) instanceof PlayerEntity player && bl && !this.isOnGround() && !this.hasVehicle() && !this.hasStatusEffect(StatusEffects.LEVITATION)) {
            EnvixiaFormComponent envixiaFormComponent = EnvixiaFormComponent.get(player);
            ItemStack itemStack = this.getEquippedStack(EquipmentSlot.CHEST);
            if ((itemStack.isOf(RinveniumItems.ENVIXIA_CHESTPLATE) && envixiaFormComponent.getTripleBoolValue1()) || (itemStack.isOf(Items.ELYTRA) && ElytraItem.isUsable(itemStack))) {
                bl = true;
                int i = this.roll + 1;
                if (!this.getWorld().isClient && i % 10 == 0) {
                    this.emitGameEvent(GameEvent.ELYTRA_GLIDE);
                }
            } else {
                bl = false;
            }
        } else {
            bl = false;
        }

        if (!this.getWorld().isClient) {
            this.setFlag(Entity.FALL_FLYING_FLAG_INDEX, bl);
            if (bl && this.getEquippedStack(EquipmentSlot.CHEST).isOf(RinveniumItems.ENVIXIA_CHESTPLATE)) {
                ServerWorld serverWorld = (ServerWorld) this.getWorld();
                Vec3d pos = this.getPos();
                serverWorld.spawnParticles(ParticleTypes.SOUL_FIRE_FLAME, pos.x, pos.y + 0.25, pos.z, 1, 0.01, 0.01, 0.01, 0.001);
            }
            ci.cancel();
            return;
        }
    }

    @Inject(method = "isGlowing", at = @At("HEAD"), cancellable = true)
    private void rinvenium$evnixiaGlowing(CallbackInfoReturnable<Boolean> cir) {
        if (((LivingEntity) ((Object)this)) instanceof PlayerEntity player) {
            EnvixiaFormComponent envixiaFormComponent = EnvixiaFormComponent.get(player);
            cir.setReturnValue(envixiaFormComponent.getTripleBoolValue1());
        }
    }

    @Inject(method = "modifyAppliedDamage", at = @At(value = "TAIL"), cancellable = true)
    private void rinvenium$envixiaFallProt(DamageSource source, float amount, CallbackInfoReturnable<Float> cir) {
        if (((LivingEntity)((Object)this)) instanceof  PlayerEntity player) {
            if (EnvixiaArmorItem.hasFullSuit(player) && source.isIn(DamageTypeTags.IS_FALL)) {
                cir.setReturnValue(amount * 0.25f);
            }
        }
    }

    @WrapWithCondition(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;onDeath(Lnet/minecraft/entity/damage/DamageSource;)V"))
    private boolean rinvenium$envixiaDeath(LivingEntity instance, DamageSource damageSource) {
        /*
        if (instance instanceof PlayerEntity player && !damageSource.isOf(RinveniumDamageSources.ORCHID)) {
            if (player.getServer() != null) {
                DeathSequenceState deathSequenceState = DeathSequenceState.getServerState(player.getServer());
                ServerPlayerEntity storedPlayer = player.getServer().getPlayerManager().getPlayer(deathSequenceState.playerUuid);
                if (deathSequenceState.canSequence) {
                    if (storedPlayer != null && player.getServer().getPlayerManager().getPlayerList().contains(storedPlayer)) {
                        DeathSequenceComponent deathSequenceComponent = DeathSequenceComponent.get(storedPlayer);
                        deathSequenceComponent.setBool(true);
                        player.getServer().getPlayerManager().getPlayerList().forEach(serverPlayerEntity -> {
                            if (serverPlayerEntity.squaredDistanceTo(storedPlayer) <= 128 * 128 && !serverPlayerEntity.equals(storedPlayer)) {
                                serverPlayerEntity.addStatusEffect(new StatusEffectInstance(RinveniumStatusEffects.WATCHED, 20 * 38, 0, false, false, false));
                            }
                        });
                        this.setHealth(0.1f);
                        return false;
                    }
                }
            }
        }
        */
        return true;
    }
}