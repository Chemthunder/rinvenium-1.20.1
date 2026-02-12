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

    public static final SoundEvent SPEAR_DASH = registerSound("spear_dash");
    public static final SoundEvent SPEAR_DASH_IMPACT = registerSound("spear_dash_impact");
    public static final SoundEvent SPEAR_PARRY = registerSound("spear_parry");
    public static final SoundEvent SPEAR_SLASH = registerSound("spear_slash");

    private static SoundEvent registerSound(String name) {
        Identifier identifier = Rinvenium.id(name);
        return Registry.register(Registries.SOUND_EVENT, identifier, SoundEvent.of(identifier));
    }

    public static void registerRinveniumSoundEvents() {
        Rinvenium.LOGGER.info("Registering " + Rinvenium.MOD_ID + " sound events");
    }
}
