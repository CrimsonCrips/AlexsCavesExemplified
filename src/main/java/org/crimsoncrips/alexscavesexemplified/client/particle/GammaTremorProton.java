package org.crimsoncrips.alexscavesexemplified.client.particle;

import com.github.alexmodguy.alexscaves.client.particle.ProtonParticle;
import com.github.alexmodguy.alexscaves.client.particle.TremorzillaProtonParticle;
import com.github.alexmodguy.alexscaves.client.render.entity.TremorzillaRenderer;
import com.github.alexmodguy.alexscaves.server.entity.living.TremorzillaEntity;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;

public class GammaTremorProton extends TremorzillaProtonParticle {


    protected GammaTremorProton(ClientLevel world, double x, double y, double z, int entityId) {
        super(world, x, y, z, entityId);
        this.lifetime = 40;
        this.orbitSpeed = 20;

    }

    @Override
    public float getTrailHeight() {
        return 1F;
    }



    public static class Factory implements ParticleProvider<SimpleParticleType> {

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            GammaTremorProton particle = new GammaTremorProton(worldIn, x, y, z, (int)xSpeed);
            particle.setColor(0F, 1F, 1F);
            particle.trailR = 0F;
            particle.trailG = 1F;
            particle.trailB = 1F;
            return particle;
        }
    }
}
