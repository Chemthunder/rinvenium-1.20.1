package silly.chemthunder.rinvenium.render.manager.client;

import silly.chemthunder.rinvenium.render.FakePlayerRender;

import java.util.concurrent.ConcurrentLinkedQueue;

public class ClientFakePlayerRendererManager {
    public final ConcurrentLinkedQueue<FakePlayerRender> PLAYER_RENDERS = new ConcurrentLinkedQueue<>();

    public void add(FakePlayerRender fakePlayerRender) {
        this.PLAYER_RENDERS.add(fakePlayerRender);
    }

    public void tick() {
        this.PLAYER_RENDERS.removeIf(fakePlayerRender -> fakePlayerRender.maxAge != -1 && ++fakePlayerRender.age >= fakePlayerRender.maxAge);
    }

    public ConcurrentLinkedQueue<FakePlayerRender> get() {
        return this.PLAYER_RENDERS;
    }

    public void clear() {
        this.PLAYER_RENDERS.clear();
    }
}
