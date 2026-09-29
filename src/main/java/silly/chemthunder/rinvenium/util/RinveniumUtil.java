package silly.chemthunder.rinvenium.util;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import silly.chemthunder.rinvenium.index.RinveniumStatusEffects;
import silly.chemthunder.rinvenium.particle.RailgunTrailParticleEffect;
import silly.chemthunder.rinvenium.particle.SmokeTrailParticleEffect;

import java.util.UUID;

public class RinveniumUtil {
    public static final UUID EMPTY_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    public static void sendDebugMessage(LivingEntity entity, boolean actionBar, String message) {
        if (entity instanceof PlayerEntity player && actionBar) {
            player.sendMessage(Text.of(message), true);
        } else {
            entity.sendMessage(Text.of(message));
        }
    }
    public static void sendDebugMessage(LivingEntity entity, boolean actionBar, Text message) {
        if (entity instanceof PlayerEntity player && actionBar) {
            player.sendMessage(message, true);
        } else {
            entity.sendMessage(message);
        }
    }

    /**Equation: -0.0002x^2 + 5*/
    public static double calculateDivergenceDropOff(double input) {
        return MathHelper.clamp(-0.0002 * (input * input) + 5.0, 0.0, 5.0);
    }

    /**Returns it in degrees*/
    public static float pitchFromVecDeg(Vec3d rotVec) {
        return (float) (-Math.toDegrees(Math.atan2(rotVec.y, Math.sqrt(rotVec.x * rotVec.x + rotVec.z * rotVec.z))));
    }
    /**Returns it in radians*/
    public static float pitchFromVecRad(Vec3d rotVec) {
        return (float) (-Math.atan2(rotVec.y, Math.sqrt(rotVec.x * rotVec.x + rotVec.z * rotVec.z)));
    }

    /**Returns it in degrees*/
    public static float yawFromVecDeg(Vec3d rotVec) {
        return (float) (Math.toDegrees(Math.atan2(rotVec.z, rotVec.x)) - 90.0F);
    }
    /**Returns it in radians*/
    public static float yawFromVecRad(Vec3d rotVec) {
        return (float) (Math.atan2(rotVec.z, rotVec.x) - 90.0F);
    }

    public static EntityHitResult raycastWithDivergenceBox(Entity entity, double maxDistance, float tickDelta, float divergence, double radius) {
        Vec3d camPos = entity.getCameraPosVec(tickDelta);
        Vec3d rot = entity.getRotationVec(tickDelta);
        rot = rot.add(
                entity.getWorld().random.nextTriangular(0.0, 0.0172275 * divergence),
                entity.getWorld().random.nextTriangular(0.0, 0.0172275 * divergence),
                entity.getWorld().random.nextTriangular(0.0, 0.0172275 * divergence)
        );
        Vec3d endPos = camPos.add(rot.x * maxDistance, rot.y * maxDistance, rot.z * maxDistance);

        Box box = new Box(camPos, endPos).expand(radius);
        return ProjectileUtil.raycast(
                entity,
                camPos,
                endPos,
                box,
                target -> !target.isSpectator() && target.canHit(),
                endPos.squaredDistanceTo(camPos)
        );
    }

    public static EntityHitResult raycastWithDivergenceBox(Entity entity, Vec3d start, Vec3d rot, double maxDistance, double radius, boolean includeFluids) {
        Vec3d endPos = start.add(rot.x * maxDistance, rot.y * maxDistance, rot.z * maxDistance);

        Box box = new Box(start, endPos).expand(radius);

        HitResult hitResult = raycastWithDivergence(entity, start, rot, maxDistance, includeFluids);

        EntityHitResult entityHitResult = ProjectileUtil.raycast(
                entity,
                start,
                endPos,
                box,
                target -> !target.isSpectator() && target.canHit(),
                endPos.squaredDistanceTo(start)
        );
        if (entityHitResult == null && hitResult != null) return null;

        if (hitResult != null && hitResult.getType() == HitResult.Type.BLOCK) {
            if (start.distanceTo(hitResult.getPos()) < start.distanceTo(entityHitResult.getPos())) return null;
        }

        return entityHitResult;
    }

    public static HitResult raycastWithDivergence(Entity entity, double maxDistance, float tickDelta, boolean includeFluids, float divergence) {
        Vec3d camPos = entity.getCameraPosVec(tickDelta);
        Vec3d rot = entity.getRotationVec(tickDelta);
        rot = rot.add(
                entity.getWorld().random.nextTriangular(0.0, 0.0172275 * divergence),
                entity.getWorld().random.nextTriangular(0.0, 0.0172275 * divergence),
                entity.getWorld().random.nextTriangular(0.0, 0.0172275 * divergence)
        );
        Vec3d endPos = camPos.add(rot.x * maxDistance, rot.y * maxDistance, rot.z * maxDistance);
        return entity.getWorld().raycast(
                new RaycastContext(
                        camPos,
                        endPos,
                        RaycastContext.ShapeType.OUTLINE,
                        includeFluids ? RaycastContext.FluidHandling.ANY : RaycastContext.FluidHandling.NONE,
                        entity
                )
        );
    }

    public static HitResult raycastWithDivergence(Entity entity, Vec3d start, Vec3d rot, double maxDistance, boolean includeFluids) {
        Vec3d endPos = start.add(rot.x * maxDistance, rot.y * maxDistance, rot.z * maxDistance);
        return entity.getWorld().raycast(
                new RaycastContext(
                        start,
                        endPos,
                        RaycastContext.ShapeType.OUTLINE,
                        includeFluids ? RaycastContext.FluidHandling.ANY : RaycastContext.FluidHandling.NONE,
                        entity
                )
        );
    }

    public static void spawnRaycastParticles(ServerWorld world, Vec3d start, Vec3d rot, double maxDistance, double step, double offset, ParticleEffect particle) {
        Vec3d endPos = start.add(rot.x * maxDistance, rot.y * maxDistance, rot.z * maxDistance);
        Vec3d direction = endPos.subtract(start);
        double length = direction.length();
        Vec3d normal = direction.normalize();
        for (double d = offset; d < length; d += step) {
            Vec3d spawnPos = start.add(normal.multiply(d));
            world.spawnParticles(particle, spawnPos.x, spawnPos.y, spawnPos.z, 1, 0.0, 0.0, 0.0, 0);
        }
    }

    public static void spawnRaycastParticles(ServerWorld world, Vec3d start, Vec3d rot, HitResult hitResult, double maxDistance, double step, double offset, ParticleEffect particle) {
        Vec3d endPos = start.add(rot.x * maxDistance, rot.y * maxDistance, rot.z * maxDistance);

        Vec3d direction = endPos.subtract(start);
        double length = direction.length();
        Vec3d normal = direction.normalize();
        if (hitResult != null) {
            if (hitResult.getType() != HitResult.Type.MISS) {
                Vec3d hitPos = hitResult.getPos();
                Vec3d hitDirection = hitPos.subtract(start);
                double hitLength = hitDirection.length();

                for (double d = offset; d < Math.min(length, hitLength); d += step) {
                    Vec3d spawnPos = start.add(normal.multiply(d));
                    world.spawnParticles(particle, spawnPos.x, spawnPos.y, spawnPos.z, 1, 0.0, 0.0, 0.0, 0);
                }
            } else {
                for (double d = offset; d < length; d += step) {
                    Vec3d spawnPos = start.add(normal.multiply(d));
                    world.spawnParticles(particle, spawnPos.x, spawnPos.y, spawnPos.z, 1, 0.0, 0.0, 0.0, 0);
                }
            }
        } else {
            for (double d = offset; d < length; d += step) {
                Vec3d spawnPos = start.add(normal.multiply(d));
                world.spawnParticles(particle, spawnPos.x, spawnPos.y, spawnPos.z, 1, 0.0, 0.0, 0.0, 0);
            }
        }
    }

    public static void spawnRaycastSmokeParticles(ServerWorld world, Vec3d start, Vec3d rot, HitResult hitResult, double maxDistance, double step, double offset, int particleAge) {
        Vec3d endPos = start.add(rot.x * maxDistance, rot.y * maxDistance, rot.z * maxDistance);
        SmokeTrailParticleEffect particle;
        Vec3d direction = endPos.subtract(start);
        double length = direction.length();
        Vec3d normal = direction.normalize();
        if (hitResult != null) {
            if (hitResult.getType() != HitResult.Type.MISS) {
                Vec3d hitPos = hitResult.getPos();
                Vec3d hitDirection = hitPos.subtract(start);
                double hitLength = hitDirection.length();

                for (double d = offset; d < Math.min(length, hitLength) * 0.8; d += step) {
                    Vec3d spawnPos = start.add(normal.multiply(d));
                    particle = new SmokeTrailParticleEffect((int) Math.ceil(d / Math.min(length, hitLength) * particleAge));
                    world.spawnParticles(particle, spawnPos.x, spawnPos.y, spawnPos.z, 1, 0.0, 0.0, 0.0, 0);
                }
            } else {
                for (double d = offset; d < length * 0.8; d += step) {
                    Vec3d spawnPos = start.add(normal.multiply(d));
                    particle = new SmokeTrailParticleEffect((int) Math.ceil(d / length * particleAge));
                    world.spawnParticles(particle, spawnPos.x, spawnPos.y, spawnPos.z, 1, 0.0, 0.0, 0.0, 0);
                }
            }
        } else {
            for (double d = offset; d < length * 0.8; d += step) {
                Vec3d spawnPos = start.add(normal.multiply(d));
                particle = new SmokeTrailParticleEffect((int) Math.ceil(d / length * particleAge));
                world.spawnParticles(particle, spawnPos.x, spawnPos.y, spawnPos.z, 1, 0.0, 0.0, 0.0, 0);
            }
        }
    }

    public static void spawnRaycastRailgunParticles(ServerWorld world, Vec3d start, Vec3d rot, HitResult hitResult, double maxDistance, double step, double offset, int particleAge) {
        Vec3d endPos = start.add(rot.x * maxDistance, rot.y * maxDistance, rot.z * maxDistance);
        RailgunTrailParticleEffect particle;
        Vec3d direction = endPos.subtract(start);
        double length = direction.length();
        Vec3d normal = direction.normalize();
        if (hitResult != null) {
            if (hitResult.getType() != HitResult.Type.MISS) {
                Vec3d hitPos = hitResult.getPos();
                Vec3d hitDirection = hitPos.subtract(start);
                double hitLength = hitDirection.length();

                for (double d = offset; d < Math.min(length, hitLength); d += step) {
                    Vec3d spawnPos = start.add(normal.multiply(d));
                    particle = new RailgunTrailParticleEffect((int) Math.ceil(d / Math.min(length, hitLength) * particleAge));
                    world.spawnParticles(particle, spawnPos.x, spawnPos.y, spawnPos.z, 1, 0.0, 0.0, 0.0, 0);
                }
            } else {
                for (double d = offset; d < length; d += step) {
                    Vec3d spawnPos = start.add(normal.multiply(d));
                    particle = new RailgunTrailParticleEffect((int) Math.ceil(d / length * particleAge));
                    world.spawnParticles(particle, spawnPos.x, spawnPos.y, spawnPos.z, 1, 0.0, 0.0, 0.0, 0);
                }
            }
        } else {
            for (double d = offset; d < length; d += step) {
                Vec3d spawnPos = start.add(normal.multiply(d));
                particle = new RailgunTrailParticleEffect((int) Math.ceil(d / length * particleAge));
                world.spawnParticles(particle, spawnPos.x, spawnPos.y, spawnPos.z, 1, 0.0, 0.0, 0.0, 0);
            }
        }
    }

    public static ItemStack exchangeWholeStack(ItemStack inputStack, PlayerEntity player, ItemStack outputStack, boolean creativeOverride) {
        boolean bl = player.isCreative();
        int count = inputStack.getCount();
        outputStack.setCount(count);
        if (creativeOverride && bl) {
            player.getInventory().insertStack(outputStack);
        } else {
            if (!player.getInventory().insertStack(outputStack)) {
                player.dropItem(outputStack, false);
                if (!player.isCreative()) {
                    inputStack.decrement(count);
                }
                return inputStack;
            }
            return outputStack;
        }
        return inputStack;
    }
    
    public static ItemStack exchangeWholeStack(ItemStack inputStack, PlayerEntity player, ItemStack outputStack) {
        return exchangeWholeStack(inputStack, player, outputStack, true);
    }

    public static int lerpColor(float tickDelta, int start, int end) {
        int r1 = (start >> 16) & 0xFF;
        int g1 = (start >> 8) & 0xFF;
        int b1 = start & 0xFF;

        int r2 = (end >> 16) & 0xFF;
        int g2 = (end >> 8) & 0xFF;
        int b2 = end & 0xFF;

        int r = MathHelper.lerp(tickDelta, r1, r2);
        int g = MathHelper.lerp(tickDelta, g1, g2);
        int b = MathHelper.lerp(tickDelta, b1, b2);

        return (r << 16) | (g << 8) | b;
    }

    public static int colorFromFloat(float rf, float gf, float bf) {
        int r = (int)  (rf * 255);
        int g = (int)  (gf * 255);
        int b = (int)  (bf * 255);

        return (r << 16) | (g << 8) | b;
    }

    public static boolean shouldLockPlayerMovement(ClientPlayerEntity player) {
        if (player == null || player.isDead()) return false;
        return player.hasStatusEffect(RinveniumStatusEffects.WATCHED);
    }
}