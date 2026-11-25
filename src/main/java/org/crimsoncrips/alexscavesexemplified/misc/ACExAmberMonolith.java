package org.crimsoncrips.alexscavesexemplified.misc;



import org.crimsoncrips.alexscavesexemplified.backport.ACExCompat;
import org.crimsoncrips.alexscavesexemplified.backport.ACExData;
import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.block.blockentity.AmberMonolithBlockEntity;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.level.biome.ACBiomeRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import org.crimsoncrips.alexscavesexemplified.datagen.tags.ACExEntityTagGenerator;
import org.crimsoncrips.alexscavesexemplified.mixins.blocks.ACExAmberMonolithAccessor;
import org.crimsoncrips.alexscavesexemplified.server.ACExAttachments;
import org.crimsoncrips.alexscavesexemplified.server.blocks.ACExBlockRegistry;
import org.crimsoncrips.alexscavesexemplified.server.entity.CuriosityOfferingEntity;
import org.crimsoncrips.alexscavesexemplified.server.entity.DarkRitualEntity;

import java.util.Optional;
import java.util.function.BooleanSupplier;

public class ACExAmberMonolith {

    public static final int GATHER_TICKS = ACExUtils.seconds(5.0F);
    private static final int CLOUD_TICKS = ACExUtils.seconds(15.0F);
    public static final int RITUAL_TICKS = GATHER_TICKS + CLOUD_TICKS;
    private static final float CURIOSITY_CUT = 0.1F;
    private static final int MIN_DARK_SUMMONS = 1;
    private static final int MAX_DARK_SUMMONS = 3;
    private static final int DARK_REPICK_TICKS = ACExUtils.seconds(5.0F);
    private static final int CURIOSITY_PARTICLES = 8;

    public static void onRightClick(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        if (!(level.getBlockEntity(event.getPos()) instanceof AmberMonolithBlockEntity monolith))
            return;
        ItemStack stack = event.getItemStack();
        boolean fed;
        if (stack.is(ACItemRegistry.AMBER_CURIOSITY.get())) {
            fed = feedCuriosity(level, monolith, stack);
        } else if (stack.is(ACItemRegistry.PURE_DARKNESS.get())) {
            fed = feedDarkness(level, monolith);
        } else {
            return;
        }
        if (!fed)
            return;
        if (!level.isClientSide) {
            ACExCompat.consume(stack, 1, event.getEntity());
        }
        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide));
    }

    private static boolean feedCuriosity(Level level, AmberMonolithBlockEntity monolith, ItemStack stack) {
        if (monolith.getSpawnsMobIn() <= 0)
            return false;
        if (!level.isClientSide) {
            level.addFreshEntity(new CuriosityOfferingEntity(level, monolith.getBlockPos(), stack.copyWithCount(1)));
        }
        return true;
    }

    private static boolean feedDarkness(Level level, AmberMonolithBlockEntity monolith) {
        if (isDark(monolith) || isRitualActive(monolith) || darkSummons(monolith).map(summons -> summons.size() == 0).orElse(true))
            return false;
        if (!level.isClientSide) {
            ACExData.set(monolith, ACExAttachments.MONOLITH_RITUAL, 1);
            monolith.setChanged();
        }
        return true;
    }

    public static void cutTimer(AmberMonolithBlockEntity monolith) {
        int spawnsMobIn = monolith.getSpawnsMobIn();
        if (spawnsMobIn <= 0)
            return;
        ((ACExAmberMonolithAccessor) monolith).setSpawnsMobIn(Math.round(spawnsMobIn * (1.0F - CURIOSITY_CUT)));
        monolith.setChanged();
        monolith.getLevel().sendBlockUpdated(monolith.getBlockPos(), monolith.getBlockState(), monolith.getBlockState(), 2);
        if (monolith.getLevel() instanceof ServerLevel serverLevel) {
            BlockPos pos = monolith.getBlockPos();
            for (int i = 0; i < CURIOSITY_PARTICLES; i++) {
                serverLevel.sendParticles(summonParticle(monolith, ACParticleRegistry.AMBER_EXPLOSION.get()), pos.getX() + serverLevel.random.nextFloat(), pos.getY() + serverLevel.random.nextFloat(), pos.getZ() + serverLevel.random.nextFloat(), 0, 0.0D, 0.0D, 0.0D, 1.0D);
            }
            serverLevel.playSound(null, pos, ACSoundRegistry.AMBER_MONOLITH_SUMMON.get(), SoundSource.BLOCKS, 0.6F, 1.4F);
        }
    }

    public static int ticksUntilSummon(AmberMonolithBlockEntity monolith) {
        int ritualLeft = isRitualActive(monolith) ? RITUAL_TICKS - ACExData.get(monolith, ACExAttachments.MONOLITH_RITUAL) + 1 : 0;
        return Math.max(monolith.getSpawnsMobIn(), ritualLeft);
    }

    public static boolean isDark(AmberMonolithBlockEntity monolith) {
        return monolith.getBlockState().is(ACExBlockRegistry.DARK_MONOLITH.get());
    }

    public static boolean isRitualActive(AmberMonolithBlockEntity monolith) {
        return ACExData.has(monolith, ACExAttachments.MONOLITH_RITUAL) && ACExData.get(monolith, ACExAttachments.MONOLITH_RITUAL) > 0;
    }

    private static Optional<HolderSet.Named<EntityType<?>>> darkSummons(AmberMonolithBlockEntity monolith) {
        TagKey<EntityType<?>> summons = monolith.getLevel().getBiome(monolith.getBlockPos()).is(ACBiomeRegistry.FORLORN_HOLLOWS) ? ACExEntityTagGenerator.FORLORN_DARK_MONOLITH_SUMMONS : ACExEntityTagGenerator.DARK_MONOLITH_SUMMONS;
        return BuiltInRegistries.ENTITY_TYPE.getTag(summons);
    }

    private static boolean pickDarkSummon(AmberMonolithBlockEntity monolith) {
        Level level = monolith.getLevel();
        BlockPos pos = monolith.getBlockPos();
        Optional<EntityType<?>> summon = darkSummons(monolith).flatMap(set -> set.getRandomElement(level.random)).map(Holder::value);
        if (summon.isEmpty())
            return false;
        ACExAmberMonolithAccessor accessor = (ACExAmberMonolithAccessor) monolith;
        accessor.setSpawnType(summon.get());
        accessor.setSpawnCount(Mth.nextInt(level.random, MIN_DARK_SUMMONS, MAX_DARK_SUMMONS));
        monolith.setChanged();
        level.sendBlockUpdated(pos, monolith.getBlockState(), monolith.getBlockState(), 2);
        return true;
    }

    public static void keepDarkSummon(AmberMonolithBlockEntity monolith) {
        if (isDark(monolith)) {
            pickDarkSummon(monolith);
        }
    }

    public static boolean summon(AmberMonolithBlockEntity monolith, BooleanSupplier spawnMobs) {
        if (isRitualActive(monolith))
            return false;
        if (!isDark(monolith))
            return spawnMobs.getAsBoolean();
        if (monolith.getLevel().getDifficulty() == Difficulty.PEACEFUL)
            return false;
        boolean spawned = spawnMobs.getAsBoolean();
        int fails = spawned ? 0 : ACExData.get(monolith, ACExAttachments.MONOLITH_SUMMON_FAILS) + 1;
        if (fails >= DARK_REPICK_TICKS) {
            pickDarkSummon(monolith);
            fails = 0;
        }
        ACExData.set(monolith, ACExAttachments.MONOLITH_SUMMON_FAILS, fails);
        return spawned;
    }

    public static void tickRitual(AmberMonolithBlockEntity monolith) {
        if (!isRitualActive(monolith))
            return;
        Level level = monolith.getLevel();
        BlockPos pos = monolith.getBlockPos();
        int ritual = ACExData.get(monolith, ACExAttachments.MONOLITH_RITUAL);
        if (ritual >= RITUAL_TICKS) {
            completeRitual(monolith);
            return;
        }
        if (level.getEntitiesOfClass(DarkRitualEntity.class, new AABB(pos)).isEmpty()) {
            level.addFreshEntity(new DarkRitualEntity(level, pos, level.getGameTime() - (ritual - 1)));
        }
        ACExData.set(monolith, ACExAttachments.MONOLITH_RITUAL, ritual + 1);
    }

    private static void completeRitual(AmberMonolithBlockEntity monolith) {
        Level level = monolith.getLevel();
        BlockPos pos = monolith.getBlockPos();
        ACExData.set(monolith, ACExAttachments.MONOLITH_RITUAL, 0);
        CompoundTag data = monolith.saveWithoutMetadata();
        level.setBlockAndUpdate(pos, ACExBlockRegistry.DARK_MONOLITH.get().defaultBlockState());
        if (level.getBlockEntity(pos) instanceof AmberMonolithBlockEntity darkMonolith) {
            darkMonolith.load(data);
            pickDarkSummon(darkMonolith);
        }
        level.playSound(null, pos, ACSoundRegistry.UNDERZEALOT_TRANSFORMATION.get(), SoundSource.BLOCKS, 8.0F, 1.0F);
    }

    public static ParticleOptions summonParticle(AmberMonolithBlockEntity monolith, ParticleOptions particle) {
        if (!isDark(monolith))
            return particle;
        return particle == ACParticleRegistry.AMBER_MONOLITH.get() ? ACParticleRegistry.UNDERZEALOT_MAGIC.get() : ACParticleRegistry.UNDERZEALOT_EXPLOSION.get();
    }
}
