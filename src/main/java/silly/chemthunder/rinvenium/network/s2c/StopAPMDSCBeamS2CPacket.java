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

public class StopAPMDSCBeamS2CPacket {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        if (client.player != null) {
            if (client.world != null && client.world.getEntities() != null) {
                UUID uuid = buf.readUuid();
                APMDSCBeamManager manager = ((RenderContainer) client.player).getAPMDSCBeamManager();
                for (APMDSCBeamRender beam : manager.get()) {
                    if (beam.uuid.equals(uuid)) {
                        beam.stopBeam = true;
                    }
                }
            }
        }
    }
}
