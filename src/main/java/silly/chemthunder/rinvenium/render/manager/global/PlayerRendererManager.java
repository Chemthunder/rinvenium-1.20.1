package silly.chemthunder.rinvenium.render.manager.global;

import silly.chemthunder.rinvenium.render.FakePlayerRenderer;

import java.util.concurrent.ConcurrentLinkedQueue;

public class PlayerRendererManager {
    public static final ConcurrentLinkedQueue<FakePlayerRenderer> PLAYER_RENDERS = new ConcurrentLinkedQueue<>();

    public static void add(FakePlayerRenderer fakePlayerRenderer) {
        PLAYER_RENDERS.add(fakePlayerRenderer);
    }

    public static void tick() {
        PLAYER_RENDERS.removeIf(fakePlayerRenderer -> fakePlayerRenderer.maxAge != -1 && ++fakePlayerRenderer.age >= fakePlayerRenderer.maxAge);
    }

    public static ConcurrentLinkedQueue<FakePlayerRenderer> get() {
        return PLAYER_RENDERS;
    }

    public static void clear() {
        PLAYER_RENDERS.clear();
    }
}
