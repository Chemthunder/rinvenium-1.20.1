package silly.chemthunder.rinvenium.util;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class RinveniumUtil {
    /**Equation: -0.0002x^2 + 5*/
    public static double calculateDivergenceDropOff(double input) {
        return MathHelper.clamp(-0.0002 * (input * input) + 5.0, 0.0, 5.0);
    }

    /**Returns it in degrees*/
    public static float pitchFromVecDeg(Vec3d rotVec) {
        return (float) (-Math.toDegrees(Math.atan2(rotVec.y, Math.sqrt(rotVec.x * rotVec.x + rotVec.z * rotVec.z))));
    }
    /**Returns it in radians*/
    public static float pitchFromVecRad(Vec3d rotVec) {
        return (float) (-Math.atan2(rotVec.y, Math.sqrt(rotVec.x * rotVec.x + rotVec.z * rotVec.z)));
    }

    /**Returns it in degrees*/
    public static float yawFromVecDeg(Vec3d rotVec) {
        return (float) (Math.toDegrees(Math.atan2(rotVec.z, rotVec.x)) - 90.0F);
    }
    /**Returns it in radians*/
    public static float yawFromVecRad(Vec3d rotVec) {
        return (float) (Math.atan2(rotVec.z, rotVec.x) - 90.0F);
    }
}
