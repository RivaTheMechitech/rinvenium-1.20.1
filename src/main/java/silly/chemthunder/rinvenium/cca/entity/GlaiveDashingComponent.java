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

    public static final String DAMAGE_STORED_KEY = "DamageStored";

    public static final int MAX_DAMAGE = 99;
    public static final int DAMAGE_PER_NOTCH = 33;
    private int passiveChargeTickDelta = 0;
    public int dashTicks = 0;
    public int damageStored = 0;

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
        if (this.passiveChargeTickDelta >= 20) {
            this.passiveChargeTickDelta = 0;
        } else {
            this.passiveChargeTickDelta++;
        }
        if (this.damageStored > MAX_DAMAGE) {
            this.setInt(MAX_DAMAGE);
        }
        if (this.damageStored < 0) {
            this.setInt(0);
        }

        if (passiveChargeTickDelta % 40 == 0) {
            if (this.damageStored < MAX_DAMAGE) {
                this.incrementInt();
            }
        }

        EnvixiaFormComponent envixiaFormComponent = EnvixiaFormComponent.get(this.player);

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
        return this.damageStored;
    }

    @Override
    public void setInt(int value) {
        this.damageStored = value;
        this.sync();
    }

    @Override
    public void addValueToInt(int count) {
        this.damageStored += count;
        this.sync();
    }

    @Override
    public void incrementInt() {
        this.damageStored++;
        this.sync();
    }

    @Override
    public void decrementInt() {
        this.damageStored--;
        this.sync();
    }

    public float getChargePercent() {
        return (float) this.damageStored / MAX_DAMAGE;
    }

    public float getNotchPercent() {
        return switch (this.notchesAvailable()) {
            case 1 -> (float) (this.damageStored - DAMAGE_PER_NOTCH) / DAMAGE_PER_NOTCH;
            case 2 -> (float) (this.damageStored - DAMAGE_PER_NOTCH * 2) / DAMAGE_PER_NOTCH;
            case 3 -> 1.0f;
            default -> (float) this.damageStored / DAMAGE_PER_NOTCH;
        };
    }

    public int notchesAvailable() {
        return Math.floorDiv(this.damageStored, DAMAGE_PER_NOTCH);
    }

    @Override
    public void readFromNbt(NbtCompound nbtCompound) {
        this.dashTicks = nbtCompound.getInt("dashTicks");
        this.damageStored = nbtCompound.getInt(DAMAGE_STORED_KEY);
    }

    @Override
    public void writeToNbt(NbtCompound nbtCompound) {
        nbtCompound.putInt("dashTicks", dashTicks);
        nbtCompound.putInt(DAMAGE_STORED_KEY, this.damageStored);
    }
}