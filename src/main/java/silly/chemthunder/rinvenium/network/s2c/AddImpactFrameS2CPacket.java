package silly.chemthunder.rinvenium.network.s2c;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.Vec3d;
import silly.chemthunder.rinvenium.render.ImpactFrame;
import silly.chemthunder.rinvenium.render.manager.client.ImpactFrameManager;
import silly.chemthunder.rinvenium.util.inject.client.RenderContainer;

import java.util.UUID;

public class AddImpactFrameS2CPacket {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        if (client.world != null && client.world.getEntities() != null) {
            if (client.player != null) {
                int maxAge = buf.readInt();
                int color = buf.readInt();
                int fadeTime = buf.readInt();
                float maxOpacity = buf.readFloat();
                double originX = buf.readDouble();
                double originY = buf.readDouble();
                double originZ = buf.readDouble();
                UUID uuid = buf.readUuid();
                ImpactFrameManager impactFrameManager = ((RenderContainer) client.player).getImpactFrameManager();
                impactFrameManager.add(new ImpactFrame(uuid, new Vec3d(originX, originY, originZ), maxAge, color, fadeTime, maxOpacity));
            }
        }
    }
}
