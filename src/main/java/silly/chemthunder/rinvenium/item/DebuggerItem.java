package silly.chemthunder.rinvenium.item;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import silly.chemthunder.rinvenium.cca.entity.DeathSequenceComponent;
import silly.chemthunder.rinvenium.index.RinveniumPackets;
import silly.chemthunder.rinvenium.index.RinveniumStatusEffects;
import silly.chemthunder.rinvenium.render.SlashRender;
import silly.chemthunder.rinvenium.render.VertexColorSet;
import silly.chemthunder.rinvenium.render.manager.server.CustomFogManager;
import silly.chemthunder.rinvenium.util.RinveniumUtil;
import silly.chemthunder.rinvenium.util.persistent.DeathSequenceState;

import java.util.List;

public class DebuggerItem extends Item {
    public DebuggerItem(Settings settings) {
        super(settings);
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        List<LivingEntity> livingEntities = world.getEntitiesByClass(LivingEntity.class, player.getBoundingBox().expand(10), livingEntity -> !livingEntity.equals(player));
        LivingEntity livingEntity = world.getClosestEntity(livingEntities, TargetPredicate.createAttackable().setPredicate(LivingEntity::isAlive), player, player.getX(), player.getY(), player.getZ());
        if (!player.isSneaking()) {
            if (world.isClient) { // Client Side Standing
                MinecraftClient client = MinecraftClient.getInstance();

            } else { // Server Side Standing
                /** Spawns Raycast particle beam */
                /*
                if (world instanceof ServerWorld serverWorld) {
                    HitResult hitResult = player.raycast(50.0, 0.0f, false);
                    RinveniumUtil.spawnRaycastSmokeParticles(serverWorld, player.getCameraPosVec(0.0f), player.getRotationVector(), hitResult, 256.0, 0.5, 1.1, 40);
                    RinveniumUtil.spawnRaycastRailgunParticles(serverWorld, player.getCameraPosVec(0.0f), player.getRotationVector(), hitResult, 256.0, 0.5, 2.3, 30);
                }
                */

                /** A bunch of random rendering test code */
                /*
                Vec3d origin = player.getPos().add(0, (player.getBoundingBox().maxY - player.getBoundingBox().minY) / 2, 0);

                if (livingEntity != null) {
                    origin = livingEntity.getPos().add(0, (livingEntity.getBoundingBox().maxY - livingEntity.getBoundingBox().minY) / 2, 0);
                }

                float randomX = world.random.nextFloat();
                randomX = randomX < 0.5 ? -randomX : randomX - 0.5f;
                float randomY = world.random.nextFloat();
                randomY = randomY < 0.5 ? -randomY : randomY - 0.5f;
                float randomZ = world.random.nextFloat();
                randomZ = randomZ < 0.5 ? -randomZ : randomZ - 0.5f;
                Vec3d originDelta = new Vec3d(randomX, randomY, randomZ);
                origin = origin.add(originDelta);
                float pitch = world.random.nextFloat() * 360.0F;
                float yaw = world.random.nextFloat() * 360.0F;
                float roll = world.random.nextFloat() * 360.0F;
                SlashRender slashRender = new SlashRender(
                        origin,
                        80,
                        new VertexColorSet(1.0f, 0.0f, 0.0f, 0.9f),
                        new VertexColorSet(0.4f, 0.0f, 0.0f, 0.9f),
                        new VertexColorSet(1.0f, 0.0f, 0.0f, 0.9f),
                        new VertexColorSet(1.0f, 0.9f, 0.9f, 1.0f)
                );
                slashRender.addTransformation(RotationAxis.POSITIVE_Y.rotationDegrees(90));
                slashRender.addTransformation(RotationAxis.NEGATIVE_X.rotationDegrees(90));
                slashRender.addTransformation(RotationAxis.POSITIVE_X.rotationDegrees(pitch));
                slashRender.addTransformation(RotationAxis.POSITIVE_Z.rotationDegrees(roll));
                slashRender.addTransformation(RotationAxis.POSITIVE_Y.rotationDegrees(yaw));
                slashRender.setSize(20.0f);
                SlashRendererManager.add(slashRender);
                player.sendMessage(Text.of("Number of slashes: " + SlashRendererManager.get().size()));
                CustomFogManager.add(new CustomFog(0.2f, 0.0f, 0.0f, -1));
                FakePlayerRendererManager.add(new FakePlayerRender(new GameProfile(UUID.randomUUID(), "orchidpuppy"), player.getPos(), player.getPitch(), player.getYaw(), 100, "orchidpuppy"));
                */

                /** Triggers Death Sequence */
                /*if (player.getServer() != null) {
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

                        }
                    }
                }*/

                /** Sends packet for screen flash*/
                /*
                if (player instanceof ServerPlayerEntity serverPlayerEntity) {
                    PacketByteBuf buf = PacketByteBufs.create();
                    buf.writeInt(20);
                    buf.writeInt(0xFFFFFF);
                    buf.writeInt(5);
                    buf.writeFloat(0.8f);
                    //ServerPlayNetworking.send(serverPlayerEntity, RinveniumPackets.ADD_SCREEN_FLASH, buf);
                }
                */
            }
        } else {
            if (world.isClient) { // Client Side Sneak

            } else { // Server Side Sneak
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.INSTANT_HEALTH, 20, 10));
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SATURATION, 20, 20));

                /** Sends packets for slash rendering */
                /*
                if (player instanceof ServerPlayerEntity serverPlayerEntity) {
                    if (livingEntity != null) {
                        PacketByteBuf buf = PacketByteBufs.create();
                        buf.writeInt(200);
                        buf.writeInt(0xFFFFFF);
                        buf.writeInt(1);
                        buf.writeFloat(1.0f);
                        buf.writeDouble(livingEntity.getX());
                        buf.writeDouble(livingEntity.getY());
                        buf.writeDouble(livingEntity.getZ());
                        buf.writeUuid(livingEntity.getUuid());
                        //ServerPlayNetworking.send(serverPlayerEntity, RinveniumPackets.ADD_IMPACT_FRAME, buf);

                        Vec3d origin = player.getPos().add(0, (player.getBoundingBox().maxY - player.getBoundingBox().minY) / 2, 0);
                        PacketByteBuf slashBuf = PacketByteBufs.create();
                        int[] ageDelta = new int[1];
                        for (int i = 0; i < 1; i++) {
                            ageDelta[i] = 20;
                        }
                        slashBuf = SlashRender.writeMultiple(slashBuf, origin, 16f, 60, true, 1, ageDelta);
                        ServerPlayNetworking.send(serverPlayerEntity, RinveniumPackets.ADD_MULTIPLE_SLASHES, slashBuf);

                    }
                }
                */
                //CustomFogManager.clear();

                /** Cancels Death Squence */
                /*
                DeathSequenceComponent deathSequenceComponent = DeathSequenceComponent.get(player);
                deathSequenceComponent.setBool(false);
                */
            }

        }
        return TypedActionResult.success(stack);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }
}