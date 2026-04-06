package silly.chemthunder.rinvenium.render.manager;

import silly.chemthunder.rinvenium.render.ScreenFlash;

import java.util.ArrayList;
import java.util.List;

public class FlashManager {
    private final List<ScreenFlash> FLASHES = new ArrayList<>();

    public void add(ScreenFlash flash) {
        this.FLASHES.add(flash);
    }

    public void tick() {
        this.FLASHES.removeIf(screenFlash -> ++screenFlash.age >= screenFlash.maxAge + screenFlash.fadeTime * 2);
    }

    public List<ScreenFlash> get() {
        return this.FLASHES;
    }

    public void clear() {
        this.FLASHES.clear();
    }
}