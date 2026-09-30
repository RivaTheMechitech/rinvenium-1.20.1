package silly.chemthunder.rinvenium.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.lwjgl.glfw.GLFW;
import silly.chemthunder.rinvenium.index.RinveniumStatusEffects;
import silly.chemthunder.rinvenium.item.DescriptionItem;
import silly.chemthunder.rinvenium.particle.RailgunTrailParticleEffect;
import silly.chemthunder.rinvenium.particle.SmokeTrailParticleEffect;

import java.util.List;
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

    /** Adds a tooltip that shows when [Shift] is held down.
     * If an item is and instance of {@link  DescriptionItem}, an anonymous class can be made in the registry that overrides the default formatting.<br>
     * For example:
     * <blockquote><pre>
     *     Item ION_CELL = create("ion_cell", new DescriptionItem(new FabricItemSettings().food(RinveniumFoodComponents.ION_CELL), "ion_cell"));
     * </pre></blockquote>
     * This can be overridden as:
     * <blockquote><pre>
     *     Item ION_CELL = create("ion_cell", new DescriptionItem(new FabricItemSettings().food(RinveniumFoodComponents.ION_CELL), "ion_cell") {
     *         &#64;Override
     *         public void appendTooltip(ItemStack stack, &#64;Nullable World world, List&lt;Text&gt; tooltip, TooltipContext context){
     *             RinveniumUtil.addExpandableTooltip(...);
     *             super.appendTooltip(stack, world, tooltip, context);
     *         }
     *     });
     * </pre></blockquote>
     * @param text The {@link Text} that will show when [Shift] is held down. By default, the {@link DescriptionItem}
     *             has this method implemented and is formatted to {@link Formatting#GRAY}.
     * @param fallback The {@link Text} that will show to prompt the player to press [Shift].
     * @param tooltip The tooltip parameter given by {@link Item#appendTooltip(ItemStack, World, List, TooltipContext)}
     * @param shouldWrap Whether the {@code text} should be wrapped. Text wrapping length is defined by {@link #MAX_WRAPPED_TOOLTIP_CHAR_LENGTH}.
     */
    public static void addExpandableTooltip(Text text, Text fallback, List<Text> tooltip, boolean shouldWrap) {
        MinecraftClient client = MinecraftClient.getInstance();
        long window = client.getWindow().getHandle();
        boolean shiftKeyDown = GLFW.glfwGetKey(window, GLFW.GLFW_KEY_LEFT_SHIFT) == GLFW.GLFW_PRESS || GLFW.glfwGetKey(window, GLFW.GLFW_KEY_RIGHT_SHIFT) == GLFW.GLFW_PRESS;
        if (shiftKeyDown) {
            if (shouldWrap) {
                addWrappedTooltip(text, tooltip);
            } else {
                tooltip.add(text);
            }
        } else {
            tooltip.add(fallback);
        }
    }

    public static final int MAX_WRAPPED_TOOLTIP_CHAR_LENGTH = 40;

    public static void addWrappedTooltip(Text text, List<Text> tooltip) {
        String fullText = text.getString();
        Style style = text.getStyle();
        String[] paragraphs = fullText.split("\\\\n");
        for (String paragraph : paragraphs) {
            paragraph = paragraph.trim();
            String[] wordsPerParagraph = paragraph.split(" ");
            if (wordsPerParagraph.length == 0) {
                tooltip.add(Text.literal(" "));
            } else {
                int characterCount = 0;
                String line = "";
                for (String word : wordsPerParagraph) {
                    characterCount += word.length();
                    characterCount++;
                    if (characterCount > MAX_WRAPPED_TOOLTIP_CHAR_LENGTH) {
                        tooltip.add(Text.literal(line).setStyle(style));
                        line = "";
                        characterCount = 0;
                    }
                    line = line.concat(word).concat(" ");
                }
                if (!line.isEmpty()) {
                    tooltip.add(Text.literal(line).setStyle(style));
                }
            }
        }

    }
}