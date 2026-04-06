package silly.chemthunder.rinvenium.render.manager.global;

import net.minecraft.client.gl.VertexBuffer;
import org.jetbrains.annotations.Nullable;
import silly.chemthunder.rinvenium.render.SlashRender;

import java.util.ArrayList;
import java.util.List;

public class SlashRendererManager {
    public static final List<SlashRender> SLASH_RENDERS = new ArrayList<>();
    @Nullable public static VertexBuffer slashBuffer;

    public static void add(SlashRender slashRender) {
        SLASH_RENDERS.add(slashRender);
    }

    public static void tick() {
        SLASH_RENDERS.removeIf(slashRender -> slashRender.maxAge != -1 && ++slashRender.age >= slashRender.maxAge);
    }

    public static List<SlashRender> get() {
        return SLASH_RENDERS;
    }

    public static void clear() {
        SLASH_RENDERS.clear();
    }
}