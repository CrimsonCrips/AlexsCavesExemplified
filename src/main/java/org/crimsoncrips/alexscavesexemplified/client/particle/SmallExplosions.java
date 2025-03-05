package org.crimsoncrips.alexscavesexemplified.client.particle;

import com.github.alexmodguy.alexscaves.client.particle.SmallExplosionParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.FastColor;

public class SmallExplosions extends SmallExplosionParticle {

    protected SmallExplosions(ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprites, boolean shortLifespan, int color1) {
        super(world, x, y, z, xSpeed, ySpeed, zSpeed, sprites, shortLifespan, color1);
    }

    public static class GammaTremorzillaFactory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public GammaTremorzillaFactory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            SmallExplosions particle = new SmallExplosions(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet, true, 0X03dffc);
            particle.setSpriteFromAge(spriteSet);
            particle.lifetime = 9 + worldIn.random.nextInt(3);
            particle.scale(1.8F + worldIn.random.nextFloat() * 0.9F);
            particle.setFadeColor(0X6df2e3);
            return particle;
        }
    }

}
