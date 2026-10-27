package silly.chemthunder.rinvenium.network.s2c;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Hand;
import silly.chemthunder.rinvenium.render.manager.client.FakePlayerRendererManager;
import silly.chemthunder.rinvenium.util.inject.client.RenderContainer;

public class FakePlayerSwingArmS2CPacket {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        if (client.world != null && client.world.getEntities() != null) {
            if (client.player != null) {
                FakePlayerRendererManager fakePlayerRendererManager = ((RenderContainer) client.player).getFakePlayerRendererManager();
                String name = buf.readString();
                fakePlayerRendererManager.get().forEach(fakePlayerRender -> {
                    if (fakePlayerRender.gameProfile.getName().equals(name)) {
                        fakePlayerRender.fakePlayer.swingHand(Hand.MAIN_HAND, false);
                    }
                });
            }
        }
    }
}
