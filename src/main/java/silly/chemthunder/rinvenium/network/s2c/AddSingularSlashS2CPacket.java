package silly.chemthunder.rinvenium.network.s2c;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import silly.chemthunder.rinvenium.render.SlashRender;
import silly.chemthunder.rinvenium.util.inject.client.RenderContainer;

public class AddSingularSlashS2CPacket {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        if (client.world != null && client.world.getEntities() != null) {
            if (client.player != null) {
                Random random = client.player.getRandom();
                double x = buf.readDouble();
                double y = buf.readDouble();
                double z = buf.readDouble();
                float size = buf.readFloat();
                int maxAge = buf.readInt();
                boolean randomTransform = buf.readBoolean();
                Vec3d origin = new Vec3d(x, y, z);
                if (randomTransform) {
                    float randomX = random.nextFloat();
                    randomX = randomX < 0.5 ? -randomX : randomX - 0.5f;
                    float randomY = random.nextFloat();
                    randomY = randomY < 0.5 ? -randomY : randomY - 0.5f;
                    float randomZ = random.nextFloat();
                    randomZ = randomZ < 0.5 ? -randomZ : randomZ - 0.5f;
                    Vec3d originDelta = new Vec3d(randomX, randomY, randomZ).normalize().multiply(0.3f);
                    origin = origin.add(originDelta);
                }
                SlashRender slashRender = new SlashRender(origin, maxAge);
                slashRender.addTransformation(RotationAxis.POSITIVE_Y.rotationDegrees(90));
                slashRender.addTransformation(RotationAxis.NEGATIVE_X.rotationDegrees(90));
                if (randomTransform) {
                    float pitch = random.nextFloat() * 360.0F;
                    float yaw = random.nextFloat() * 360.0F;
                    float roll = random.nextFloat() * 360.0F;
                    slashRender.addTransformation(RotationAxis.POSITIVE_X.rotationDegrees(pitch));
                    slashRender.addTransformation(RotationAxis.POSITIVE_Z.rotationDegrees(roll));
                    slashRender.addTransformation(RotationAxis.POSITIVE_Y.rotationDegrees(yaw));
                }
                slashRender.setSize(size);
                ((RenderContainer) client.player).getSlashRendererManager().add(slashRender);

            }
        }
    }
}
