package silly.chemthunder.rinvenium.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import silly.chemthunder.rinvenium.cca.entity.EnvixiaFormComponent;
import silly.chemthunder.rinvenium.index.RinveniumItems;
import silly.chemthunder.rinvenium.render.manager.client.*;
import silly.chemthunder.rinvenium.render.manager.client.FakePlayerRendererManager;
import silly.chemthunder.rinvenium.util.inject.RenderContainer;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity implements RenderContainer {
    @Shadow @Final public ClientPlayNetworkHandler networkHandler;
    @Unique private final FlashManager flashManager = new FlashManager();
    @Unique private final ImpactFrameManager impactFrameManager = new ImpactFrameManager();
    @Unique private final SlashRendererManager slashRendererManager = new SlashRendererManager();
    @Unique private final FakePlayerRendererManager fakePlayerRendererManager = new FakePlayerRendererManager();
    @Unique private final APMDSCBeamManager apmdscBeamManager = new APMDSCBeamManager();

    public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @WrapOperation(
        method = "tickMovement",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z"
        )
    )
    private boolean gitsnshiggles$noMovementSlowWhenSwordBlocking(ClientPlayerEntity player, Operation<Boolean> original) {
        if (player.getActiveItem().isOf(RinveniumItems.ENVINIUM_GLAIVE)) return false;
        if (player.getActiveItem().isOf(RinveniumItems.HAIL_OF_THE_GODS)) return false;
        return original.call(player);
    }

    @Inject(
        method = "tickMovement",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/network/ClientPlayerEntity;getEquippedStack(Lnet/minecraft/entity/EquipmentSlot;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private void rinvenium$elytraFly(CallbackInfo ci) {
        ItemStack itemStack = this.getEquippedStack(EquipmentSlot.CHEST);
        EnvixiaFormComponent envixiaFormComponent = EnvixiaFormComponent.get((ClientPlayerEntity) ((Object)this));
        
        if (((itemStack.isOf(RinveniumItems.ENVIXIA_CHESTPLATE) && envixiaFormComponent.getTripleBoolValue1()) || (itemStack.isOf(Items.ELYTRA) && ElytraItem.isUsable(itemStack))) && this.checkFallFlying()) {
            this.networkHandler.sendPacket(new ClientCommandC2SPacket(this, ClientCommandC2SPacket.Mode.START_FALL_FLYING));
        }
    }

    @Override
    public FlashManager getFlashManager() {
        return this.flashManager;
    }

    @Override
    public ImpactFrameManager getImpactFrameManager() {
        return this.impactFrameManager;
    }

    @Override
    public SlashRendererManager getSlashRendererManager() {
        return this.slashRendererManager;
    }

    @Override
    public FakePlayerRendererManager getFakePlayerRendererManager() {
        return this.fakePlayerRendererManager;
    }
    @Override
    public APMDSCBeamManager getAPMDSCBeamManager() {
        return this.apmdscBeamManager;
    }
}