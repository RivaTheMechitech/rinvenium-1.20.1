package silly.chemthunder.rinvenium.index;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import silly.chemthunder.rinvenium.Rinvenium;

public class RinveniumSoundEvents {
    private RinveniumSoundEvents() {
        Rinvenium.LOGGER.info("Rinvenium Sound Events was instantiated");
    }

    public static final SoundEvent GLAIVE_DASH = registerSound("glaive_dash");
    public static final SoundEvent GLAIVE_DASH_IMPACT = registerSound("glaive_dash_impact");
    public static final SoundEvent GLAIVE_PARRY = registerSound("glaive_parry");
    public static final SoundEvent GLAIVE_SLASH = registerSound("glaive_slash");
    public static final SoundEvent HAIL_OF_THE_GODS_SHOOT = registerSound("hail_of_the_gods_shoot");
    public static final SoundEvent HAIL_OF_THE_GODS_OVERHEAT = registerSound("overheat");
    public static final SoundEvent ENVIXIA_CORE_USE = registerSound("envixia_core_use");
    public static final SoundEvent ENVIXIUS_FORGED = registerSound("envixius_forged");
    public static final SoundEvent INGOT_FORGED = registerSound("ingot_forged");
    public static final SoundEvent ION_CELL_FORMED = registerSound("ion_cell_formed");
    public static final SoundEvent PLATE_FORMED = registerSound("plate_formed");
    public static final SoundEvent BELL = registerSound("bell");
    public static final SoundEvent HEARTBEAT = registerSound("heartbeat");

    private static SoundEvent registerSound(String name) {
        Identifier identifier = Rinvenium.id(name);
        return Registry.register(Registries.SOUND_EVENT, identifier, SoundEvent.of(identifier));
    }

    public static void registerRinveniumSoundEvents() {
        Rinvenium.LOGGER.info("Registering " + Rinvenium.MOD_ID + " sound events");
    }
}