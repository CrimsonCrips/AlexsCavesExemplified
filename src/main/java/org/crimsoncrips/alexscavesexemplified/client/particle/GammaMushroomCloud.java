package org.crimsoncrips.alexscavesexemplified.client.particle;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.client.ClientProxy;
import com.github.alexmodguy.alexscaves.client.model.MushroomCloudModel;
import com.github.alexmodguy.alexscaves.client.particle.MushroomCloudParticle;
import com.github.alexmodguy.alexscaves.client.render.ACRenderTypes;
import com.github.alexmodguy.alexscaves.client.sound.NuclearExplosionSound;
import com.github.alexmodguy.alexscaves.server.misc.ACMath;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class GammaMushroomCloud extends MushroomCloudParticle {

   private static final ResourceLocation GAMMA = new ResourceLocation("alexscavesexemplified:textures/entity/gamma_mushroom_cloud.png");
    private static final ResourceLocation GAMMA_GLOW = new ResourceLocation("alexscavesexemplified:textures/entity/gamma_mushroom_cloud_glow.png");

    protected GammaMushroomCloud(ClientLevel level, double x, double y, double z, float scale, boolean pink) {
        super(level, x, y, z, scale, pink);
        this.scale = scale + 0.2F;
    }
    private final float scale;
    private static final MushroomCloudModel MODEL = new MushroomCloudModel();
    private static final int BALL_FOR = 10;
    private static final int FADE_SPEED = 10;

    private boolean playedRinging;
    private boolean playedExplosion;
    private boolean playedRumble;


    public void tick() {
        ((ClientProxy) AlexsCaves.PROXY).renderNukeSkyDarkFor = 70;
        ((ClientProxy) AlexsCaves.PROXY).muteNonNukeSoundsFor = 50;
        boolean large = this.scale > 2.0F;
        if(age > BALL_FOR / 2 + 5){
            if(!playedExplosion){
                playedExplosion = true;
                playSound(large ? ACSoundRegistry.LARGE_NUCLEAR_EXPLOSION.get() : ACSoundRegistry.NUCLEAR_EXPLOSION.get(), lifetime - 20, lifetime, 0.2F, false);
            }
        }
        if (age < BALL_FOR) {
            if(!playedRinging && AlexsCaves.CLIENT_CONFIG.nuclearBombFlash.get()){
                playedRinging = true;
                playSound(ACSoundRegistry.NUCLEAR_EXPLOSION_RINGING.get(), 100, 50, 0.05F, true);
            }
            ((ClientProxy) AlexsCaves.PROXY).renderNukeFlashFor = 16;
        } else if (age < lifetime - FADE_SPEED) {
            float life = (float) (Math.log(1 + (age - BALL_FOR) / (float) (lifetime - BALL_FOR))) * 2F;
            float explosionSpread = (12 * life + 4F) * scale;
            for (int i = 0; i < (1 + random.nextInt(2)) * scale; i++) {
                Vec3 from = new Vec3(level.random.nextFloat() - 0.5F, level.random.nextFloat() - 0.5F, level.random.nextFloat() - 0.5F).scale(scale * 1.4F).add(this.x, this.y, this.z);
                Vec3 away = new Vec3(level.random.nextFloat() - 0.5F, level.random.nextFloat() - 0.5F, level.random.nextFloat() - 0.5F).scale(2.34F);
                this.level.addParticle(ACEParticleRegistry.TREMORZILLA_GAMMA_EXPLOSION.get(), from.x, from.y, from.z, away.x, away.y, away.z);
            }
            for (int j = 0; j < scale * scale; j++) {
                Vec3 explosionBase = new Vec3((level.random.nextFloat() - 0.5F) * explosionSpread, (-0.6F + level.random.nextFloat() * 0.5F) * explosionSpread * 0.1F, (level.random.nextFloat() - 0.5F) * explosionSpread).add(this.x, this.y, this.z);
                this.level.addParticle(ACEParticleRegistry.TREMORZILLA_GAMMA_EXPLOSION.get(), explosionBase.x, explosionBase.y, explosionBase.z, 0, 0, 0);
            }
            if(age > BALL_FOR){
                if(!playedRumble){
                    playedRumble = true;
                    playSound(ACSoundRegistry.NUCLEAR_EXPLOSION_RUMBLE.get(), lifetime + 100, lifetime, 0.1F, true);
                }
            }
        }
        super.tick();
    }

    public void render(VertexConsumer vertexConsumer, Camera camera, float partialTick) {
        Vec3 vec3 = camera.getPosition();

        float f = (float)(Mth.lerp((double)partialTick, this.xo, this.x) - vec3.x());
        float f1 = (float)(Mth.lerp((double)partialTick, this.yo, this.y) - vec3.y());
        float f2 = (float)(Mth.lerp((double)partialTick, this.zo, this.z) - vec3.z());
        PoseStack posestack = new PoseStack();
        posestack.pushPose();
        posestack.translate(f, f1 - 0.5F, f2);
        posestack.scale(-this.scale, -this.scale, this.scale);
        MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers().bufferSource();
        MODEL.hideFireball(this.age >= 10);
        float life = (float)Math.log((double)(1.0F + ((float)(this.age - 10) + partialTick) / (float)(this.lifetime - 10))) * 2.0F;
        float glowLife = life < 1.0F ? 1.0F - life : 0.0F;
        int left = this.lifetime - this.age;
        float alpha = left <= 10 ? (float)left / 10.0F : 1.0F;
        MODEL.animateParticle((float)this.age, ACMath.smin(life, 1.0F, 0.5F), partialTick);
        VertexConsumer baseConsumer = multibuffersource$buffersource.getBuffer(RenderType.entityTranslucent(GAMMA));
        MODEL.renderToBuffer(posestack, baseConsumer, this.getLightColor(partialTick), OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, alpha);
        VertexConsumer glowConsumer1 = multibuffersource$buffersource.getBuffer(ACRenderTypes.getEyesAlphaEnabled(GAMMA));
        MODEL.renderToBuffer(posestack, glowConsumer1, 240, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, alpha);
        VertexConsumer glowConsumer2 = multibuffersource$buffersource.getBuffer(ACRenderTypes.getEyesAlphaEnabled(GAMMA_GLOW));
        MODEL.renderToBuffer(posestack, glowConsumer2, 240, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, glowLife * alpha);
        multibuffersource$buffersource.endBatch();
        posestack.popPose();
    }

    private void playSound(SoundEvent soundEvent, int duration, int fadesAt, float fadeInBy, boolean looping){
        Minecraft.getInstance().getSoundManager().queueTickingSound(new NuclearExplosionSound(soundEvent, this.x, this.y, this.z, duration, fadesAt, fadeInBy, looping));
    }

    public static class GammaNukeFactory implements ParticleProvider<SimpleParticleType> {
        public GammaNukeFactory() {
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            if (xSpeed == (double)0.0F) {
                xSpeed = (double)1.0F;
            }

            return new GammaMushroomCloud(worldIn, x, y, z, (float)Math.max((double)0.5F, xSpeed), ySpeed >= (double)1.0F);
        }
    }




}
