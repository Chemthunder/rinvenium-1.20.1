package silly.chemthunder.rinvenium.render.manager.client;

import net.minecraft.client.gl.VertexBuffer;
import org.jetbrains.annotations.Nullable;
import silly.chemthunder.rinvenium.render.SlashRender;

import java.util.concurrent.ConcurrentLinkedQueue;

public class DeathSequenceSlashManager {
    public static final ConcurrentLinkedQueue<SlashRender> SLASH_RENDERS = new ConcurrentLinkedQueue<>();
    @Nullable
    public static VertexBuffer slashBuffer;

    public static void add(SlashRender slashRender) {
        SLASH_RENDERS.add(slashRender);
    }

    public static void tick() {
        SLASH_RENDERS.removeIf(slashRender -> slashRender.maxAge != -1 && ++slashRender.age >= slashRender.maxAge);
    }

    public static ConcurrentLinkedQueue<SlashRender> get() {
        return SLASH_RENDERS;
    }

    public static void clear() {
        SLASH_RENDERS.clear();
    }
}
