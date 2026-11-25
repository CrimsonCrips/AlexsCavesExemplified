package org.crimsoncrips.alexscavesexemplified.client.particle;

import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;
import com.github.alexmodguy.alexscaves.client.ClientProxy;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.SimpleParticleType;

public class MiniGammaMushroomCloud extends GammaMushroomCloud {

    private static final int EXTRA_TIME = ACExUtils.seconds(1.0F);
    private static final int LIFETIME = ACExUtils.seconds(2.0F) + EXTRA_TIME;
    private static final float SPEED_UP = 1.6F;
    private static final int FLASH_FOR = Math.round(ACExUtils.seconds(0.5F) / SPEED_UP) + EXTRA_TIME;
    private static final int FLASH_HOLD = Math.round(ACExUtils.seconds(0.8F) / SPEED_UP);
    private static final int SKY_DARK = Math.round(ACExUtils.seconds(3.5F) / SPEED_UP);
    private static final int MUTE = Math.round(ACExUtils.seconds(2.5F) / SPEED_UP) + EXTRA_TIME;
    private static final float BASE_FADE = 0.05F;
    private static final float EXTRA_FADE = BASE_FADE * (SPEED_UP - 1.0F);

    private final int flashFor;
    private int flashLeft;

    protected MiniGammaMushroomCloud(ClientLevel level, double x, double y, double z, float scale, float duration) {
        super(level, x, y, z, scale, false);
        this.lifetime = Math.round(LIFETIME * duration);
        this.flashFor = Math.round(FLASH_FOR * duration);
    }

    @Override
    public void tick() {
        super.tick();
        ClientProxy.renderNukeSkyDarkFor = SKY_DARK;
        ClientProxy.muteNonNukeSoundsFor = MUTE;
        this.flashLeft = this.age < this.flashFor ? FLASH_HOLD : Math.max(0, this.flashLeft - 1);
        ClientProxy.renderNukeFlashFor = this.flashLeft;
        if (this.flashLeft <= 0) {
            ClientProxy.nukeFlashAmount = Math.max(0.0F, ClientProxy.nukeFlashAmount - EXTRA_FADE);
        }
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new MiniGammaMushroomCloud(level, x, y, z, (float) Math.max(0.3D, xSpeed), ySpeed > 0.0D ? (float) ySpeed : 1.0F);
        }
    }
}
