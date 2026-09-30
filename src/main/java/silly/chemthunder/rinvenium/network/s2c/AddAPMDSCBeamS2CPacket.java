package silly.chemthunder.rinvenium.network.s2c;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.Vec3d;
import silly.chemthunder.rinvenium.render.APMDSCBeamRender;
import silly.chemthunder.rinvenium.render.manager.client.APMDSCBeamManager;
import silly.chemthunder.rinvenium.util.inject.RenderContainer;

import java.util.UUID;

public class AddAPMDSCBeamS2CPacket {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        if (client.player != null) {
            if (client.world != null && client.world.getEntities() != null) {
                UUID uuid = buf.readUuid();
                double startX = buf.readDouble();
                double startY = buf.readDouble();
                double startZ = buf.readDouble();
                double endX = buf.readDouble();
                double endY = buf.readDouble();
                double endZ = buf.readDouble();
                int maxAge = buf.readInt();
                APMDSCBeamManager manager = ((RenderContainer) client.player).getAPMDSCBeamManager();
                manager.add(new APMDSCBeamRender(uuid, new Vec3d(startX, startY, startZ), new Vec3d(endX, endY, endZ), maxAge));
            }
        }
    }
}
