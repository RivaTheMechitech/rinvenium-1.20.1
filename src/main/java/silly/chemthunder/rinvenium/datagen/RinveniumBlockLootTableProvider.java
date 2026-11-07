package silly.chemthunder.rinvenium.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.SlabBlock;
import silly.chemthunder.rinvenium.index.RinveniumBlocks;

public class RinveniumBlockLootTableProvider extends FabricBlockLootTableProvider {
    public RinveniumBlockLootTableProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        RinveniumBlocks.BLOCKS.forEach((identifier, block) -> {
            if (block instanceof SlabBlock) {
                this.slabDrops(block);
            } else {
                this.addDrop(block);
            }
        });
    }
}
