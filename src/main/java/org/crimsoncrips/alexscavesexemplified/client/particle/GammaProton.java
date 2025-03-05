package org.crimsoncrips.alexscavesexemplified.client.particle;

import com.github.alexmodguy.alexscaves.client.particle.ProtonParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.SimpleParticleType;

public class GammaProton extends ProtonParticle {


    protected GammaProton(ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(world, x, y, z, xSpeed, ySpeed, zSpeed);
        rCol = 0F;
        gCol = 0.9F;
        bCol = 1.0F;
    }


    public static class GammaFactory implements ParticleProvider<SimpleParticleType> {
        public GammaFactory() {
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            GammaProton particle = new GammaProton(worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
            particle.trailR = 0.0F;
            particle.trailG = 0.9F;
            particle.trailB = 1.0F;
            return particle;
        }
    }

}
