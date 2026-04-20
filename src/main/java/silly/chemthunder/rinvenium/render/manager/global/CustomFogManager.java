package silly.chemthunder.rinvenium.render.manager.global;

import silly.chemthunder.rinvenium.render.CustomFog;

import java.util.concurrent.ConcurrentLinkedQueue;

public class CustomFogManager {
    private static final ConcurrentLinkedQueue<CustomFog> FOGS = new ConcurrentLinkedQueue<CustomFog>();

    public static void add(CustomFog customFog) {
        FOGS.add(customFog);
    }

    public static void tick() {
        FOGS.removeIf(customFog -> customFog.maxAge != -1 && ++customFog.age >= customFog.maxAge);
    }

    public static ConcurrentLinkedQueue<CustomFog> get() {
        return FOGS;
    }

    public static void clear() {
        FOGS.clear();
    }
}
