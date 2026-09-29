package silly.chemthunder.rinvenium.cca.item;

import dev.onyxstudios.cca.api.v3.item.ItemComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

public class APMDSCItemComponent extends ItemComponent {
    private static final String ION_CELL_COUNT = "ion_cell_count";
    private static final String IS_CHARGING = "is_charging";
    private static final String STARSTRUCK_COUNT = "starstruck_count";
    private static final String LOW_POWER_COUNT = "low_power_count";

    public static final int MAX_ION_CELL_COUNT = 10;
    public static final int MAX_STARSTRUCK_COUNT = 2; // 3 shots per ion cell as the count starts from index of 0
    public static final int MAX_LOW_POWER_COUNT = 1; // 2 shots per ion cell as the count starts from index of 0

    public APMDSCItemComponent(ItemStack stack) {
        super(stack);
    }

    public int getIonCellCount() {
        return this.getInt(ION_CELL_COUNT);
    }
    public void setIonCellCount(int count) {
        this.putInt(ION_CELL_COUNT, count);
    }
    public void addIonCellCount(int count) {
        if (getIonCellCount() > MAX_ION_CELL_COUNT) return;
        count = MathHelper.clamp(getIonCellCount() + count, 0, MAX_ION_CELL_COUNT);
        setIonCellCount(count);
    }

    public int getStarstruckCount() {
        return this.getInt(STARSTRUCK_COUNT);
    }
    public void setStarstruckCount(int count) {
        this.putInt(STARSTRUCK_COUNT, count);
    }
    public void addStarstruckCount(int count) {
        if (this.getStarstruckCount() < 0) this.setStarstruckCount(0);
        this.setStarstruckCount(this.getStarstruckCount() + count);
    }

    public int getLowPowerCount() {
        return this.getInt(LOW_POWER_COUNT);
    }
    public void setLowPowerCount(int count) {
        this.putInt(LOW_POWER_COUNT, count);
    }
    public void addLowPowerCount(int count) {
        if (this.getLowPowerCount() < 0) this.setLowPowerCount(0);
        this.setLowPowerCount(this.getLowPowerCount() + count);
    }

    public boolean isCharging() {
        return this.getBoolean(IS_CHARGING);
    }
    public void setIsCharging(boolean isCharging) {
        this.putBoolean(IS_CHARGING, isCharging);
    }
}
