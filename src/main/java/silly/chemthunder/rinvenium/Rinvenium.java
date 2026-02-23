package silly.chemthunder.rinvenium;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import silly.chemthunder.rinvenium.index.RinveniumEnchantments;
import silly.chemthunder.rinvenium.index.RinveniumEntities;
import silly.chemthunder.rinvenium.index.RinveniumItems;
import silly.chemthunder.rinvenium.index.RinveniumPackets;
import silly.chemthunder.rinvenium.index.RinveniumParticles;
import silly.chemthunder.rinvenium.index.RinveniumSoundEvents;
import silly.chemthunder.rinvenium.index.RinveniumStatusEffects;

import java.util.ArrayList;
import java.util.UUID;

public class Rinvenium implements ModInitializer {
	public static final String MOD_ID = "rinvenium";

    public static Identifier id (String path){
        return Identifier.of(MOD_ID, path); }
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final ArrayList<UUID> haters = new ArrayList<>();

	public void onInitialize() {
        RinveniumItems.init();
        RinveniumEnchantments.init();
        RinveniumStatusEffects.init();
        RinveniumEntities.init();
        RinveniumSoundEvents.registerRinveniumSoundEvents();
        RinveniumPackets.registerC2SPackets();
        RinveniumParticles.init();

		LOGGER.info(MOD_ID + " has been successfully initalized!");
        LOGGER.info("balls");

        // haters
        haters.add(UUID.fromString("c2fd27cf-5931-462b-8d7d-7f11adb7998b"));
        haters.add(UUID.fromString("d0f1f0f4-631e-4290-9f60-78ace9e5e0ef"));
	}
}