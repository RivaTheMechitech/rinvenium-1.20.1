package silly.chemthunder.rinvenium.cca.entity.riva;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;
import silly.chemthunder.rinvenium.cca.RinveniumComponents;
import silly.chemthunder.rinvenium.cca.primitive.IntComponent;


public class GlaiveHealComponent implements IntComponent, AutoSyncedComponent, CommonTickingComponent {
    public static final String HIT_COUNT_KEY = "HitCount";
    public static final int TRIGGER_COUNT = 6;
    private int hitCount = 0;

    private final PlayerEntity player;

    public GlaiveHealComponent(PlayerEntity player) {
        this.player = player;
    }

    public static GlaiveHealComponent get(@NotNull PlayerEntity player) {
        return RinveniumComponents.GLAIVE_HEAL.get(player);
    }

    private void sync() {
        RinveniumComponents.GLAIVE_HEAL.sync(this.player);
    }

    @Override
    public int getInt() {
        return this.hitCount;
    }

    @Override
    public void setInt(int value) {
        this.hitCount = value;
        this.sync();
    }

    @Override
    public void addValueToInt(int count) {
        this.hitCount++;
    }

    @Override
    public void incrementInt() {
        this.hitCount++;
        this.sync();
    }

    @Override
    public void decrementInt() {
        this.hitCount--;
        this.sync();
    }

    @Override
    public void readFromNbt(NbtCompound nbtCompound) {
        this.hitCount = nbtCompound.getInt(HIT_COUNT_KEY);
    }

    @Override
    public void writeToNbt(NbtCompound nbtCompound) {
        nbtCompound.putInt(HIT_COUNT_KEY, this.hitCount);
    }

    @Override
    public void tick() {
        if (this.hitCount >= TRIGGER_COUNT) {
            this.player.heal(2);
            this.setInt(0);
        }
    }
}