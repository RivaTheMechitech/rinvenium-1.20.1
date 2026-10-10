package silly.chemthunder.rinvenium.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import silly.chemthunder.rinvenium.Rinvenium;
import silly.chemthunder.rinvenium.index.RinveniumItems;

import java.util.concurrent.CompletableFuture;

public class RinveniumItemTagProvider extends FabricTagProvider.ItemTagProvider {
    public static TagKey<Item> BIG_ITEM_RENDERING = TagKey.of(RegistryKeys.ITEM, Rinvenium.id("big_item_renderer"));
    public static TagKey<Item> ENVIXIA_CORE_INGREDIENTS = TagKey.of(RegistryKeys.ITEM, Rinvenium.id("envixia_core_ingredients"));
    public static TagKey<Item> LOCKED_RECIPES = TagKey.of(RegistryKeys.ITEM, Rinvenium.id("locked_recipes"));
    public static TagKey<Item> ENVIXIA_MUNCHIES = TagKey.of(RegistryKeys.ITEM, Rinvenium.id("envixia_munchies"));
    public static TagKey<Item> TRIGGERS_RESKIN_SCREEN = TagKey.of(RegistryKeys.ITEM, Rinvenium.id("triggers_reskin_screen"));

    public RinveniumItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        this.getOrCreateTagBuilder(BIG_ITEM_RENDERING)
                .add(
                        RinveniumItems.ENVINIUM_GLAIVE
                );
        this.getOrCreateTagBuilder(ENVIXIA_CORE_INGREDIENTS)
                .add(
                        RinveniumItems.ION_CELL,
                        RinveniumItems.ENVIXIUS_PLATE,
                        Items.BEACON
                );
        this.getOrCreateTagBuilder(LOCKED_RECIPES)
                .add(
                        RinveniumItems.ENVIXIA_CORE,
                        RinveniumItems.ENVIXIA_HELMET,
                        RinveniumItems.ENVIXIA_CHESTPLATE,
                        RinveniumItems.ENVIXIA_LEGGINGS,
                        RinveniumItems.ENVIXIA_BOOTS,
                        RinveniumItems.ENVINIUM_GLAIVE
                );
        this.getOrCreateTagBuilder(ENVIXIA_MUNCHIES)
                .add(
                        RinveniumItems.BATTERY,
                        RinveniumItems.ION_CELL
                );
        this.getOrCreateTagBuilder(TRIGGERS_RESKIN_SCREEN)
                .add(
                        RinveniumItems.ENVINIUM_GLAIVE
                );
    }
}