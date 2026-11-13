package silly.chemthunder.rinvenium.entity.client;

import net.minecraft.client.model.ModelPart;

public class CustomPosing {
    public static void holdAPMSDC(ModelPart holdingArm, ModelPart otherArm, ModelPart head, boolean rightArmed) {
        float holdingYaw = 0.3f;
        float otherYaw = 0.6f;
        ModelPart modelPart = rightArmed ? holdingArm : otherArm;
        ModelPart modelPart2 = rightArmed ? otherArm : holdingArm;
        modelPart.yaw = (rightArmed ? -holdingYaw : holdingYaw) + head.yaw;
        modelPart2.yaw = (rightArmed ? otherYaw : -otherYaw) + head.yaw;
        modelPart.pitch = (float) (-Math.PI / 2) + head.pitch + 0.1F;
        modelPart2.pitch = -1.5F + head.pitch;
    }
}
