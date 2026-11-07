package silly.chemthunder.rinvenium.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import silly.chemthunder.rinvenium.Rinvenium;
import silly.chemthunder.rinvenium.index.RinveniumBlocks;

import java.util.concurrent.CompletableFuture;

import static net.minecraft.registry.tag.BlockTags.*;

public class RinveniumBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public static final TagKey<Block> SOUL_FIRE = TagKey.of(RegistryKeys.BLOCK, Rinvenium.id("soul_fire"));

    public RinveniumBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        this.getOrCreateTagBuilder(SOUL_FIRE)
                .add(
                        Blocks.SOUL_FIRE,
                        Blocks.SOUL_CAMPFIRE
                );
        RinveniumBlocks.BLOCKS.forEach((identifier, block) -> {
            this.getOrCreateTagBuilder(PICKAXE_MINEABLE)
                    .add(block);
        });
    }
}