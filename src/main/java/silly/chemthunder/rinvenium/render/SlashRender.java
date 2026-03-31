package silly.chemthunder.rinvenium.render;

import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;

import java.util.ArrayList;
import java.util.List;

public class SlashRender {
    public final Vec3d origin;
    public final Vec3d direction;
    public final int maxAge;
    public float size;
    public int age;
    public List<Quaternionf> TRANSFORMATION = new ArrayList<>();

    public SlashRender(Vec3d origin, Vec3d direction, int maxAge) {
        this.origin = origin;
        this.direction = direction;
        this.maxAge = maxAge;
        this.age = 0;
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
