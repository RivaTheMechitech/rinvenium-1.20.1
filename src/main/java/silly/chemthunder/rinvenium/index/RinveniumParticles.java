package silly.chemthunder.rinvenium.index;

import com.mojang.serialization.Codec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import silly.chemthunder.rinvenium.Rinvenium;
import silly.chemthunder.rinvenium.particle.*;

import java.util.function.Function;

public class RinveniumParticles {
    public static final DefaultParticleType APMSDC = register("apmsdc_normal", true);
    public static final DefaultParticleType APMSDC_LOW = register("apmsdc_low_power", true);
    public static final DefaultParticleType APMSDC_HIGH = register("apmsdc_high_power", true);
    public static final DefaultParticleType HAIL_OF_THE_GODS_TRAIL = register("hail_of_the_gods_trail", true);
    public static final DefaultParticleType HAIL_OF_THE_GODS_SMOKE = register("hail_of_the_gods_smoke", true);
    public static final ParticleType<SmokeTrailParticleEffect> SMOKE_TRAIL = registerComplex("smoke_trail", true, SmokeTrailParticleEffect.PARAMETER_FACTORY, particleType -> SmokeTrailParticleEffect.CODEC);
    public static final ParticleType<RailgunTrailParticleEffect> RAILGUN_TRAIL = registerComplex("railgun_trail", true, RailgunTrailParticleEffect.PARAMETER_FACTORY, particleType -> RailgunTrailParticleEffect.CODEC);

    private static DefaultParticleType register(String name, boolean alwaysShow) {
        return Registry.register(Registries.PARTICLE_TYPE, Rinvenium.id(name), FabricParticleTypes.simple(alwaysShow));
    }

    private static <T extends ParticleEffect> ParticleType<T> registerComplex(String name, boolean alwaysShow, ParticleEffect.Factory<T> factory, Function<ParticleType<T>, Codec<T>> codecGetter) {
        return Registry.register(Registries.PARTICLE_TYPE, Rinvenium.id(name), new ParticleType<T>(alwaysShow, factory) {
            @Override
            public Codec<T> getCodec() {
                return (Codec<T>) codecGetter.apply(this);
            }
        });
    }

    @Environment(EnvType.CLIENT)
    public static void clientInit() {
        ParticleFactoryRegistry.getInstance().register(APMSDC, APMSDCParticle.FactoryNormal::new);
        ParticleFactoryRegistry.getInstance().register(APMSDC_LOW, APMSDCParticle.FactoryLowPower::new);
        ParticleFactoryRegistry.getInstance().register(APMSDC_HIGH, APMSDCParticle.FactoryHighPower::new);
        ParticleFactoryRegistry.getInstance().register(HAIL_OF_THE_GODS_TRAIL, HailOfTheGodsTrailParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(HAIL_OF_THE_GODS_SMOKE, HailOfTheGodsTrailParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(SMOKE_TRAIL, SmokeTrailParticle.Default.Factory::new);
        ParticleFactoryRegistry.getInstance().register(RAILGUN_TRAIL, RailgunTrailParticle.Default.Factory::new);
    }

    public static void init() {
        Rinvenium.LOGGER.info("Registering Rinvenium Particles");
    }
}