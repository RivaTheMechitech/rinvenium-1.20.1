package silly.chemthunder.rinvenium.cca.entity;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;
import silly.chemthunder.rinvenium.cca.RinveniumComponents;
import silly.chemthunder.rinvenium.cca.primitive.BoolComponent;
import silly.chemthunder.rinvenium.cca.primitive.IntComponent;
import silly.chemthunder.rinvenium.index.RinveniumItems;

import java.sql.Time;

public class APMDSCComponent implements IntComponent, BoolComponent, AutoSyncedComponent, CommonTickingComponent {
    public static final String USE_TIME_KEY = "UseTime";
    public static final String IS_USING_KEY = "IsUsing";
    public static final int MAX_USE_TIME = 20; // Max use time before being set on fire

    private int useTime = 0;
    private boolean isUsing = false;
    private final PlayerEntity player;

    public APMDSCComponent(PlayerEntity player) {
        this.player = player;
    }

    public static APMDSCComponent get(@NotNull PlayerEntity player) {
        return RinveniumComponents.APMDSC_ENTITY.get(player);
    }
    public void sync() {
        RinveniumComponents.APMDSC_ENTITY.sync(this.player);
    }

    @Override
    public int getInt() {
        return this.useTime;
    }

    @Override
    public void setInt(int value) {
        this.useTime = value;
        this.sync();
    }

    @Override
    public void incrementInt() {
        this.useTime++;
        this.sync();
    }

    @Override
    public void decrementInt() {
        this.useTime--;
        this.sync();
    }

    @Override
    public void addValueToInt(int count) {
        this.useTime += count;
        this.sync();
    }

    @Override
    public boolean getBool() {
        if (!this.player.getWorld().isClient && !this.player.getMainHandStack().isOf(RinveniumItems.APMDSC) || this.player.getItemCooldownManager().isCoolingDown(RinveniumItems.APMDSC)) {
            return false;
        }
        return this.isUsing;
    }

    @Override
    public void setBool(boolean value) {
        this.isUsing = value;
        this.sync();
    }

    @Override
    public void tick() {
        if (!this.isUsing) {
            this.decrementInt();
            if (this.useTime < 0) {
                this.setInt(0);
            }
        }
    }

    @Override
    public void readFromNbt(NbtCompound nbtCompound) {
        this.useTime = nbtCompound.getInt(USE_TIME_KEY);
        this.isUsing = nbtCompound.getBoolean(IS_USING_KEY);
    }

    @Override
    public void writeToNbt(NbtCompound nbtCompound) {
        nbtCompound.putInt(USE_TIME_KEY, useTime);
        nbtCompound.putBoolean(IS_USING_KEY, isUsing);
    }
}
