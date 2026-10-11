package silly.chemthunder.rinvenium.network.s2c;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import silly.chemthunder.rinvenium.render.APMDSCBeamRender;
import silly.chemthunder.rinvenium.render.manager.client.APMDSCBeamManager;
import silly.chemthunder.rinvenium.util.RinveniumUtil;
import silly.chemthunder.rinvenium.util.inject.RenderContainer;

import java.util.UUID;

public class AddAPMDSCBeamS2CPacket {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        if (client.player != null) {
            if (client.world != null) {
                double startX = buf.readDouble();
                double startY = buf.readDouble();
                double startZ = buf.readDouble();
                HitResult hitResult = client.player.raycast(6.0, 0.0f, false);
                Vec3d endPos;
                if (hitResult.getType() == HitResult.Type.BLOCK) {
                    BlockHitResult blockHitResult = (BlockHitResult) hitResult;
                    endPos = blockHitResult.getPos();
                } else {
                    endPos = hitResult.getPos();
                }
                double endX = endPos.getX();
                double endY = endPos.getY();
                double endZ = endPos.getZ();
                int maxAge = buf.readInt();
                UUID uuid = buf.readUuid();
                APMDSCBeamManager manager = ((RenderContainer) client.player).getAPMDSCBeamManager();
                manager.add(new APMDSCBeamRender(uuid, new Vec3d(startX, startY, startZ), new Vec3d(endX, endY, endZ), maxAge));
                RinveniumUtil.sendDebugMessage(client.player, true, "beam list size: " + manager.get().size());
            }
        }
    }
}
