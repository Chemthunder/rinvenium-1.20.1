package silly.chemthunder.rinvenium.util.inject;

import silly.chemthunder.rinvenium.render.manager.global.CustomFogManager;
import silly.chemthunder.rinvenium.render.manager.FlashManager;
import silly.chemthunder.rinvenium.render.manager.ImpactFrameManager;

public interface RenderContainer {
    FlashManager getFlashManager();
    ImpactFrameManager getImpactFrameManager();
}