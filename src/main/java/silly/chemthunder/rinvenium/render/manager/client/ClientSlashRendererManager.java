package silly.chemthunder.rinvenium.render.manager.client;

import silly.chemthunder.rinvenium.render.SlashRender;

import java.util.concurrent.ConcurrentLinkedQueue;

public class ClientSlashRendererManager {
    public final ConcurrentLinkedQueue<SlashRender> SLASH_RENDERS = new ConcurrentLinkedQueue<>();

    public void add(SlashRender slashRender) {
        this.SLASH_RENDERS.add(slashRender);
    }

    public void tick() {
        this.SLASH_RENDERS.removeIf(slashRender -> slashRender.maxAge != -1 && ++slashRender.age >= slashRender.maxAge);
    }

    public ConcurrentLinkedQueue<SlashRender> get() {
        return this.SLASH_RENDERS;
    }

    public void clear() {
        this.SLASH_RENDERS.clear();
    }
}
