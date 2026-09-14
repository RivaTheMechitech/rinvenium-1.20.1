package silly.chemthunder.rinvenium.cca.item;

import dev.onyxstudios.cca.api.v3.item.ItemComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

public class APMDSCItemComponent extends ItemComponent {
    private static final String ION_CELL_COUNT = "ion_cell_count";
    public static final int MAX_ION_CELL_COUNT = 10;

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
        if (getIonCellCount() >= MAX_ION_CELL_COUNT || getIonCellCount() <= 0) return;
        if (getIonCellCount() + count > MAX_ION_CELL_COUNT || getIonCellCount() + count < 0) {
            count = MathHelper.clamp(getIonCellCount() + count, 0, MAX_ION_CELL_COUNT);
        }
        setIonCellCount(count);
    }

}
