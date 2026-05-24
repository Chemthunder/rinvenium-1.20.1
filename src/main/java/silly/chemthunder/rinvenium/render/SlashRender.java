package silly.chemthunder.rinvenium.render;

import net.minecraft.network.PacketByteBuf;
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
    public int ageDelta = 0;
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

    public SlashRender(Vec3d origin, int maxAge) {
        this(
                origin,
                maxAge,
                new VertexColorSet(1.0f, 0.0f, 0.0f, 0.9f),
                new VertexColorSet(0.4f, 0.0f, 0.0f, 0.9f),
                new VertexColorSet(1.0f, 0.0f, 0.0f, 0.9f),
                new VertexColorSet(1.0f, 0.9f, 0.9f, 1.0f)
        );
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

    public void setAgeDelta(int ageDelta) {
        this.ageDelta = ageDelta;
    }

    public static PacketByteBuf writeMultiple(PacketByteBuf buf, Vec3d origin, float size, int maxAge, boolean randomTransformations, int numberOfSlashes, int... ageDelta) {
        if (numberOfSlashes < ageDelta.length) {
            buf.writeInt(0);
            buf.writeIntArray(new int[0]);
            buf.writeIntArray(new int[0]);
            buf.writeDouble(0);
            buf.writeDouble(0);
            buf.writeDouble(0);
            buf.writeFloat(0f);
            buf.writeInt(0);
            buf.writeBoolean(false);
            return buf;
        } else {
            buf.writeInt(numberOfSlashes);
            int[] groupIndex = new int[numberOfSlashes];
            int[] ageDeltaArray = new int[numberOfSlashes];
            for (int i = 0; i < numberOfSlashes; i++) {
                groupIndex[i] = i;
                ageDeltaArray[i] = ageDelta[i];
            }
            buf.writeIntArray(groupIndex);
            buf.writeIntArray(ageDeltaArray);
            buf.writeDouble(origin.x);
            buf.writeDouble(origin.y);
            buf.writeDouble(origin.z);
            buf.writeFloat(size);
            buf.writeInt(maxAge);
            buf.writeBoolean(randomTransformations);
        }
        return buf;
    }
    public static PacketByteBuf writeSingular(PacketByteBuf buf, Vec3d origin, float size, int maxAge, boolean randomTransformations) {
        buf.writeDouble(origin.x);
        buf.writeDouble(origin.y);
        buf.writeDouble(origin.z);
        buf.writeFloat(size);
        buf.writeInt(maxAge);
        buf.writeBoolean(randomTransformations);
        return buf;
    }

}