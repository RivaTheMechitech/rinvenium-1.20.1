package silly.chemthunder.rinvenium.util;

import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.joml.Vector3f;

public class ModParticleUtil {

    public static void addExpandingSphereOfParticles(World world, Vec3d origin, int count, double speed, ParticleEffect particle) {
        if (!world.isClient) return;

        for (int i = 0; i < count; i++) {
            double theta = Math.acos(2 * world.random.nextDouble() - 1);
            double phi = 2 * Math.PI * world.random.nextDouble();

            double dx = Math.sin(theta) * Math.cos(phi);
            double dy = Math.cos(theta);
            double dz = Math.sin(theta) * Math.sin(phi);

            double length = Math.sqrt(dx * dx + dy * dy + dz * dz);
            dx /= length;
            dy /= length;
            dz /= length;
            dx *= speed;
            dy *= speed;
            dz *= speed;

            world.addParticle(
                    particle,
                    origin.x,
                    origin.y,
                    origin.z,
                    dx,
                    dy,
                    dz
            );
        }
    }

    public static void addCollapsingSphereOfParticles(World world, Vec3d origin, int count, double speed, ParticleEffect particle, double maxRadius) {
        if (!world.isClient) return;

        for (int i = 0; i < count; i++) {
            double theta = Math.acos(2 * world.random.nextDouble() - 1);
            double phi = 2 * Math.PI * world.random.nextDouble();

            double dx = maxRadius * Math.sin(theta) * Math.cos(phi);
            double dy = maxRadius * Math.cos(theta);
            double dz = maxRadius * Math.sin(theta) * Math.sin(phi);

            double length = Math.sqrt(dx * dx + dy * dy + dz * dz);
            double velX = (dx / length) * speed;
            double velY = (dy / length) * speed;
            double velZ = (dz / length) * speed;

            world.addParticle(
                    particle,
                    origin.x + dx,
                    origin.y + dy,
                    origin.z + dz,
                    -velX,
                    -velY,
                    -velZ
            );
        }
    }

    public static void addExpandingRingOfParticles(World world, Vec3d origin, double yGap, int rings, int count, double speed, ParticleEffect particle) {
        for (int j = (int) -Math.floor((double) rings / 2); j < Math.ceil((double) rings / 2); j++) {
            for (int i = 0; i < count; i++) {
                double theta = Math.acos(2 * world.random.nextDouble() - 1);
                double phi = 2 * Math.PI * world.random.nextDouble();

                double dx = Math.sin(theta) * Math.cos(phi);
                double dz = Math.sin(theta) * Math.sin(phi);

                double length = Math.sqrt(dx * dx + 1 + dz * dz);
                dx /= length;
                dz /= length;
                dx *= speed;
                dz *= speed;

                if (world instanceof ServerWorld serverWorld) {
                    serverWorld.spawnParticles(
                            particle,
                            origin.x,
                            origin.y + j * yGap,
                            origin.z,
                            1,
                            dx,
                            0,
                            dz,
                            speed / 3
                    );
                }
            }
        }
    }

    public static void addCollapsingRingOfParticles(World world, Vec3d origin, double yGap, int rings, int count, double speed, ParticleEffect particle, double maxRadius) {
        if (!world.isClient) return;
        for (int j = (int) -Math.floor((double) rings / 2); j < Math.ceil((double) rings / 2); j++) {
            for (int i = 0; i < count; i++) {
                double theta = Math.acos(2 * world.random.nextDouble() - 1);
                double phi = 2 * Math.PI * world.random.nextDouble();

                double dx = maxRadius * Math.sin(theta) * Math.cos(phi);
                double dz = maxRadius * Math.sin(theta) * Math.sin(phi);

                double length = Math.sqrt(dx * dx + dz * dz);
                double velX = (dx / length) * speed;
                double velZ = (dz / length) * speed;

                world.addParticle(
                        particle,
                        origin.x + dx,
                        origin.y + j * yGap,
                        origin.z + dz,
                        -velX,
                        0,
                        -velZ
                );
            }
        }
    }

    public static Vector3f colorToVec3f(int color) {
        return Vec3d.unpackRgb(color).toVector3f();
    }
    
    public static Vec3d colorToVec3d(int color) {
        return Vec3d.unpackRgb(color);
    }
}