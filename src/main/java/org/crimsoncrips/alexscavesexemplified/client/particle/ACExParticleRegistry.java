package org.crimsoncrips.alexscavesexemplified.client.particle;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;

public class ACExParticleRegistry {
    public static final DeferredRegister<ParticleType<?>> DEF_REG = DeferredRegister.create(Registries.PARTICLE_TYPE, AlexsCavesExemplified.MODID);

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> TREMORZILLA_GAMMA_EXPLOSION = DEF_REG.register("tremorzilla_gamma_explosion", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> TREMORZILLA_GAMMA_PROTON = DEF_REG.register("tremorzilla_gamma_proton", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> TREMORZILLA_GAMMA_LIGHTNING = DEF_REG.register("tremorzilla_gamma_lightning", () -> new SimpleParticleType(false));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> GAMMA_PROTON = DEF_REG.register("gamma_proton", () -> new SimpleParticleType(false));


    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> GAMMA_MUSHROOM_CLOUD = DEF_REG.register("gamma_mushroom_cloud", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> MINI_GAMMA_MUSHROOM_CLOUD = DEF_REG.register("mini_gamma_mushroom_cloud", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> GAMMA_TEPHRA = DEF_REG.register("gamma_tephra", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> GAMMA_TEPHRA_SMALL = DEF_REG.register("gamma_tephra_small", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> GAMMA_TEPHRA_FLAME = DEF_REG.register("gamma_tephra_flame", () -> new SimpleParticleType(false));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> AZURE_FOCUSED_LIGHTNING = DEF_REG.register("azure_focused_lightning", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SCARLET_FOCUSED_LIGHTNING = DEF_REG.register("scarlet_focused_lightning", () -> new SimpleParticleType(false));

}
