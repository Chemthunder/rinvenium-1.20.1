package silly.chemthunder.rinvenium.render.manager;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import silly.chemthunder.rinvenium.render.ImpactFrame;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ImpactFrameManager {
    private final ConcurrentLinkedQueue<ImpactFrame> FRAMES = new ConcurrentLinkedQueue<ImpactFrame>();

    public void add(ImpactFrame impactFrame) {
        this.FRAMES.add(impactFrame);
    }

    public void tick() {
        this.FRAMES.removeIf(impactFrame -> ++impactFrame.age >= impactFrame.maxAge + impactFrame.fadeTime * 2);
    }

    public ConcurrentLinkedQueue<ImpactFrame> get() {
        return this.FRAMES;
    }

    public void clear() {
        this.FRAMES.clear();
    }

    public boolean shouldShow() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null && client.world != null) {
            if (!FRAMES.isEmpty()) {
                for (Entity entity : client.world.getEntities()) {
                    for (ImpactFrame impactFrame : FRAMES) {
                        if (entity.getUuid().equals(impactFrame.uuid)) {
                            Vec3d vec3d = client.player.getRotationVec(1.0F).normalize();
                            Vec3d vec3d2 = new Vec3d(entity.getX() - client.player.getX(), entity.getEyeY() - client.player.getEyeY(), entity.getZ() - client.player.getZ());
                            double d = vec3d2.length();
                            vec3d2 = vec3d2.normalize();
                            double e = vec3d.dotProduct(vec3d2);
                            return e > 0.1 / d && client.player.canSee(entity);
                        }
                    }
                }
            } else {
                return false;
            }
        }
        return false;
    }
}