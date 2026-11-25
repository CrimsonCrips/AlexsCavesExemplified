package org.crimsoncrips.alexscavesexemplified.client.submarine;

import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.AudioStream;
import net.minecraft.client.sounds.SoundBufferLibrary;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.client.sounds.WeighedSoundEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

@OnlyIn(Dist.CLIENT)
public class ACExMuffledSoundInstance implements SoundInstance {

    private final SoundInstance wrapped;
    private final float volumeScale;

    public ACExMuffledSoundInstance(SoundInstance wrapped, float volumeScale) {
        this.wrapped = wrapped;
        this.volumeScale = volumeScale;
    }

    @Override
    public ResourceLocation getLocation() {
        return this.wrapped.getLocation();
    }

    @Nullable
    @Override
    public WeighedSoundEvents resolve(SoundManager manager) {
        return this.wrapped.resolve(manager);
    }

    @Override
    public Sound getSound() {
        return this.wrapped.getSound();
    }

    @Override
    public SoundSource getSource() {
        return this.wrapped.getSource();
    }

    @Override
    public boolean isLooping() {
        return this.wrapped.isLooping();
    }

    @Override
    public boolean isRelative() {
        return this.wrapped.isRelative();
    }

    @Override
    public int getDelay() {
        return this.wrapped.getDelay();
    }

    @Override
    public float getVolume() {
        return this.wrapped.getVolume() * this.volumeScale;
    }

    @Override
    public float getPitch() {
        return this.wrapped.getPitch();
    }

    @Override
    public double getX() {
        return this.wrapped.getX();
    }

    @Override
    public double getY() {
        return this.wrapped.getY();
    }

    @Override
    public double getZ() {
        return this.wrapped.getZ();
    }

    @Override
    public Attenuation getAttenuation() {
        return this.wrapped.getAttenuation();
    }

    @Override
    public boolean canStartSilent() {
        return this.wrapped.canStartSilent();
    }

    @Override
    public boolean canPlaySound() {
        return this.wrapped.canPlaySound();
    }

    @Override
    public CompletableFuture<AudioStream> getStream(SoundBufferLibrary soundBuffers, Sound sound, boolean looping) {
        return this.wrapped.getStream(soundBuffers, sound, looping);
    }
}
