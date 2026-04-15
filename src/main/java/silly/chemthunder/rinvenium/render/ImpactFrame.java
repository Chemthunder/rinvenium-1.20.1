package silly.chemthunder.rinvenium.render;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;
import java.util.ArrayList;
import java.util.List;

public class ImpactFrame {
    public final Entity entity;

    public final Vec3d origin;
    public final Vec3d presetOrigin;
    /** {@link #direction} is not to be modified */
    public final Vec3d direction;
    public final int maxAge;
    public float size = 1.0f;
    public int age;
    public List<Quaternionf> TRANSFORMATION = new ArrayList<>();

    public final int color;
    public final int fadeTime;
    public final float maxOpacity;
    public int fadeTicks;

    /** @param color The color of the impact frame screen flash
     *  @param maxOpacity The opacity of the impact frame screen flash
     * */
    public ImpactFrame(Entity entity, Vec3d origin, int maxAge, int color, int fadeTime, float maxOpacity) {
        this.entity = entity;
        this.direction = new Vec3d(0, 10, 0);
        this.presetOrigin = origin;
        this.origin = origin.subtract(direction.normalize().multiply(0.5f));
        this.maxAge = maxAge;
        this.color = color;
        this.fadeTime = fadeTime;
        this.maxOpacity = maxOpacity;
        this.age = 0;
        this.fadeTicks = 0;
    }

    public void addTransformation(Quaternionf quaternionf) {
        TRANSFORMATION.add(quaternionf);
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }
}
