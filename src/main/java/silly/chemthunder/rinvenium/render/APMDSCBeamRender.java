package silly.chemthunder.rinvenium.render;

import net.minecraft.util.math.Vec3d;

import java.util.UUID;

public class APMDSCBeamRender {
    public static final int FADE_OUT_DURATION = 10;
    public static final int FADE_IN_DURATION = 5;

    public final UUID uuid;
    public final Vec3d startPos;
    public final Vec3d endPos;
    public final int maxAge;
    public int age;
    public boolean stopBeam;

    public APMDSCBeamRender(UUID uuid, Vec3d startPos, Vec3d endPos, int maxAge) {
        this.uuid = uuid;
        this.startPos = startPos;
        this.endPos = endPos;
        this.maxAge = maxAge;
        this.age = 0;
        this.stopBeam = false;
    }
}
