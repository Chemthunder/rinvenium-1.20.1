package silly.chemthunder.rinvenium.render;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

public class FakePlayerRenderer {
    public final PlayerEntity player;
    public final Vec3d origin;
    public final float pitch;
    public final float yaw;
    public final int maxAge;
    public int age;

    public FakePlayerRenderer(PlayerEntity player, Vec3d origin, float pitch, float yaw, int maxAge) {
        this.player = player;
        this.origin = origin;
        this.pitch = pitch;
        this.yaw = yaw;
        this.maxAge = maxAge;
        this.age = 0;
    }
}
