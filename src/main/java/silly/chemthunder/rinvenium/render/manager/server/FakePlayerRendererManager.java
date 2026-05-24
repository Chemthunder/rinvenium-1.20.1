package silly.chemthunder.rinvenium.render.manager.server;

import silly.chemthunder.rinvenium.render.FakePlayerRender;

import java.util.concurrent.ConcurrentLinkedQueue;

public class FakePlayerRendererManager {
    public final ConcurrentLinkedQueue<FakePlayerRender> PLAYER_RENDERS = new ConcurrentLinkedQueue<>();

    public void add(FakePlayerRender fakePlayerRender) {
        PLAYER_RENDERS.add(fakePlayerRender);
    }

    public void tick() {
        PLAYER_RENDERS.removeIf(fakePlayerRender -> fakePlayerRender.maxAge != -1 && ++fakePlayerRender.age >= fakePlayerRender.maxAge);
    }

    public ConcurrentLinkedQueue<FakePlayerRender> get() {
        return PLAYER_RENDERS;
    }

    public void clear() {
        PLAYER_RENDERS.clear();
    }

    public void remove(String name) {
        PLAYER_RENDERS.removeIf(fakePlayerRender -> fakePlayerRender.gameProfile.getName().equals(name));
    }

    public FakePlayerRender getPlayer(String name) {
        for (FakePlayerRender fakePlayerRender : PLAYER_RENDERS) {
            if (fakePlayerRender.gameProfile.getName().equals(name)) {
                return fakePlayerRender;
            }
        }
        return null;
    }
}
