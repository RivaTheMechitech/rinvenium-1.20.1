package silly.chemthunder.rinvenium.event.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import silly.chemthunder.rinvenium.Rinvenium;
import silly.chemthunder.rinvenium.render.APMDSCBeamRender;
import silly.chemthunder.rinvenium.render.FakePlayerRender;
import silly.chemthunder.rinvenium.render.ImpactFrame;
import silly.chemthunder.rinvenium.render.SlashRender;
import silly.chemthunder.rinvenium.render.manager.client.APMDSCBeamManager;
import silly.chemthunder.rinvenium.render.manager.client.ImpactFrameManager;
import silly.chemthunder.rinvenium.render.manager.client.FakePlayerRendererManager;
import silly.chemthunder.rinvenium.render.manager.client.SlashRendererManager;
import silly.chemthunder.rinvenium.util.RinveniumTextureUtils;
import silly.chemthunder.rinvenium.util.inject.RenderContainer;

import java.util.ArrayList;
import java.util.List;

public class WorldRendererListener {
    public static int time = 0;
    public static void execute() {
        WorldRenderEvents.LAST.register(context -> {
            MinecraftClient client = MinecraftClient.getInstance();
            ClientWorld world = client.world;
            Camera camera = context.camera();

            if (world != null) {
                if (client.player != null) {
                    ClientPlayerEntity player = client.player;
                    RenderContainer renderContainer = ((RenderContainer) player);
                    SlashRendererManager slashRendererManager = renderContainer.getSlashRendererManager();
                    slashRendererManager.tick();
                    time++;
                    slashRendererManager.get().forEach(slashRender -> renderSlashes(context, client, world, camera, slashRender));
                    renderContainer.getFakePlayerRendererManager().tick();
                    FakePlayerRendererManager fakePlayerRendererManager = renderContainer.getFakePlayerRendererManager();
                    fakePlayerRendererManager.tick();
                    fakePlayerRendererManager.get().forEach(fakePlayerRender -> renderPlayer(context, client, world, camera, fakePlayerRender));
                    ImpactFrameManager impactFrameManager = ((RenderContainer) client.player).getImpactFrameManager();
                    impactFrameManager.tick();
                    impactFrameManager.get().forEach(impactFrame -> {
                        if (impactFrameManager.shouldShow()) {
                            //client.options.hudHidden = true;
                            renderImpactFrame(context, client, world, camera, impactFrame, slashRendererManager);
                        } else {
                            //client.options.hudHidden = false;
                        }
                    });

                    APMDSCBeamManager apmdscBeamManager = ((RenderContainer) client.player).getAPMDSCBeamManager();
                    apmdscBeamManager.tick();
                    apmdscBeamManager.get().forEach(beam -> renderAPMDSCBeam(context, client, world, player, camera, beam));
                }
            }
        });
    }

    private static void renderAPMDSCBeam(WorldRenderContext context, MinecraftClient client, ClientWorld world, ClientPlayerEntity player, Camera camera, APMDSCBeamRender beam) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        double viewDistance = client.options.getClampedViewDistance() * 16;

        double camX = camera.getPos().getX();
        double camY = camera.getPos().getY();
        double camZ = camera.getPos().getZ();

        MatrixStack matrices = context.matrixStack();
        matrices.translate(-camX, -camY, -camZ);
        matrices.translate(beam.startPos.getX(), beam.startPos.getY(), beam.startPos.getZ());

        Matrix4f transformation = matrices.peek().getPositionMatrix();

        matrices.translate(-beam.startPos.getX(), -beam.startPos.getY(), -beam.startPos.getZ());
        matrices.translate(camX, camY, camZ);

        if (beam.startPos.squaredDistanceTo(camera.getPos()) < viewDistance * viewDistance) {
            RenderSystem.disableCull();
            RenderSystem.enableBlend();
            RenderSystem.depthMask(MinecraftClient.isFabulousGraphicsOrBetter());
            RenderSystem.enableDepthTest();

            bufferbuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

            buildAPMDSCBeamVertices(beam, bufferbuilder, transformation, camX, camY, camZ);

            RenderSystem.setShader(GameRenderer::getPositionColorProgram);
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

            tessellator.draw();

            RenderSystem.enableCull();
            RenderSystem.depthMask(true);
        }
    }

    private static void renderPlayer(WorldRenderContext context, MinecraftClient client, ClientWorld world, Camera camera, FakePlayerRender playerRenderer) {
        if (playerRenderer.fakePlayer == null) {
            playerRenderer.fakePlayer = new OtherClientPlayerEntity(world, playerRenderer.gameProfile) {
                @Override
                public Identifier getSkinTexture() {
                    return Rinvenium.id("textures/entity/fakeplayer/" + playerRenderer.skinTexture + ".png");
                }
            };
            playerRenderer.fakePlayer.prevYaw = playerRenderer.yaw;
            playerRenderer.fakePlayer.prevHeadYaw = playerRenderer.yaw;
            playerRenderer.fakePlayer.prevBodyYaw = playerRenderer.yaw;
            playerRenderer.fakePlayer.prevPitch = playerRenderer.pitch;
            playerRenderer.fakePlayer.setPitch(playerRenderer.pitch);
        }
        client.getEntityRenderDispatcher().render(
                playerRenderer.fakePlayer,
                playerRenderer.origin.x - camera.getPos().getX(),
                playerRenderer.origin.y - camera.getPos().getY(),
                playerRenderer.origin.z - camera.getPos().getZ(),
                playerRenderer.yaw,
                0.0f,
                context.matrixStack(),
                context.worldRenderer().bufferBuilders.getEntityVertexConsumers(),
                240
        );
    }

    private static void renderImpactFrame(WorldRenderContext context, MinecraftClient client, ClientWorld world, Camera camera, ImpactFrame impactFrame, SlashRendererManager slashRendererManager) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        double viewDistance = client.options.getClampedViewDistance() * 16;

        double camX = camera.getPos().getX();
        double camY = camera.getPos().getY();
        double camZ = camera.getPos().getZ();

        RenderSystem.depthMask(false);
        RenderSystem.disableDepthTest();
        RenderSystem.disableCull();

        RenderSystem.setShader(GameRenderer::getRenderTypeOutlineProgram);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

        for (Entity entity : world.getEntities()) {
            if (entity.getUuid().equals(impactFrame.uuid)) {
                BlockPos blockPos = entity.getBlockPos();
                if ((context.world().isOutOfHeightLimit(blockPos.getY()) || context.worldRenderer().isRenderingReady(blockPos))) {

                    if (entity.age == 0) {
                        entity.lastRenderX = entity.getX();
                        entity.lastRenderY = entity.getY();
                        entity.lastRenderZ = entity.getZ();
                    }

                    VertexConsumerProvider vertexConsumerProvider;
                    OutlineVertexConsumerProvider outlineVertexConsumerProvider = context.worldRenderer().bufferBuilders.getOutlineVertexConsumers();
                    vertexConsumerProvider = outlineVertexConsumerProvider;
                    int i = 0x000000;
                    outlineVertexConsumerProvider.setColor(ColorHelper.Argb.getRed(i), ColorHelper.Argb.getGreen(i), ColorHelper.Argb.getBlue(i), 255);

                    List<SlashRender> slashRenders = new ArrayList<>();
                    for (SlashRender slashRender : slashRendererManager.get()) {
                        if (entity.getBoundingBox().expand(5).contains(slashRender.presetOrigin)) {
                            slashRenders.add(slashRender);
                        }
                    }

                    VertexConsumer outlineVertexConsumer = outlineVertexConsumerProvider.getBuffer(RenderLayer.getOutline(RinveniumTextureUtils.SCREEN_FLASH));

                    for (SlashRender slash : slashRenders) {
                        float ageDelta = (float) slash.age / slash.maxAge;
                        float yDelta;
                        float zDelta;
                        //yDelta
                        float A1 = -6.06f;
                        float k1 = 0.16f;
                        float b1 = 4.76f;
                        float ageYDelta = ageDelta;
                        if (ageDelta <= 0.19f) {
                            yDelta = (float) (-A1 * ageYDelta * Math.exp(-b1 * (ageYDelta - k1)));
                        } else if (ageDelta > 0.19f && ageDelta <= 0.48f) {
                            yDelta = 1f;
                        } else {
                            ageYDelta = ageYDelta - 0.25f;
                            yDelta = (float) (-A1 * ageYDelta * Math.exp(-b1 * (ageYDelta - k1)));
                        }
                        // zDelta
                        float A2 = 1.68f;
                        float k2 = 0.056f;
                        float b2 = 6.2f;
                        float ageZDelta = ageDelta;
                        if (ageDelta <= 0.11f) {
                            zDelta = (float) (-A2 * ageZDelta * Math.exp(-Math.pow(b2, 2) * (Math.pow(ageZDelta, 2) - k2)));
                        } else if (ageDelta > 0.11f && ageDelta <= 0.35f) {
                            zDelta = 1;
                        } else {
                            ageZDelta = ageZDelta - 0.234f;
                            zDelta = (float) (-A2 * ageZDelta * Math.exp(-Math.pow(b2, 2) * (Math.pow(ageZDelta, 2) - k2)));
                        }
                        double endY = MathHelper.lerp(yDelta, slash.origin.y, slash.origin.add(slash.direction.normalize()).y);
                        double endYMid = MathHelper.lerp(yDelta, slash.origin.y, slash.origin.add(slash.direction.normalize().multiply(0.5f)).y);
                        double endZNeg = MathHelper.lerp(zDelta, slash.origin.z, slash.origin.add(slash.direction.add(slash.direction.normalize().multiply(0.1).rotateX((float) (Math.PI / 2))).normalize()).z);
                        double endZPos = MathHelper.lerp(zDelta, slash.origin.z, slash.origin.add(slash.direction.add(slash.direction.normalize().multiply(0.1).rotateX((float) -(Math.PI / 2))).normalize()).z);

                        MatrixStack matrices = context.matrixStack();
                        matrices.push();

                        matrices.translate(-camX, -camY, -camZ);
                        matrices.translate(slash.origin.add(slash.direction.normalize().multiply(0.5f)).x, slash.origin.add(slash.direction.normalize().multiply(0.5f)).y, slash.origin.add(slash.direction.normalize().multiply(0.5f)).z);

                        slash.TRANSFORMATION.forEach(matrices::multiply);
                        matrices.scale(slash.getSize(), slash.getSize(), slash.getSize());

                        Matrix4f transformation = matrices.peek().getPositionMatrix();

                        matrices.translate(-slash.origin.add(slash.direction.normalize().multiply(0.5f)).x, -slash.origin.add(slash.direction.normalize().multiply(0.5f)).y, -slash.origin.add(slash.direction.normalize().multiply(0.5f)).z);
                        matrices.translate(camX, camY, camZ);

                        buildSlashOutlineVertices(slash, outlineVertexConsumer, transformation, 0.0, camX, camY, camZ, endYMid, endZNeg, endY, endZPos);

                        matrices.pop();
                    }

                    context.worldRenderer().entityOutlinePostProcessor.render(context.tickDelta());
                    client.getFramebuffer().beginWrite(false);

                    context.worldRenderer().renderEntity(entity, camX, camY, camZ, context.tickDelta(), context.matrixStack(), vertexConsumerProvider);

                    VertexConsumerProvider.Immediate normal = context.worldRenderer().bufferBuilders.getEntityVertexConsumers();
                    RenderSystem.setShaderColor(255.0f, 255.0f, 255.0f, 1.0f);
                    context.worldRenderer().renderEntity(entity, camX, camY, camZ, context.tickDelta(), context.matrixStack(), normal);
                    normal.draw();
                    RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
                }
            }
        }

        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.enableCull();
        RenderSystem.enableBlend();
    }

    public static void renderSlashes(WorldRenderContext context, MinecraftClient client, ClientWorld world, Camera camera, SlashRender slash) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        double viewDistance = client.options.getClampedViewDistance() * 16;

        double camX = camera.getPos().getX();
        double camY = camera.getPos().getY();
        double camZ = camera.getPos().getZ();

        float ageDelta = (float) slash.age / slash.maxAge;
        float yDelta;
        float zDelta;
        float red;
        float nRed;
        // yDelta
        float A1 = -6.06f;
        float k1 = 0.16f;
        float b1 = 4.76f;
        float ageYDelta = ageDelta;
        if (ageDelta <= 0.19f) {
            yDelta = (float) (-A1 * ageYDelta * Math.exp(-b1 * (ageYDelta - k1)));
        } else if (ageDelta > 0.19f && ageDelta <= 0.48f) {
            yDelta = 1f;
        } else {
            ageYDelta = ageYDelta - 0.25f;
            yDelta = (float) (-A1 * ageYDelta * Math.exp(-b1 * (ageYDelta - k1)));
        }
        // zDelta
        float A2 = 1.68f;
        float k2 = 0.056f;
        float b2 = 6.2f;
        float ageZDelta = ageDelta;
        if (ageDelta <= 0.11f) {
            zDelta = (float) (-A2 * ageZDelta * Math.exp(-Math.pow(b2, 2) * (Math.pow(ageZDelta, 2) - k2)));
        } else if (ageDelta > 0.11f && ageDelta <= 0.35f) {
            zDelta = 1;
        } else {
            ageZDelta = ageZDelta - 0.234f;
            zDelta = (float) (-A2 * ageZDelta * Math.exp(-Math.pow(b2, 2) * (Math.pow(ageZDelta, 2) - k2)));
        }
        // red
        red = (float) Math.min(-0.65 * Math.exp(-5.5 * ageDelta) + 1.05, 1.0);
        // green and blue
        nRed = (float) MathHelper.clamp(-0.8 * Math.exp(-10 * (ageDelta - 0.5)) + 0.8, 0.0, 1.0);

        Vec3d positionOffset = slash.direction.normalize().multiply(slash.size).multiply(0.5f);

        MatrixStack matrices = context.matrixStack();
        matrices.push();

        matrices.translate(-camX, -camY, -camZ);
        matrices.translate(slash.origin.add(slash.direction.normalize().multiply(0.5f)).x, slash.origin.add(slash.direction.normalize().multiply(0.5f)).y, slash.origin.add(slash.direction.normalize().multiply(0.5f)).z);

        slash.TRANSFORMATION.forEach(matrices::multiply);
        matrices.scale(slash.getSize(), slash.getSize(), slash.getSize());

        Matrix4f transformation = matrices.peek().getPositionMatrix();

        matrices.translate(-slash.origin.add(slash.direction.normalize().multiply(0.5f)).x, -slash.origin.add(slash.direction.normalize().multiply(0.5f)).y, -slash.origin.add(slash.direction.normalize().multiply(0.5f)).z);
        matrices.translate(camX, camY, camZ);

        if (slash.origin.squaredDistanceTo(camera.getPos()) < viewDistance * viewDistance) {
            RenderSystem.disableCull();
            RenderSystem.enableBlend();
            RenderSystem.depthMask(MinecraftClient.isFabulousGraphicsOrBetter());
            RenderSystem.enableDepthTest();

            /*if (SlashRendererManager.slashBuffer != null) {
                SlashRendererManager.slashBuffer.close();
            }
            SlashRendererManager.slashBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);*/

            bufferbuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

            double xOffset = 0.0;

            double endY = MathHelper.lerp(yDelta, slash.origin.y, slash.origin.add(slash.direction.normalize()).y);
            double endYMid = MathHelper.lerp(yDelta, slash.origin.y, slash.origin.add(slash.direction.normalize().multiply(0.5f)).y);
            double endZNeg = MathHelper.lerp(zDelta, slash.origin.z, slash.origin.add(slash.direction.add(slash.direction.normalize().multiply(0.1).rotateX((float) (Math.PI / 2))).normalize()).z);
            double endZPos = MathHelper.lerp(zDelta, slash.origin.z, slash.origin.add(slash.direction.add(slash.direction.normalize().multiply(0.1).rotateX((float) -(Math.PI / 2))).normalize()).z);
            double edgeOffset = MathHelper.lerp(zDelta, 0, slash.direction.normalize().multiply(slash.getSize()).multiply(0.05).y);

            boolean shouldRender = true;
            if (client.player != null) {
                shouldRender = ((RenderContainer) client.player).getImpactFrameManager().get().isEmpty();
            }
            if (shouldRender) {
                buildSlashVertices(slash, bufferbuilder, transformation, xOffset, camX, camY, camZ, red, nRed, endYMid, endZNeg, endY, endZPos);
            }

            matrices.pop();

            /*BufferBuilder.BuiltBuffer builtBuffer = bufferbuilder.end();
            SlashRendererManager.slashBuffer.bind();
            SlashRendererManager.slashBuffer.upload(builtBuffer);
            VertexBuffer.unbind();*/

            RenderSystem.setShader(GameRenderer::getPositionColorProgram);
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

            /*if (SlashRendererManager.slashBuffer != null) {
                SlashRendererManager.slashBuffer.bind();
                ShaderProgram shaderProgram = RenderSystem.getShader();
                Matrix4f positionMatrix = RenderSystem.getModelViewStack().peek().getPositionMatrix();

                SlashRendererManager.slashBuffer.draw(positionMatrix, RenderSystem.getProjectionMatrix(), shaderProgram);
            }*/
            tessellator.draw();

            RenderSystem.enableCull();
            RenderSystem.depthMask(true);
        }
    }

    private static void buildSlashVertices(SlashRender slash, BufferBuilder bufferbuilder, Matrix4f transformation, double xOffset, double camX, double camY, double camZ, float red, float nRed, double endYMid, double endZNeg, double endY, double endZPos) {
        bufferbuilder.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (endYMid - camY), (float) (slash.origin.z - camZ)).color(1.0f, 0.9f, 0.9f, 0.9f).next();
        bufferbuilder.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (slash.origin.y - camY), (float) (slash.origin.z - camZ)).color(red, nRed, nRed, 0.9f).next();
        bufferbuilder.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (endYMid - camY), (float) (endZNeg - camZ)).color(red, nRed, nRed, 0.9f).next();
        bufferbuilder.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (endY - camY), (float) (slash.origin.z - camZ)).color(red, nRed, nRed, 0.9f).next();

        bufferbuilder.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (endYMid - camY), (float) (slash.origin.z - camZ)).color(1.0f, 0.9f, 0.9f, 0.9f).next();
        bufferbuilder.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (slash.origin.y - camY), (float) (slash.origin.z - camZ)).color(red, nRed, nRed, 0.9f).next();
        bufferbuilder.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (endYMid - camY), (float) (endZPos - camZ)).color(red, nRed, nRed, 0.9f).next();
        bufferbuilder.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (endY - camY), (float) (slash.origin.z - camZ)).color(red, nRed, nRed, 0.9f).next();
    }
    private static void buildSlashOutlineVertices(SlashRender slash, VertexConsumer vertexConsumer, Matrix4f transformation, double xOffset, double camX, double camY, double camZ, double endYMid, double endZNeg, double endY, double endZPos) {
        vertexConsumer.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (endYMid - camY), (float) (slash.origin.z - camZ)).color(1.0f, 1.0f, 1.0f, 1.0f).texture(1, 1).normal(1, 0, 0).next();
        vertexConsumer.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (slash.origin.y - camY), (float) (slash.origin.z - camZ)).color(1.0f, 1.0f, 1.0f, 1.0f).texture(1, 0).normal(1, 0, 0).next();
        vertexConsumer.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (endYMid - camY), (float) (endZNeg - camZ)).color(1.0f, 1.0f, 1.0f, 1.0f).texture(0, 0).normal(1, 0, 0).next();
        vertexConsumer.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (endY - camY), (float) (slash.origin.z - camZ)).color(1.0f, 1.0f, 1.0f, 1.0f).texture(0, 1).normal(1, 0, 0).next();

        vertexConsumer.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (endYMid - camY), (float) (slash.origin.z - camZ)).color(1.0f, 1.0f, 1.0f, 1.0f).texture(1, 1).normal(1, 0, 0).next();
        vertexConsumer.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (slash.origin.y - camY), (float) (slash.origin.z - camZ)).color(1.0f, 1.0f, 1.0f, 1.0f).texture(1, 0).normal(1, 0, 0).next();
        vertexConsumer.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (endYMid - camY), (float) (endZPos - camZ)).color(1.0f, 1.0f, 1.0f, 1.0f).texture(0, 0).normal(1, 0, 0).next();
        vertexConsumer.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (endY - camY), (float) (slash.origin.z - camZ)).color(1.0f, 1.0f, 1.0f, 1.0f).texture(0, 1).normal(1, 0, 0).next();
    }

    private static void buildSlashOutlineVertices_(SlashRender slash, BufferBuilder bufferbuilder, MatrixStack matrices, Matrix4f transformation, double xOffset, double camX, double camY, double camZ, double endYMid, double endZNeg, double endY, double endZPos) {
        float outlineScalar = 1.2f;
        matrices.translate(-camX, -camY, -camZ);
        matrices.translate(slash.origin.add(slash.direction.normalize().multiply(0.5f)).x, slash.origin.add(slash.direction.normalize().multiply(0.5f)).y, slash.origin.add(slash.direction.normalize().multiply(0.5f)).z);

        matrices.scale(outlineScalar, outlineScalar, outlineScalar);

        matrices.translate(-slash.origin.add(slash.direction.normalize().multiply(0.5f)).x, -slash.origin.add(slash.direction.normalize().multiply(0.5f)).y, -slash.origin.add(slash.direction.normalize().multiply(0.5f)).z);
        matrices.translate(camX, camY, camZ);

        bufferbuilder.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (endYMid - camY), (float) (slash.origin.z - camZ)).color(0.0f, 0.0f, 0.0f, 1.0f).next();
        bufferbuilder.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (slash.origin.y - camY), (float) (slash.origin.z - camZ)).color(0.0f, 0.0f, 0.0f, 1.0f).next();
        bufferbuilder.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (endYMid - camY), (float) (endZNeg - camZ)).color(0.0f, 0.0f, 0.0f, 1.0f).next();
        bufferbuilder.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (endY - camY), (float) (slash.origin.z - camZ)).color(0.0f, 0.0f, 0.0f, 1.0f).next();

        bufferbuilder.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (endYMid - camY), (float) (slash.origin.z - camZ)).color(0.0f, 0.0f, 0.0f, 1.0f).next();
        bufferbuilder.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (slash.origin.y - camY), (float) (slash.origin.z - camZ)).color(0.0f, 0.0f, 0.0f, 1.0f).next();
        bufferbuilder.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (endYMid - camY), (float) (endZPos - camZ)).color(0.0f, 0.0f, 0.0f, 1.0f).next();
        bufferbuilder.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (endY - camY), (float) (slash.origin.z - camZ)).color(0.0f, 0.0f, 0.0f, 1.0f).next();

        matrices.translate(-camX, -camY, -camZ);
        matrices.translate(slash.origin.add(slash.direction.normalize().multiply(0.5f)).x, slash.origin.add(slash.direction.normalize().multiply(0.5f)).y, slash.origin.add(slash.direction.normalize().multiply(0.5f)).z);

        matrices.scale(1 / outlineScalar, 1 / outlineScalar, 1 / outlineScalar);

        matrices.translate(-slash.origin.add(slash.direction.normalize().multiply(0.5f)).x, -slash.origin.add(slash.direction.normalize().multiply(0.5f)).y, -slash.origin.add(slash.direction.normalize().multiply(0.5f)).z);
        matrices.translate(camX, camY, camZ);

        xOffset = 4.98E-5;

        bufferbuilder.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (endYMid - camY), (float) (slash.origin.z - camZ)).color(1.0f, 1.0f, 1.0f, 1.0f).next();
        bufferbuilder.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (slash.origin.y - camY), (float) (slash.origin.z - camZ)).color(1.0f, 1.0f, 1.0f, 1.0f).next();
        bufferbuilder.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (endYMid - camY), (float) (endZNeg - camZ)).color(1.0f, 1.0f, 1.0f, 1.0f).next();
        bufferbuilder.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (endY - camY), (float) (slash.origin.z - camZ)).color(1.0f, 1.0f, 1.0f, 1.0f).next();

        bufferbuilder.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (endYMid - camY), (float) (slash.origin.z - camZ)).color(1.0f, 1.0f, 1.0f, 1.0f).next();
        bufferbuilder.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (slash.origin.y - camY), (float) (slash.origin.z - camZ)).color(1.0f, 1.0f, 1.0f, 1.0f).next();
        bufferbuilder.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (endYMid - camY), (float) (endZPos - camZ)).color(1.0f, 1.0f, 1.0f, 1.0f).next();
        bufferbuilder.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (endY - camY), (float) (slash.origin.z - camZ)).color(1.0f, 1.0f, 1.0f, 1.0f).next();

        bufferbuilder.vertex(transformation, (float) (slash.origin.x - xOffset - camX), (float) (endYMid - camY), (float) (slash.origin.z - camZ)).color(1.0f, 1.0f, 1.0f, 1.0f).next();
        bufferbuilder.vertex(transformation, (float) (slash.origin.x - xOffset - camX), (float) (slash.origin.y - camY), (float) (slash.origin.z - camZ)).color(1.0f, 1.0f, 1.0f, 1.0f).next();
        bufferbuilder.vertex(transformation, (float) (slash.origin.x - xOffset - camX), (float) (endYMid - camY), (float) (endZNeg - camZ)).color(1.0f, 1.0f, 1.0f, 1.0f).next();
        bufferbuilder.vertex(transformation, (float) (slash.origin.x - xOffset - camX), (float) (endY - camY), (float) (slash.origin.z - camZ)).color(1.0f, 1.0f, 1.0f, 1.0f).next();

        bufferbuilder.vertex(transformation, (float) (slash.origin.x - xOffset - camX), (float) (endYMid - camY), (float) (slash.origin.z - camZ)).color(1.0f, 1.0f, 1.0f, 1.0f).next();
        bufferbuilder.vertex(transformation, (float) (slash.origin.x - xOffset - camX), (float) (slash.origin.y - camY), (float) (slash.origin.z - camZ)).color(1.0f, 1.0f, 1.0f, 1.0f).next();
        bufferbuilder.vertex(transformation, (float) (slash.origin.x - xOffset - camX), (float) (endYMid - camY), (float) (endZPos - camZ)).color(1.0f, 1.0f, 1.0f, 1.0f).next();
        bufferbuilder.vertex(transformation, (float) (slash.origin.x - xOffset - camX), (float) (endY - camY), (float) (slash.origin.z - camZ)).color(1.0f, 1.0f, 1.0f, 1.0f).next();
    }

    private static void buildAPMDSCBeamVertices(APMDSCBeamRender beam, BufferBuilder bufferbuilder, Matrix4f transformation, double camX, double camY, double camZ) {
        bufferbuilder.vertex(transformation, (float) (beam.startPos.x + 0.375 - camX), (float) (beam.startPos.y + 0.375 - camY), (float) (beam.startPos.z + 0.375 - camZ)).color(0.0f, 0.8f, 0.8f, 0.4f); // Top right start
        bufferbuilder.vertex(transformation, (float) (beam.endPos.x + 0.375 - camX), (float) (beam.endPos.y + 0.375 - camY), (float) (beam.endPos.z + 0.375 - camZ)).color(0.0f, 0.8f, 0.8f, 0.4f); // Top right end
        bufferbuilder.vertex(transformation, (float) (beam.endPos.x + 0.375 - camX), (float) (beam.endPos.y - 0.375 - camY), (float) (beam.endPos.z + 0.375 - camZ)).color(0.0f, 0.8f, 0.8f, 0.4f); // Bottom right end
        bufferbuilder.vertex(transformation, (float) (beam.startPos.x + 0.375 - camX), (float) (beam.startPos.y + 0.375 - camY), (float) (beam.startPos.z + 0.375 - camZ)).color(0.0f, 0.8f, 0.8f, 0.4f); // Bottom right start
    }
}
