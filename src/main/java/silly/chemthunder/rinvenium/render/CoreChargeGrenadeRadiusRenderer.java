package silly.chemthunder.rinvenium.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.render.*;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

public class CoreChargeGrenadeRadiusRenderer {
    public static void init() {
        WorldRenderEvents.END.register(context -> {
            drawCircle(context, new Vec3d(0, 90, 0), 30);
        });
    }

    private static void drawCircle(WorldRenderContext context, Vec3d centre, int radius) {
        Vec3d normalisedCentre = new Vec3d(centre.x - context.camera().getPos().x, centre.y - context.camera().getPos().y, centre.z - context.camera().getPos().z);
        List<Vec3d> vertices = new ArrayList<>();
        for (int i = 0; i <= 360; i++) {
            double x = Math.cos(Math.toRadians(i));
            double y = Math.sin(Math.toRadians(i));
            Vec3d vertex = new Vec3d(x, y, 0);
            vertex = new Vec3d(vertex.x * radius, vertex.y * radius, vertex.z * radius);
            vertex = normalisedCentre.add(vertex);
            vertices.add(vertex);
        }
        for (int i = 0; i < (vertices.size() / 2); i++) {
            int i1 = i;
            int i2 = i + 1;
            int i3 = 360 - i1;
            int i4 = 360 - i2;

            Vec3d c1 = vertices.get(i1);
            Vec3d c2 = vertices.get(i2);
            Vec3d c3 = vertices.get(i3);
            Vec3d c4 = vertices.get(i4);

            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShader(GameRenderer::getPositionColorProgram);

            BufferBuilder buffer = Tessellator.getInstance().getBuffer();

            buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

            Matrix4f matrix = context.matrixStack().peek().getPositionMatrix();

            buffer.vertex(matrix, (float) c1.x, (float) c1.y, (float) c1.z)
                .color(255, 0, 0, 255)
                .next();

            buffer.vertex(matrix, (float) c2.x, (float) c2.y, (float) c2.z)
                .color(255, 0, 0, 255)
                .next();

            buffer.vertex(matrix, (float) c3.x, (float) c3.y, (float) c3.z)
                .color(255, 0, 0, 255)
                .next();

            buffer.vertex(matrix, (float) c4.x, (float) c4.y, (float) c4.z)
                .color(255, 0, 0, 255)
                .next();

            BufferRenderer.drawWithGlobalProgram(buffer.end());

            buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

            buffer.vertex(matrix, (float) c4.x, (float) c4.y, (float) c4.z)
                .color(255, 0, 0, 255)
                .next();

            buffer.vertex(matrix, (float) c3.x, (float) c3.y, (float) c3.z)
                .color(255, 0, 0, 255)
                .next();

            buffer.vertex(matrix, (float) c2.x, (float) c2.y, (float) c2.z)
                .color(255, 0, 0, 255)
                .next();

            buffer.vertex(matrix, (float) c1.x, (float) c1.y, (float) c1.z)
                .color(255, 0, 0, 255)
                .next();

            BufferRenderer.drawWithGlobalProgram(buffer.end());
        }
    }
}
