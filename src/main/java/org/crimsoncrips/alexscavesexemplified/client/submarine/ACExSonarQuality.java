package org.crimsoncrips.alexscavesexemplified.client.submarine;

import net.minecraft.resources.ResourceLocation;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;

public enum ACExSonarQuality {
    HIGH("shaders/post/submarine_sonar.json"),
    LOW("shaders/post/submarine_sonar_fast.json");

    public final ResourceLocation chain;

    ACExSonarQuality(String chain) {
        this.chain = AlexsCavesExemplified.prefix(chain);
    }
}
