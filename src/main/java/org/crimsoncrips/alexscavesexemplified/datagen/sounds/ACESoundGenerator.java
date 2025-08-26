package org.crimsoncrips.alexscavesexemplified.datagen.sounds;

import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.crimsoncrips.alexscavesexemplified.client.ACExSoundRegistry;


public class ACESoundGenerator extends ACESoundProvider {

	public ACESoundGenerator(PackOutput output, ExistingFileHelper helper) {
		super(output, helper);
	}

	@Override
	public void registerSounds() {
		this.generateNewSoundWithSubtitle(ACExSoundRegistry.TESLA_EXPLODING, "block/tesla_exploding", 1);
		this.generateNewSoundWithSubtitle(ACExSoundRegistry.TESLA_FIRE, "block/tesla_fire", 2);
		this.generateNewSoundWithSubtitle(ACExSoundRegistry.TESLA_POWERUP, "block/tesla_powerup", 1);
		this.generateNewSoundWithSubtitle(ACExSoundRegistry.SWEET_PUNISHED, "entity/sweet_punished", 1);
		this.generateNewSoundWithSubtitle(ACExSoundRegistry.PSPSPSPS, "entity/pspspsps", 1);
		this.generateNewSoundWithSubtitle(ACExSoundRegistry.CARAMEL_EAT, "entity/caramel_eat", 1);

	}
}
