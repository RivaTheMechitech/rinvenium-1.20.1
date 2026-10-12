package silly.chemthunder.rinvenium.render;

import net.minecraft.util.math.Vec3d;
import silly.chemthunder.rinvenium.item.APMDSCItem;

import java.util.UUID;

public class APMDSCBeamRender {
    public static final int FADE_OUT_DURATION = 10;
    public static final int FADE_IN_DURATION = 5;

    public final UUID uuid;
    public final UUID owner;
    public final Vec3d startPos;
    public final int maxAge;
    public final APMDSCItem.BeamType beamType;
    public int age;
    public boolean stopBeam;

    public APMDSCBeamRender(UUID uuid, UUID owner, Vec3d startPos, int maxAge, APMDSCItem.BeamType beamType) {
        this.uuid = uuid;
        this.owner = owner;
        this.startPos = startPos;
        this.maxAge = maxAge;
        this.beamType = beamType;
        this.age = 0;
        this.stopBeam = false;
    }
}
