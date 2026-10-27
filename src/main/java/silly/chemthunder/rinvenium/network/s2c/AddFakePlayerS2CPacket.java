package silly.chemthunder.rinvenium.network.s2c;

import com.mojang.authlib.GameProfile;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.Vec3d;
import silly.chemthunder.rinvenium.render.FakePlayerRender;
import silly.chemthunder.rinvenium.render.manager.client.FakePlayerRendererManager;
import silly.chemthunder.rinvenium.util.inject.client.RenderContainer;

import java.util.UUID;

public class AddFakePlayerS2CPacket {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        if (client.world != null && client.world.getEntities() != null) {
            if (client.player != null) {
                FakePlayerRendererManager fakePlayerRendererManager = ((RenderContainer) client.player).getFakePlayerRendererManager();
                String name = buf.readString();
                Vec3d spawnPos = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
                float pitch = buf.readFloat();
                float yaw = buf.readFloat();
                int age = buf.readInt();
                fakePlayerRendererManager.add(new FakePlayerRender(new GameProfile(UUID.randomUUID(), name), spawnPos, pitch, yaw, age, name));
            }
        }
    }
}
