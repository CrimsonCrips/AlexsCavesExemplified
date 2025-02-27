package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import com.github.alexmodguy.alexscaves.server.entity.ACFrogRegistry;
import com.github.alexmodguy.alexscaves.server.level.feature.AmbersolFeature;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.FrogVariant;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.animal.frog.Tadpole;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.ModList;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.compat.AMCompat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(AmbersolFeature.class)
public abstract class ACEAmberFeature extends Feature<NoneFeatureConfiguration> {

    public ACEAmberFeature(Codec<NoneFeatureConfiguration> pCodec) {
        super(pCodec);
    }
    //Massive Props to Drullkus for assistance


    @Inject(method = "drawOrb", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/WorldGenLevel;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"))
    private static void drawOrb(WorldGenLevel level, BlockPos center, RandomSource random, BlockState blockState, int radiusX, int radiusY, int radiusZ, CallbackInfo ci, @Local(ordinal = 1) BlockPos fill) {
        if (random.nextDouble() < 0.02 && AlexsCavesExemplified.COMMON_CONFIG.PRESERVED_AMBER_ENABLED.get() && level.ensureCanWrite(fill) && level.ensureCanWrite(center)) {
            ServerLevel servLevel = level.getLevel();
            switch (random.nextInt(0, 4)) {
                case 0:
                    if (!ModList.get().isLoaded("alexsmobs"))
                        return;
                    LivingEntity entity = AMCompat.createAmberAM(servLevel,random);
                    finalizeAmberSpawn(new Vec3(fill.getCenter().x, fill.getY() + 0.4, fill.getCenter().z),entity,servLevel,random);

                    break;
                case 1:
                    Frog frog = EntityType.FROG.create(servLevel);
                    if (frog != null) {
                        frog.setNoAi(true);
                        finalizeAmberSpawn(new Vec3(fill.getCenter().x, fill.getY() + 0.4, fill.getCenter().z),frog,servLevel,random);
                    }
                    break;
                default:
                    Tadpole tadpole = EntityType.TADPOLE.create(servLevel);
                    if (tadpole != null) {
                        tadpole.setNoAi(true);
                        finalizeAmberSpawn(new Vec3(fill.getCenter().x, fill.getY() + 0.4, fill.getCenter().z),tadpole,servLevel,random);
                    }
                    break;

            }
        }
    }

    @Unique
    private static void finalizeAmberSpawn(Vec3 position, LivingEntity entity, ServerLevel level, RandomSource random){
        entity.setInvulnerable(true);
        entity.setPos(position);
        int rotation = random.nextInt(0,361);
        entity.setYBodyRot(rotation);
        entity.setYHeadRot(rotation);
        entity.setYRot(rotation);
        entity.setSilent(true);

        if (entity instanceof Frog frog){
            FrogVariant frogVariant = switch (random.nextInt(0, 4)) {
                case 0 -> FrogVariant.COLD;
                case 1 -> FrogVariant.WARM;
                case 2 -> FrogVariant.TEMPERATE;
                default -> ACFrogRegistry.PRIMORDIAL.get();
            };
            frog.setVariant(frogVariant);
        }

        level.addFreshEntity(entity);
    }


}
