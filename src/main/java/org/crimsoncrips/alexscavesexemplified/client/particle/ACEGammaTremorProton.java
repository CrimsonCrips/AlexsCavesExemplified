package org.crimsoncrips.alexscavesexemplified.client.particle;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.client.particle.ProtonParticle;
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

public class ACEGammaTremorProton extends ProtonParticle {

    private static final ResourceLocation CENTER_TEXTURE = new ResourceLocation(AlexsCavesExemplified.MODID, "textures/particle/tremorzilla_gamma_proton.png");
    private final int tremorzillaId;
    private final float initialXRot;
    private final float initialYRot;

    protected ACEGammaTremorProton(ClientLevel world, double x, double y, double z, int entityId) {
        super(world, x, y, z, 0, 0, 0);
        this.lifetime = 40;
        orbitOffset = new Vec3(0, 0, 1.0F + random.nextFloat() * 0.2F);
        this.orbitSpeed = 20;
        this.tremorzillaId = entityId;
        setInMouthPos(1.0F);
        this.setPos(this.orbitX, this.orbitY, this.orbitZ);
        this.initialXRot = random.nextFloat() * 180F;
        this.initialYRot = random.nextFloat() * 180F;

    }

    public ResourceLocation getTexture() {
        return CENTER_TEXTURE;
    }

    @Override
    public float getTrailHeight() {
        return 1F;
    }


    public void tick() {
        this.setInMouthPos(1.0F);
        super.tick();
        float fadeIn = 0.8F * Mth.clamp(age / (float) this.lifetime * 32.0F, 0.0F, 1.0F);
        float fadeOut = Mth.clamp( 1F - age / (float) this.lifetime * 0.5F, 0.0F, 1.0F);
        this.trailA = fadeIn * fadeOut;
    }

    public float getAlpha() {
        return age < 2 ? 0.0F : 1.0F;
    }

    @Override
    public Vec3 getOrbitPosition(float angle) {
        Vec3 center = new Vec3(orbitX, orbitY, orbitZ);
        Vec3 add = orbitOffset.scale(orbitDistance).yRot((float)Math.toRadians(initialYRot)).xRot((float)Math.toRadians(initialXRot));
        float rot = angle * (reverseOrbit ? -orbitSpeed : orbitSpeed) * (float) (Math.PI / 360F);
        switch (orbitAxis) {
            case 0:
                add = add.xRot(rot);
                break;
            case 1:
                add = add.yRot(rot);
                break;
            case 2:
                add = add.zRot(rot);
                break;
        }
        return center.add(add);
    }

    public void setInMouthPos(float partialTick) {
        if (tremorzillaId != -1 && level.getEntity(tremorzillaId) instanceof TremorzillaEntity entity) {
            Vec3 mouthPos = TremorzillaRenderer.getMouthPositionFor(tremorzillaId);
            if (mouthPos != null) {
                Vec3 translate = mouthPos.yRot((float) (Math.PI - entity.yBodyRot * ((float) Math.PI / 360F)));
                Vec3 newOrbit = new Vec3(entity.getX() + translate.x, entity.getY() + translate.y, entity.getZ() + translate.z);
                this.orbitX = newOrbit.x;
                this.orbitY = newOrbit.y;
                this.orbitZ = newOrbit.z;
            }
            if(entity.getBeamProgress(1.0F) <= 0){
                this.remove();
            }
        }
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            ACEGammaTremorProton particle = new ACEGammaTremorProton(worldIn, x, y, z, (int)xSpeed);
            particle.setColor(0F, 1F, 1F);
            particle.trailR = 0F;
            particle.trailG = 1F;
            particle.trailB = 1F;
            return particle;
        }
    }

}
