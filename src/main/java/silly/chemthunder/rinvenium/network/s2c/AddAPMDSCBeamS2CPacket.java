package silly.chemthunder.rinvenium.network.s2c;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.Vec3d;
import silly.chemthunder.rinvenium.item.APMDSCItem;
import silly.chemthunder.rinvenium.render.APMDSCBeamRender;
import silly.chemthunder.rinvenium.render.manager.client.APMDSCBeamManager;
import silly.chemthunder.rinvenium.util.inject.client.RenderContainer;

import java.util.UUID;

public class AddAPMDSCBeamS2CPacket {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        if (client.player != null) {
            if (client.world != null) {
                UUID uuid = buf.readUuid();
                UUID owner = buf.readUuid();
                /*double startX = buf.readDouble();
                double startY = buf.readDouble();
                double startZ = buf.readDouble();*/
                int maxAge = buf.readInt();
                int beamTypeOrdinal = buf.readInt();
                APMDSCItem.BeamType beamType = APMDSCItem.BeamType.values()[beamTypeOrdinal];

                Vec3d startPos = null;
                AbstractClientPlayerEntity player = null;
                for (AbstractClientPlayerEntity player2 : client.world.getPlayers()) {
                    if (player2.getUuid().equals(owner)) {
                        player = player2;
                        startPos = player.getEyePos().add(player.getRotationVecClient().normalize().multiply(1.0f));
                    }
                }
                if (player != null) {
                    APMDSCBeamManager manager = ((RenderContainer) client.player).getAPMDSCBeamManager();
                    if (startPos != null) {
                        manager.add(new APMDSCBeamRender(uuid, owner, startPos /*new Vec3d(startX, startY, startZ)*/, maxAge, beamType));
                    }
                }
            }
        }
    }
}
