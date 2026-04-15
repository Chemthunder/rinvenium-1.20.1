package silly.chemthunder.rinvenium.render.manager;

import silly.chemthunder.rinvenium.render.ImpactFrame;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ImpactFrameManager {
    private final ConcurrentLinkedQueue<ImpactFrame> FRAMES = new ConcurrentLinkedQueue<ImpactFrame>();

    public void add(ImpactFrame impactFrame) {
        this.FRAMES.add(impactFrame);
    }

    public void tick() {
        this.FRAMES.removeIf(impactFrame -> ++impactFrame.age >= impactFrame.maxAge + impactFrame.fadeTime * 2);
    }

    public ConcurrentLinkedQueue<ImpactFrame> get() {
        return this.FRAMES;
    }

    public void clear() {
        this.FRAMES.clear();
    }
}