package silly.chemthunder.rinvenium.render.manager.client;

import net.minecraft.client.gl.VertexBuffer;
import org.jetbrains.annotations.Nullable;
import silly.chemthunder.rinvenium.Rinvenium;
import silly.chemthunder.rinvenium.render.SlashRender;

import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SlashRendererManager {
    public final ConcurrentLinkedQueue<SlashRender> SLASH_RENDERS = new ConcurrentLinkedQueue<>();
    public final ConcurrentLinkedQueue<SlashRender> SLASH_RENDERS_BUFFER = new ConcurrentLinkedQueue<>();
    @Nullable public static VertexBuffer slashBuffer;

    public void add(SlashRender slashRender) {
        SLASH_RENDERS.add(slashRender);
    }

    public void tick() {
        SLASH_RENDERS.forEach(slashRender -> {
            if (slashRender.ageDelta > 0) {
                slashRender.ageDelta--;
                slashRender.age = 0;
            } else {
                slashRender.age++;
            }
            if (slashRender.maxAge != -1 && slashRender.age >= slashRender.maxAge) {
                SLASH_RENDERS.remove(slashRender);
            }
        });
    }

    public ConcurrentLinkedQueue<SlashRender> get() {
        return SLASH_RENDERS;
    }

    public void clear() {
        SLASH_RENDERS.clear();
    }
}