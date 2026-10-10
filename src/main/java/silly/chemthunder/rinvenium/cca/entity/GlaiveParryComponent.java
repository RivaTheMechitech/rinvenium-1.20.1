package silly.chemthunder.rinvenium.cca.entity;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;
import silly.chemthunder.rinvenium.cca.RinveniumComponents;
import silly.chemthunder.rinvenium.cca.primitive.DoubleBoolComponent;
import silly.chemthunder.rinvenium.cca.primitive.DoubleIntComponent;
import silly.chemthunder.rinvenium.index.RinveniumItems;

public class GlaiveParryComponent implements DoubleIntComponent, DoubleBoolComponent, AutoSyncedComponent, CommonTickingComponent {
    public static final String PARRY_WINDOW_KEY = "ParryWindow";
    public static final String DAMAGE_WINDOW_KEY = "DamageWindow";
    public static final String CAN_PARRY_KEY = "CanParryKey";
    public static final String IS_BLOCKING_KEY = "IsBlocking";

    public static final int MAX_PARRY_WINDOW = 10;
    public static final int MAX_DAMAGE_WINDOW = 100;
    private int parryWindow = MAX_PARRY_WINDOW;
    private int damageWindow = 0;
    
    private final PlayerEntity player;

    private boolean canParry = false;
    private boolean isBlocking = false;

    public GlaiveParryComponent(@NotNull PlayerEntity player) {
        this.player = player;
    }

    public static GlaiveParryComponent get(@NotNull PlayerEntity player) {
        return RinveniumComponents.GLAIVE_PARRY.get(player);
    }

    private void sync() {
        RinveniumComponents.GLAIVE_PARRY.sync(player);
    }

    /**Returns {@link #parryWindow}.*/
    public int getDoubleIntValue1() {
        return this.parryWindow;
    }

    /**Returns {@link #damageWindow}.*/
    public int getDoubleIntValue2() {
        return this.damageWindow;
    }

    public void setDoubleIntValue1(int value) {
        this.parryWindow = value;
        this.sync();
    }

    public void setDoubleIntValue2(int value) {
        this.damageWindow = value;
        this.sync();
    }

    public void addToDoubleIntValue1(int count) {
        this.parryWindow += count;
        this.sync();
    }

    public void addToDoubleIntValue2(int count) {
        this.damageWindow += count;
        this.sync();
    }

    public void incrementDoubleIntValue1() {
        this.parryWindow++;
        this.sync();
    }

    public void incrementDoubleIntValue2() {
        this.damageWindow++;
        this.sync();
    }

    public void decrementDoubleIntValue1() {
        this.parryWindow--;
        this.sync();
    }

    public void decrementDoubleIntValue2() {
        this.damageWindow--;
        this.sync();
    }

    /**Returns {@link #canParry}.*/
    public boolean getDoubleBoolValue1() {
        if (this.parryWindow > 0 && this.damageWindow <= 0) {
            this.setDoubleBoolValue1(true);
        }
        if (!this.player.getWorld().isClient && (!this.player.getMainHandStack().isOf(RinveniumItems.ENVINIUM_GLAIVE) || this.parryWindow <= 0 || this.damageWindow > 0)) {
            this.setDoubleBoolValue1(false);
        }
        return this.canParry;
    }

    /**Returns {@link #isBlocking}.*/
    public boolean getDoubleBoolValue2() {
        if (!this.player.getWorld().isClient && (!this.player.getMainHandStack().isOf(RinveniumItems.ENVINIUM_GLAIVE) || !this.player.isUsingItem())) {
            this.setDoubleBoolValue2(false);
        }
        return this.isBlocking;
    }

    public void setDoubleBoolValue1(boolean value) {
        this.canParry = value;
        this.sync();
    }

    public void setDoubleBoolValue2(boolean value) {
        this.isBlocking = value;
        this.sync();
    }

    public void tick() {
        if (this.parryWindow < MAX_PARRY_WINDOW && !this.isBlocking) {
            EnvixiaFormComponent envixiaFormComponent = EnvixiaFormComponent.get(this.player);
            if (envixiaFormComponent.getTripleBoolValue1()) {
                this.addToDoubleIntValue1(2);
            } else {
                this.incrementDoubleIntValue1();
            }
        } else if (this.parryWindow > MAX_PARRY_WINDOW) {
            this.setDoubleIntValue1(MAX_PARRY_WINDOW);
        }
        if (this.damageWindow > 0) {
            this.decrementDoubleIntValue2();
        } else if (this.damageWindow < 0) {
            this.setDoubleIntValue2(0);
        }
    }

    // nbt ====
    public void readFromNbt(NbtCompound nbtCompound) {
        this.parryWindow = nbtCompound.getInt(PARRY_WINDOW_KEY);
        this.damageWindow = nbtCompound.getInt(DAMAGE_WINDOW_KEY);

        this.canParry = nbtCompound.getBoolean(CAN_PARRY_KEY);
        this.isBlocking = nbtCompound.getBoolean(IS_BLOCKING_KEY);
    }

    public void writeToNbt(NbtCompound nbtCompound) {
        nbtCompound.putInt(PARRY_WINDOW_KEY, this.parryWindow);
        nbtCompound.putInt(DAMAGE_WINDOW_KEY, this.damageWindow);
        
        nbtCompound.putBoolean(CAN_PARRY_KEY, this.canParry);
        nbtCompound.putBoolean(IS_BLOCKING_KEY, this.isBlocking);
    }

    public float getParryWindowPercentage() {
        return (float) this.parryWindow / MAX_PARRY_WINDOW;
    }
    
    public float getDamageWindowPercentage() {
        return (float) this.damageWindow / MAX_DAMAGE_WINDOW;
    }
}