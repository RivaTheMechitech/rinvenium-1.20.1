package silly.chemthunder.rinvenium.item;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import silly.chemthunder.rinvenium.cca.entity.EnvixiaFormComponent;
import silly.chemthunder.rinvenium.cca.entity.HailOfTheGodComponent;
import silly.chemthunder.rinvenium.entity.GunshotEntity;
import silly.chemthunder.rinvenium.index.RinveniumDamageSources;
import silly.chemthunder.rinvenium.index.RinveniumItems;
import silly.chemthunder.rinvenium.index.RinveniumPackets;
import silly.chemthunder.rinvenium.index.RinveniumParticles;
import silly.chemthunder.rinvenium.index.RinveniumSoundEvents;
import silly.chemthunder.rinvenium.index.RinveniumStatusEffects;
import silly.chemthunder.rinvenium.util.RinveniumUtil;

import java.util.List;

public class HotGItem extends Item {
    public static final int BULLET_PER_SEC = 10;
    private int shootCooldown = 0;

    public HotGItem(Settings settings) {
        super(settings);
    }

    @Override
    public Text getName(ItemStack stack) {
        return super.getName(stack).copy().styled(style -> style.withColor(0xf58442));
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.NONE;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 100;
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (!user.getItemCooldownManager().isCoolingDown(RinveniumItems.HAIL_OF_THE_GODS)) {
            user.setCurrentHand(hand);
            return TypedActionResult.consume(stack);
        }
        return TypedActionResult.fail(stack);
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (user instanceof PlayerEntity player) {
            EnvixiaFormComponent envixiaFormComponent = EnvixiaFormComponent.get(player);
            HailOfTheGodComponent hailOfTheGodComponent = HailOfTheGodComponent.get(player);

            if (player.getItemCooldownManager().isCoolingDown(RinveniumItems.HAIL_OF_THE_GODS)) {
                player.stopUsingItem();
            }
            //shootAsProjectile(world, user, player, hailOfTheGodComponent);
            if (hailOfTheGodComponent.getDoubleIntValue2() > 0) {
                player.sendMessage(Text.literal("! Hail of the Gods is ").append(Text.literal("overheating !").formatted(Formatting.BOLD)).formatted(Formatting.RED), true);
                if (hailOfTheGodComponent.getDoubleIntValue2() >= 1 && hailOfTheGodComponent.getDoubleIntValue2() <= 5) {
                    world.playSound(null, player.getBlockPos(), RinveniumSoundEvents.HAIL_OF_THE_GODS_OVERHEAT, SoundCategory.PLAYERS,0.9f, 1.0f);
                }
                if (player.getItemUseTime() % 10 == 0) {
                    shootAsRaycastBuckshot(world, user, player, hailOfTheGodComponent);
                    world.playSound(null, player.getBlockPos(), RinveniumSoundEvents.HAIL_OF_THE_GODS_SHOOT, SoundCategory.PLAYERS,0.5f, 0.9f);
                }
            } else {
                shootAsRaycast(world, user, player, hailOfTheGodComponent);
                if (envixiaFormComponent.getTripleBoolValue1()) shootAsRaycastAimAssist(world, user, player, hailOfTheGodComponent);
                world.playSound(null, player.getBlockPos(), RinveniumSoundEvents.HAIL_OF_THE_GODS_SHOOT, SoundCategory.PLAYERS, 0.5f, 0.9f);
            }

            if (!world.isClient) {
                if (hailOfTheGodComponent.getDoubleIntValue1() < HailOfTheGodComponent.MAX_USE_TIME && hailOfTheGodComponent.getDoubleIntValue2() <= 0) {
                    hailOfTheGodComponent.addToDoubleIntValue1(3);
                } else {
                    hailOfTheGodComponent.addToDoubleIntValue2(5);
                }
            }
        }

        if (user.isUsingItem() && user.getActiveItem().isOf(RinveniumItems.HAIL_OF_THE_GODS) && user.getItemUseTime() >= this.getMaxUseTime(stack)) {
            user.stopUsingItem();
            if (user instanceof PlayerEntity player) {
                player.getItemCooldownManager().set(RinveniumItems.HAIL_OF_THE_GODS, 100);
            }
        }
    }

    private void shootAsRaycast(World world, LivingEntity user, PlayerEntity player, HailOfTheGodComponent hailOfTheGodComponent) {
        Vec3d clientRot = null;
        if (world.isClient) {
            clientRot = user.getRotationVecClient();
        }

        if (!world.isClient && !player.getItemCooldownManager().isCoolingDown(RinveniumItems.HAIL_OF_THE_GODS)) {
            final float tickDelta = 0.0f;
            boolean shouldFlash = false;
            Entity entity = null;
            EnvixiaFormComponent envixiaFormComponent = EnvixiaFormComponent.get(player);
            for (int i = 0; i < (envixiaFormComponent.getTripleBoolValue1() ? 4 : 3); i++) {
                Vec3d start = player.getCameraPosVec(tickDelta);
                Vec3d rot = clientRot != null ? clientRot : player.getRotationVec(tickDelta);
                float divergence = (float) (RinveniumUtil.calculateDivergenceDropOff(hailOfTheGodComponent.getDoubleIntValue1()) * 2.0f + 0.1f);
                rot = rot.add(
                        player.getWorld().random.nextTriangular(0.0, 0.0172275 * divergence),
                        player.getWorld().random.nextTriangular(0.0, 0.0172275 * divergence),
                        player.getWorld().random.nextTriangular(0.0, 0.0172275 * divergence)
                );
                HitResult hitResult = RinveniumUtil.raycastWithDivergence(player, start, rot, 50.0, false);
                EntityHitResult entityHitResult = RinveniumUtil.raycastWithDivergenceBox(player, start, rot, 50.0, 0.25, false);
                if (entityHitResult != null) {
                    Entity target = entityHitResult.getEntity();
                    if (target != null && target.damage(RinveniumDamageSources.boop(target), 0.1f)) {
                        float random = world.random.nextFloat();
                        if (random <= 0.05f) {
                            if (target instanceof LivingEntity livingEntity) {
                                livingEntity.addStatusEffect(new StatusEffectInstance(RinveniumStatusEffects.SPARKED_WITH_CD, 40, 0));
                            }
                        }
                        shouldFlash = true;
                        entity = target;
                    }
                }

                RinveniumUtil.spawnRaycastParticles((ServerWorld) world, start, rot, hitResult, 50.0, 0.5, 1.2, RinveniumParticles.HAIL_OF_THE_GODS_TRAIL);
                RinveniumUtil.spawnRaycastParticles((ServerWorld) world, start, rot, hitResult, 50.0, 1.0, 1.1, RinveniumParticles.HAIL_OF_THE_GODS_SMOKE);
            }

            if (shouldFlash && entity != null) {
                ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player;
                if (serverPlayerEntity != null) {
                    PacketByteBuf buf = PacketByteBufs.create();
                    buf.writeDouble(entity.getX());
                    buf.writeDouble(entity.getY());
                    buf.writeDouble(entity.getZ());
                    buf.writeInt(0Xfbb630);
                    ServerPlayNetworking.send(serverPlayerEntity, RinveniumPackets.FLASH_PARTICLE, buf);
                }
            }
        }
    }

    private void shootAsRaycastBuckshot(World world, LivingEntity user, PlayerEntity player, HailOfTheGodComponent hailOfTheGodComponent) {
        Vec3d clientRot = null;
        if (world.isClient) {
            clientRot = user.getRotationVecClient();
        }

        if (!world.isClient && !player.getItemCooldownManager().isCoolingDown(RinveniumItems.HAIL_OF_THE_GODS)) {
            final float tickDelta = 0.0f;
            boolean shouldFlash = false;
            Entity entity = null;
            for (int i = 0; i < 40; i++) {
                Vec3d start = player.getCameraPosVec(tickDelta);
                Vec3d rot = clientRot != null ? clientRot : player.getRotationVec(tickDelta);
                float divergence = 10.1f;

                rot = rot.add(
                        player.getWorld().random.nextTriangular(0.0, 0.0172275 * divergence),
                        player.getWorld().random.nextTriangular(0.0, 0.0172275 * divergence),
                        player.getWorld().random.nextTriangular(0.0, 0.0172275 * divergence)
                );

                HitResult hitResult = RinveniumUtil.raycastWithDivergence(player, start, rot, 50.0, false);
                EntityHitResult entityHitResult = RinveniumUtil.raycastWithDivergenceBox(player, start, rot, 50.0, 0.25, false);

                if (entityHitResult != null) {
                    Entity target = entityHitResult.getEntity();
                    if (target != null && target.damage(RinveniumDamageSources.boop(target), 0.1f)) {
                        shouldFlash = true;
                        entity = target;
                    }
                }

                RinveniumUtil.spawnRaycastParticles((ServerWorld) world, start, rot, hitResult, 50.0, 0.5, 1.2, RinveniumParticles.HAIL_OF_THE_GODS_TRAIL);
                RinveniumUtil.spawnRaycastParticles((ServerWorld) world, start, rot, hitResult, 50.0, 1.0, 1.1, RinveniumParticles.HAIL_OF_THE_GODS_SMOKE);
            }

            if (shouldFlash && entity != null) {
                ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player;
                if (serverPlayerEntity != null) {
                    PacketByteBuf buf = PacketByteBufs.create();
                    buf.writeDouble(entity.getX());
                    buf.writeDouble(entity.getY());
                    buf.writeDouble(entity.getZ());
                    buf.writeInt(0Xfbb630);
                    ServerPlayNetworking.send(serverPlayerEntity, RinveniumPackets.FLASH_PARTICLE, buf);
                }
            }
        }
    }

    private void shootAsRaycastAimAssist(World world, LivingEntity user, PlayerEntity player, HailOfTheGodComponent hailOfTheGodComponent) {
        Vec3d clientRot = null;
        if (world.isClient) {
            clientRot = user.getRotationVecClient();
        }

        if (!world.isClient && !player.getItemCooldownManager().isCoolingDown(RinveniumItems.HAIL_OF_THE_GODS)) {
            final float tickDelta = 0.0f;
            boolean shouldFlash = false;
            Entity entity = null;

            for (int i = 0; i < 5; i++) {
                Vec3d start = player.getCameraPosVec(tickDelta);
                Vec3d rot = clientRot != null ? clientRot : player.getRotationVec(tickDelta);
                float divergence = 5.0f;

                rot = rot.add(
                        player.getWorld().random.nextTriangular(0.0, 0.0172275 * divergence),
                        player.getWorld().random.nextTriangular(0.0, 0.0172275 * divergence),
                        player.getWorld().random.nextTriangular(0.0, 0.0172275 * divergence)
                );

                HitResult hitResult = RinveniumUtil.raycastWithDivergence(player, start, rot, 50.0, false);
                EntityHitResult entityHitResult = RinveniumUtil.raycastWithDivergenceBox(player, start, rot, 50.0, 0.25, false);

                if (entityHitResult != null) {
                    Entity target = entityHitResult.getEntity();
                    if (target != null && target.damage(RinveniumDamageSources.boop(target), 0.1f)) {
                        shouldFlash = true;
                        entity = target;
                    }
                }

                RinveniumUtil.spawnRaycastParticles((ServerWorld) world, start, rot, hitResult, 50.0, 0.5, 1.2, RinveniumParticles.HAIL_OF_THE_GODS_TRAIL);
                RinveniumUtil.spawnRaycastParticles((ServerWorld) world, start, rot, hitResult, 50.0, 1.0, 1.1, RinveniumParticles.HAIL_OF_THE_GODS_SMOKE);
            }

            if (shouldFlash && entity != null) {
                ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player;
                if (serverPlayerEntity != null) {
                    PacketByteBuf buf = PacketByteBufs.create();
                    buf.writeDouble(entity.getX());
                    buf.writeDouble(entity.getY());
                    buf.writeDouble(entity.getZ());
                    buf.writeInt(0Xfbb630);
                    ServerPlayNetworking.send(serverPlayerEntity, RinveniumPackets.FLASH_PARTICLE, buf);
                }
            }
        }
    }

    private void shootAsProjectile(World world, LivingEntity user, PlayerEntity player, HailOfTheGodComponent hailOfTheGodComponent) {
        Vec3d clientRot = null;
        if (world.isClient) {
            clientRot = user.getRotationVecClient();
        }

        if (!world.isClient && !player.getItemCooldownManager().isCoolingDown(RinveniumItems.HAIL_OF_THE_GODS)) {
            for (int i = 0; i < 4; i++) {
                GunshotEntity bullet = new GunshotEntity(world, user);

                if (bullet.getOwner() == null) {
                    bullet.setOwner(user);
                } else if (!bullet.getOwner().equals(user)) {
                    bullet.setOwner(user);
                }

                Vec3d spawnPos = user.getPos();
                bullet.updatePosition(spawnPos.getX(), spawnPos.getY() + 0.5f, spawnPos.getZ());
                float pitch;
                float yaw;

                if (clientRot != null) {
                    pitch = RinveniumUtil.pitchFromVecDeg(clientRot);
                    yaw = RinveniumUtil.yawFromVecDeg(clientRot);
                } else {
                    pitch = user.getPitch();
                    yaw = user.getYaw();
                }

                bullet.updateRotation();
                bullet.setVelocity(user, pitch, yaw, 0.0f, 3.2f * 3.0f, (float) RinveniumUtil.calculateDivergenceDropOff(hailOfTheGodComponent.getDoubleIntValue1()) * 2.0f + 0.1f);

                if (hailOfTheGodComponent.getDoubleIntValue2() > 0) {
                    if (hailOfTheGodComponent.getDoubleIntValue2() % 2 == 0) {
                        world.spawnEntity(bullet);
                        break;
                    }
                } else {
                    world.spawnEntity(bullet);
                }
            }
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (entity instanceof PlayerEntity player) {
            HailOfTheGodComponent hailOfTheGodComponent = HailOfTheGodComponent.get(player);
            //player.sendMessage(Text.literal("use time: " + hailOfTheGodComponent.getDoubleIntValue1() + "   overheat: " + hailOfTheGodComponent.getDoubleIntValue2()), true);
        }
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        return 0x9cfdff;
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        return 0;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        RinveniumUtil.addExpandableTooltip(Text.translatable("item.rinvenium.hail_of_the_gods.desc").formatted(Formatting.GRAY), Text.translatable("item.rinvenium.expand_toolip"), tooltip, true);
        super.appendTooltip(stack, world, tooltip, context);
    }
}