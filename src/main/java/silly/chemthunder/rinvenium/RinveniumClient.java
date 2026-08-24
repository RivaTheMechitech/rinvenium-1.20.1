package silly.chemthunder.rinvenium;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.util.Identifier;
import silly.chemthunder.rinvenium.entity.client.EnvixiaArmorModel;
import silly.chemthunder.rinvenium.entity.client.EnvixiaArmorRenderer;
import silly.chemthunder.rinvenium.event.client.WorldRendererListener;
import silly.chemthunder.rinvenium.index.RinveniumEntities;
import silly.chemthunder.rinvenium.index.RinveniumItems;
import silly.chemthunder.rinvenium.index.RinveniumPackets;
import silly.chemthunder.rinvenium.index.RinveniumParticles;
import silly.chemthunder.rinvenium.index.client.RinveniumModelPredicateProvider;
import silly.chemthunder.rinvenium.render.EnviniumSpearItemRenderer;

public class RinveniumClient implements ClientModInitializer {
    public void onInitializeClient() {
        RinveniumEntities.clientInit();
        RinveniumPackets.registerS2CPackets();
        RinveniumParticles.clientInit();
        RinveniumModelPredicateProvider.registerModelPredicates();
        WorldRendererListener.execute();

        EntityModelLayerRegistry.registerModelLayer(EnvixiaArmorRenderer.ENVIXIA_ARMOR, EnvixiaArmorModel::getTexturedModelData);
        ArmorRenderer.register(new EnvixiaArmorRenderer(), RinveniumItems.ENVIXIA_HELMET, RinveniumItems.ENVIXIA_CHESTPLATE, RinveniumItems.ENVIXIA_LEGGINGS, RinveniumItems.ENVIXIA_BOOTS);
        Rinvenium.LOGGER.info("Registering spear renderer");
        EnviniumSpearItemRenderer enviniumSpearItemRenderer = new EnviniumSpearItemRenderer(new Identifier(Rinvenium.MOD_ID, "envinium_spear"));
        BuiltinItemRendererRegistry.INSTANCE.register(RinveniumItems.ENVINIUM_SPEAR, enviniumSpearItemRenderer);
        Rinvenium.LOGGER.info("Spear renderer registered");
    }
}