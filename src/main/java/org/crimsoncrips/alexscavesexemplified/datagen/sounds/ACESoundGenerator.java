package org.crimsoncrips.alexscavesexemplified.datagen.sounds;

import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.crimsoncrips.alexscavesexemplified.client.ACESoundRegistry;


public class ACESoundGenerator extends ACESoundProvider {

	public ACESoundGenerator(PackOutput output, ExistingFileHelper helper) {
		super(output, helper);
	}

	@Override
	public void registerSounds() {
		this.generateNewSoundWithSubtitle(ACESoundRegistry.TESLA_EXPLODING, "block/tesla_exploding", 1);
		this.generateNewSoundWithSubtitle(ACESoundRegistry.TESLA_FIRE, "block/tesla_fire", 2);
		this.generateNewSoundWithSubtitle(ACESoundRegistry.TESLA_POWERUP, "block/tesla_powerup", 1);
		this.generateNewSoundWithSubtitle(ACESoundRegistry.SWEET_PUNISHED, "entity/sweet_punished", 1);
		this.generateNewSoundWithSubtitle(ACESoundRegistry.PSPSPSPS, "entity/pspspsps", 1);
		this.generateNewSoundWithSubtitle(ACESoundRegistry.CARAMEL_EAT, "entity/caramel_eat", 1);

	}
}
