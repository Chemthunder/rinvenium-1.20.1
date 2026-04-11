package silly.chemthunder.rinvenium.render;

import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SlashRender {
    public final Vec3d origin;
    public final Vec3d presetOrigin;
    /** {@link #direction} is not to be modified */
    public final Vec3d direction;
    public final int maxAge;
    public final List<VertexColorSet> vertexColorSets;
    public float size = 1.0f;
    public int age;
    public List<Quaternionf> TRANSFORMATION = new ArrayList<>();

    public SlashRender(Vec3d origin, int maxAge, VertexColorSet... vertexColorSets) {
        this.direction = new Vec3d(0, 10, 0);
        this.presetOrigin = origin;
        this.origin = origin.subtract(direction.normalize().multiply(0.5f));
        this.maxAge = maxAge;
        this.vertexColorSets = List.of(vertexColorSets);
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

    private void updateOrigin() {
        //this.origin = presetOrigin.subtract(direction.normalize().multiply(this.size).multiply(0.5f))       ;
    }
}