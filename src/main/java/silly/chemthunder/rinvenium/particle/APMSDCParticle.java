package silly.chemthunder.rinvenium.particle;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.PortalParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

public class APMSDCParticle extends PortalParticle {
    public APMSDCParticle(ClientWorld clientWorld, double d, double e, double f, double g, double h, double i, int maxAge) {
        super(clientWorld, d, e, f, g, h, i);
        this.red = 1.0F;
        this.green = 1.0F;
        this.blue = 1.0F;
        this.maxAge = maxAge;
    }

    public static class FactoryNormal implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public FactoryNormal(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            APMSDCParticle apmsdcParticle = new APMSDCParticle(clientWorld, d, e, f, g, h, i, 20);
            apmsdcParticle.setSprite(this.spriteProvider);
            return apmsdcParticle;
        }
    }

    public static class FactoryLowPower implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public FactoryLowPower(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            APMSDCParticle apmsdcParticle = new APMSDCParticle(clientWorld, d, e, f, g, h, i, 10);
            apmsdcParticle.setSprite(this.spriteProvider);
            return apmsdcParticle;
        }
    }

    public static class FactoryHighPower implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public FactoryHighPower(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            APMSDCParticle apmsdcParticle = new APMSDCParticle(clientWorld, d, e, f, g, h, i, 30);
            apmsdcParticle.setSprite(this.spriteProvider);
            return apmsdcParticle;
        }
    }
}
