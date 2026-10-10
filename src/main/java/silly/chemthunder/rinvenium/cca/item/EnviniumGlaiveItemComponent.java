package silly.chemthunder.rinvenium.cca.item;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.item.ItemComponent;
import net.minecraft.item.ItemStack;
import silly.chemthunder.rinvenium.Rinvenium;

public class EnviniumGlaiveItemComponent extends ItemComponent {
    public static final ComponentKey<EnviniumGlaiveItemComponent> KEY = ComponentRegistry.getOrCreate(Rinvenium.id("glaive"), EnviniumGlaiveItemComponent.class);

    private static final String GLAIVE_RUSH = "glaive_rush";
    private static final String FINAL_RUSH = "final_rush";
    private static final String PARRY_WINDOW = "parry_window";
    private static final String DAMAGE_WINDOW = "damage_window";
    private static final String IS_IN_DAMAGE_STATE = "is_in_damage_state";
    
    public EnviniumGlaiveItemComponent(ItemStack stack) {
        super(stack);
    }

    public int getCharge() {
        return this.getInt(GLAIVE_RUSH);
    }

    public void setCharge(int type) {
        this.putInt(GLAIVE_RUSH, type);
    }

    public boolean getFinalRush() {
        return this.getBoolean(FINAL_RUSH);
    }

    public void setFinalRush(boolean type) {
        this.putBoolean(FINAL_RUSH, type);
    }

    public int getParryWindow() {
        return this.getInt(PARRY_WINDOW);
    }

    public void setParryWindow(int type) {
        this.putInt(PARRY_WINDOW, type);
    }

    public int getDamageWindow() {
        return this.getInt(DAMAGE_WINDOW);
    }

    public void setDamageWindow(int type) {
        this.putInt(DAMAGE_WINDOW, type);
    }

    public boolean isInDamageState() {
        return this.getBoolean(IS_IN_DAMAGE_STATE);
    }

    public void setIsInDamageState(boolean value) {
        this.putBoolean(IS_IN_DAMAGE_STATE, value);
    }
}