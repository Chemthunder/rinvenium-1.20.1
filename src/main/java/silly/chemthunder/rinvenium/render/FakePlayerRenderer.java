package silly.chemthunder.rinvenium.render;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Vec3d;

public class FakePlayerRenderer {
    public final GameProfile gameProfile;
    public final Vec3d origin;
    public final int maxAge;
    public final String skinTexture;
    public int age;
    public OtherClientPlayerEntity fakePlayer;
    public ClientWorld world;
    public float pitch;
    public float yaw;

    public FakePlayerRenderer(GameProfile gameProfile, Vec3d origin, float pitch, float yaw, int maxAge, String skinTexture) {
        this.gameProfile = gameProfile;
        this.origin = origin;
        this.pitch = pitch;
        this.yaw = yaw;
        this.age = 0;
        this.maxAge = maxAge;
        this.skinTexture = skinTexture;
    }
}
