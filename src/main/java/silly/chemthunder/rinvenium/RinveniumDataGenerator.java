package silly.chemthunder.rinvenium;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import silly.chemthunder.rinvenium.datagen.RinveniumBlockTagProvider;
import silly.chemthunder.rinvenium.datagen.RinveniumItemTagProvider;
import silly.chemthunder.rinvenium.datagen.RinveniumModelProvider;
import silly.chemthunder.rinvenium.datagen.RinveniumRecipeProvider;

public class RinveniumDataGenerator implements DataGeneratorEntrypoint {
	
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(RinveniumItemTagProvider::new);
        pack.addProvider(RinveniumBlockTagProvider::new);
        pack.addProvider(RinveniumRecipeProvider::new);
        pack.addProvider(RinveniumModelProvider::new);
	}
}
