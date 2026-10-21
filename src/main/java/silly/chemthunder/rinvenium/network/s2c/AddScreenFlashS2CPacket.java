package silly.chemthunder.rinvenium.network.s2c;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import silly.chemthunder.rinvenium.render.ScreenFlash;
import silly.chemthunder.rinvenium.render.manager.client.FlashManager;
import silly.chemthunder.rinvenium.util.inject.client.RenderContainer;

public class AddScreenFlashS2CPacket {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        int maxAge = buf.readInt();
        int color = buf.readInt();
        int fadeTime = buf.readInt();
        float maxOpacity = buf.readFloat();
        
        if (client.player != null) {
            FlashManager flashManager = ((RenderContainer) client.player).getFlashManager();
            flashManager.add(new ScreenFlash(maxAge, color, fadeTime, maxOpacity));
        }
    }
}