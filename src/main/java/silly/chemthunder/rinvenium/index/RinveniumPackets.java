package silly.chemthunder.rinvenium.index;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.apache.logging.log4j.core.jmx.Server;
import silly.chemthunder.rinvenium.Rinvenium;
import silly.chemthunder.rinvenium.network.s2c.*;

public class RinveniumPackets {
    /**C2S Packets*/
    /*public static final Identifier DUMMY = createC2SId("dummy");*/

    public static void registerC2SPackets() {

    }

    /**S2C Packets*/
    public static final Identifier FLASH_PARTICLE = createS2CId("flash_particle");
    public static final Identifier ADD_SCREEN_FLASH = createS2CId("add_screen_flash");
    public static final Identifier ADD_IMPACT_FRAME = createS2CId("add_impact_frame");
    public static final Identifier ADD_CUSTOM_FOG = createS2CId("add_custom_fog");
    public static final Identifier ADD_SLASH = createS2CId("add_slash");
    public static final Identifier ADD_MULTIPLE_SLASHES = createS2CId("add_multiple_slashes");
    public static final Identifier ADD_SINGULAR_SLASH = createS2CId("add_singular_slash");
    public static final Identifier ADD_FAKE_PLAYER = createS2CId("add_fake_player");
    public static final Identifier FAKE_PLAYER_ARM_SWING = createS2CId("fake_player_arm_swing");
    public static final Identifier REMOVE_FAKE_PLAYER = createS2CId("remove_fake_player");

    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(FLASH_PARTICLE, SpawnFlashParticleS2CPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(ADD_SCREEN_FLASH, AddScreenFlashS2CPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(ADD_IMPACT_FRAME, AddImpactFrameS2CPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(ADD_CUSTOM_FOG, AddCustomFogS2CPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(ADD_MULTIPLE_SLASHES, AddMultipleSlashS2CPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(ADD_SINGULAR_SLASH, AddSingularSlashS2CPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(ADD_FAKE_PLAYER, AddFakePlayerS2CPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(FAKE_PLAYER_ARM_SWING, FakePlayerSwingArmS2CPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(REMOVE_FAKE_PLAYER, RemoveFakePlayerS2CPacket::receive);
    }

    public static Identifier createC2SId(String name) {
        return Rinvenium.id(name + "_c2s_packet");
    }
    
    public static Identifier createS2CId(String name) {
        return Rinvenium.id(name + "_s2c_packet");
    }

    public static void init() {
        Rinvenium.LOGGER.info("Registering Rinvenium Packets");
    }

    public static void sendImpactFrame(ServerPlayerEntity player, int time, LivingEntity target) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(time);
        buf.writeInt(0xFFFFFF);
        buf.writeInt(1);
        buf.writeFloat(1.0f);
        buf.writeDouble(player.getX());
        buf.writeDouble(player.getY());
        buf.writeDouble(player.getZ());
        buf.writeUuid(target.getUuid());

        ServerPlayNetworking.send(player, RinveniumPackets.ADD_IMPACT_FRAME, buf);
    }
    public static void sendRedFlash(ServerPlayerEntity player, int time, int fade, float opacity) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(time);
        buf.writeInt(0xFF0000);
        buf.writeInt(fade);
        buf.writeFloat(opacity);

        ServerPlayNetworking.send(player, RinveniumPackets.ADD_SCREEN_FLASH, buf);
    }
    public static void sendFakePlayerArmSwing(ServerPlayerEntity player, String name) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeString(name);
        ServerPlayNetworking.send(player, RinveniumPackets.FAKE_PLAYER_ARM_SWING, buf);
    }
    public static void removeFakePlayer(ServerPlayerEntity player, String name) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeString(name);
        ServerPlayNetworking.send(player, RinveniumPackets.REMOVE_FAKE_PLAYER, buf);
    }
    public static void spawnFakePlayerPosPitchYawAge(ServerPlayerEntity player, String name, Vec3d pos, float pitch, float yaw, int age) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeString(name);
        buf.writeDouble(pos.x);
        buf.writeDouble(pos.y);
        buf.writeDouble(pos.z);
        buf.writeFloat(pitch);
        buf.writeFloat(yaw);
        buf.writeInt(age);

        ServerPlayNetworking.send(player, RinveniumPackets.ADD_FAKE_PLAYER, buf);
    }
}