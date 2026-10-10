package silly.chemthunder.rinvenium.index.client;

import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;
import silly.chemthunder.rinvenium.cca.RinveniumComponents;
import silly.chemthunder.rinvenium.cca.item.GlaiveTextureItemComponent;
import silly.chemthunder.rinvenium.index.RinveniumItems;
import silly.chemthunder.rinvenium.item.EnviniumGlaiveItem;
import silly.chemthunder.rinvenium.item.EnvixiaCoreItem;

public class RinveniumModelPredicateProvider {
    public static void registerModelPredicates() {
        ModelPredicateProviderRegistry.register(
                RinveniumItems.ENVIXIA_CORE,
                new Identifier("activated"),
                (stack, world, entity, seed) -> {
                    if (stack.isOf(RinveniumItems.ENVIXIA_CORE) && stack.getItem() instanceof EnvixiaCoreItem coreItem) {
                        return coreItem.isComplete(stack) ? 1.0f : 0.0f;
                    }
                    return 0.0f;
                }
        );
        ModelPredicateProviderRegistry.register(
                RinveniumItems.ENVINIUM_GLAIVE,
                new Identifier("custom_glaive_texture"),
                (stack, world, entity, seed) -> {
                    if (stack.isOf(RinveniumItems.ENVINIUM_GLAIVE)) {
                        GlaiveTextureItemComponent glaiveTextureItemComponent = RinveniumComponents.GLAIVE_TEXTURE.get(stack);
                        return (float) EnviniumGlaiveItem.Texture.valueOf(glaiveTextureItemComponent.getTexture().toUpperCase()).ordinal() / EnviniumGlaiveItem.Texture.values().length;
                    }
                    return 0.0f;
                }
        );
    }
}