package silly.chemthunder.rinvenium.cca.item;

import dev.onyxstudios.cca.api.v3.item.ItemComponent;
import net.minecraft.item.ItemStack;

public class GlaiveTextureItemComponent extends ItemComponent {
    private static final String TEXTURE_KEY = "texture";

    public GlaiveTextureItemComponent(ItemStack stack) {
        super(stack);
    }

    public String getTexture() {
        return this.getString(TEXTURE_KEY);
    }
    
    public void setTexture(String texture) {
        this.putString(TEXTURE_KEY, texture);
    }
}