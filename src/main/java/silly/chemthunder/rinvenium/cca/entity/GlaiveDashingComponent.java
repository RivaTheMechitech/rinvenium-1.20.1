package silly.chemthunder.rinvenium.cca.entity;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import silly.chemthunder.rinvenium.Rinvenium;
import silly.chemthunder.rinvenium.cca.primitive.IntComponent;

public class GlaiveDashingComponent implements IntComponent, AutoSyncedComponent, CommonTickingComponent {
    public static final ComponentKey<GlaiveDashingComponent> KEY = ComponentRegistry.getOrCreate(Rinvenium.id("dash"), GlaiveDashingComponent.class);

    public static final String DASH_COOLDOWN_KEY = "DashCooldown";

    public static final int MAX_COOLDOWN = 200;
    public int dashTicks = 0;
    public int dashCooldown = 0;

    private final PlayerEntity player;

    public GlaiveDashingComponent(PlayerEntity player) {
        this.player = player;
    }

    public void sync() {
        KEY.sync(this.player);
    }

    public static GlaiveDashingComponent get(@NotNull PlayerEntity player) {
        return KEY.get(player);
    }

    public void tick() {
        if (this.dashCooldown < MAX_COOLDOWN) {
            EnvixiaFormComponent envixiaFormComponent = EnvixiaFormComponent.get(this.player);
            if (envixiaFormComponent.getTripleBoolValue1()) {
                this.addValueToInt(2);
            } else {
                this.incrementInt();
            }
        } else if (this.dashCooldown > MAX_COOLDOWN) {
            this.setInt(MAX_COOLDOWN);
        }
        if (dashTicks > 0) {
            dashTicks--;
            spawnParticles(player, player.getWorld());
            if (dashTicks == 0) {
                sync();
            }
        }
    }

    private void spawnParticles(PlayerEntity player, World world) {
        double x = player.getX();
        double y = player.getY();
        double z = player.getZ();

        if (world instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(ParticleTypes.ELECTRIC_SPARK,
                    x,
                    y + 0.1f,
                    z,
                    2,
                    0.08,
                    0.08,
                    0.08,
                    0.09
            );

            serverWorld.spawnParticles(ParticleTypes.END_ROD,
                    x,
                    y + 0.1f,
                    z,
                    2,
                    0.08,
                    0.08,
                    0.08,
                    0.09
            );
        }
    }

    @Override
    public int getInt() {
        return this.dashCooldown;
    }

    @Override
    public void setInt(int value) {
        this.dashCooldown = value;
        this.sync();
    }

    @Override
    public void addValueToInt(int count) {
        this.dashCooldown += count;
        this.sync();
    }

    @Override
    public void incrementInt() {
        this.dashCooldown++;
        this.sync();
    }

    @Override
    public void decrementInt() {
        this.dashCooldown--;
        this.sync();
    }

    public float getChargePercent() {
        return (float) this.dashCooldown / MAX_COOLDOWN;
    }

    @Override
    public void readFromNbt(NbtCompound nbtCompound) {
        this.dashTicks = nbtCompound.getInt("dashTicks");
        this.dashCooldown = nbtCompound.getInt(DASH_COOLDOWN_KEY);
    }

    @Override
    public void writeToNbt(NbtCompound nbtCompound) {
        nbtCompound.putInt("dashTicks", dashTicks);
        nbtCompound.putInt(DASH_COOLDOWN_KEY, this.dashCooldown);
    }
}