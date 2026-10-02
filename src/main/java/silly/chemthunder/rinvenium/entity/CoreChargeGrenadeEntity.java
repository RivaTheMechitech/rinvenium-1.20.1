package silly.chemthunder.rinvenium.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import silly.chemthunder.rinvenium.index.RinveniumItems;

public class CoreChargeGrenadeEntity extends ThrownItemEntity {
    private int discardTicks = 0;

    public CoreChargeGrenadeEntity(EntityType<? extends CoreChargeGrenadeEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected Item getDefaultItem() {
        return RinveniumItems.CORE_CHARGE_GRENADE;
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (!this.getWorld().isClient) {
            this.getWorld().sendEntityStatus(this, EntityStatuses.PLAY_DEATH_SOUND_OR_ADD_PROJECTILE_HIT_PARTICLES);
            if (discardTicks <= 200) {
                discardTicks += 1;
            } else {
                this.discard();
            }
        }
    }

    @Override
    protected float getGravity() {
        return this.isOnGround() ? 0 : super.getGravity();
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        Vec3d vec3d = blockHitResult.getPos().subtract(this.getX(), this.getY(), this.getZ());
        this.setVelocity(vec3d);
        Vec3d vec3d2 = vec3d.normalize().multiply(0.05F);
        this.setPos(this.getX() - vec3d2.x, this.getY() - vec3d2.y, this.getZ() - vec3d2.z);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        Entity entity = entityHitResult.getEntity();
        Vec3d rotation = entity.getRotationVector();
        Vec3d ccgrotation = this.getRotationVector();
        Vec3d lastVec = rotation.subtract(ccgrotation);
        this.setVelocity(lastVec);
    }
}
