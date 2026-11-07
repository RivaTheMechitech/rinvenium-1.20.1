package silly.chemthunder.rinvenium.index;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import silly.chemthunder.rinvenium.Rinvenium;

public class RinveniumBlocks {
    public static final Block AURIO_BLOCK = registerBlock("aurio_block",
            new Block(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).sounds(BlockSoundGroup.NETHERITE)));

    public static final Block ENVINIA_BLOCK = registerBlock("envinia_block",
            new Block(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).sounds(BlockSoundGroup.NETHERITE)));

    public static final Block ENVIXIUS_BLOCK = registerBlock("envixius_block",
            new Block(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).sounds(BlockSoundGroup.NETHERITE)));


    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, new Identifier(Rinvenium.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block) {
        return Registry.register(Registries.ITEM, new Identifier(Rinvenium.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings()));
    }
    public static void registerRinveniumBlocks() {
        Rinvenium.LOGGER.info("Registering ModBlocks for " + Rinvenium.MOD_ID);
    }
}
