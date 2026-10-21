package silly.chemthunder.rinvenium.network.s2c;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import silly.chemthunder.rinvenium.render.APMDSCBeamRender;
import silly.chemthunder.rinvenium.render.manager.client.APMDSCBeamManager;
import silly.chemthunder.rinvenium.util.inject.SyncedRenderContainer;
import silly.chemthunder.rinvenium.util.inject.client.RenderContainer;

import java.util.UUID;

public class StopAPMDSCBeamS2CPacket {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        if (client.player != null && client.world != null) {
            PlayerEntity player = client.world.getPlayerByUuid(buf.readUuid());
            if (player != null) {
                ((SyncedRenderContainer) player).setShouldRenderAPMDSCBeam(false);
            }
        }
    }
}
