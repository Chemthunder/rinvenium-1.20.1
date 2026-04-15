package silly.chemthunder.rinvenium.render.manager;

import silly.chemthunder.rinvenium.render.ScreenFlash;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class FlashManager {
    private final ConcurrentLinkedQueue<ScreenFlash> FLASHES = new ConcurrentLinkedQueue<ScreenFlash>();

    public void add(ScreenFlash flash) {
        this.FLASHES.add(flash);
    }

    public void tick() {
        this.FLASHES.removeIf(screenFlash -> ++screenFlash.age >= screenFlash.maxAge + screenFlash.fadeTime * 2);
    }

    public ConcurrentLinkedQueue<ScreenFlash> get() {
        return this.FLASHES;
    }

    public void clear() {
        this.FLASHES.clear();
    }
}