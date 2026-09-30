package silly.chemthunder.rinvenium.util.inject;

import silly.chemthunder.rinvenium.render.manager.client.*;
import silly.chemthunder.rinvenium.render.manager.client.FakePlayerRendererManager;

public interface RenderContainer {
    FlashManager getFlashManager();
    ImpactFrameManager getImpactFrameManager();
    SlashRendererManager getSlashRendererManager();
    FakePlayerRendererManager getFakePlayerRendererManager();
    APMDSCBeamManager getAPMDSCBeamManager();
}