package silly.chemthunder.rinvenium.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import silly.chemthunder.rinvenium.util.RinveniumUtil;

import java.util.List;

public class DescriptionItem extends Item {
    private final String item;

    public DescriptionItem(Settings settings, String item) {
        super(settings);
        this.item = item;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        RinveniumUtil.addExpandableTooltip(Text.translatable("item.rinvenium." + item + ".desc").formatted(Formatting.GRAY), Text.translatable("item.rinvenium.expand_toolip"), tooltip, true);
        super.appendTooltip(stack, world, tooltip, context);
    }
} // i like this, this is good, maybe add a . between the "desc" and the number though for formatting's sake
// i'm stealing this