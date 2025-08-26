package org.crimsoncrips.alexscavesexemplified.server.entity;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.server.block.blockentity.NuclearSirenBlockEntity;
import com.github.alexmodguy.alexscaves.server.block.poi.ACPOIRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.item.NuclearBombEntity;
import com.github.alexmodguy.alexscaves.server.entity.item.NuclearExplosionEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.google.common.base.Predicates;
import net.hellomouse.alexscavesenriched.AlexsCavesEnriched;
import net.hellomouse.alexscavesenriched.entity.NuclearExplosion2Entity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.network.PlayMessages;
import org.crimsoncrips.alexscavesexemplified.client.particle.ACExParticleRegistry;
import org.crimsoncrips.alexscavesexemplified.compat.ACEnrichedCompat;
import org.crimsoncrips.alexscavesexemplified.misc.ACEUtils;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.Gammafied;
import org.crimsoncrips.alexscavesexemplified.server.blocks.ACExBlockRegistry;

import java.util.stream.Stream;

public class GammaNuclearBombEntity extends NuclearBombEntity {

    private static final EntityDataAccessor<Integer> TIME = SynchedEntityData.defineId(GammaNuclearBombEntity.class, EntityDataSerializers.INT);
    public static final int MAX_TIME = 300;
    private boolean spawnedExplosion = false;

    public GammaNuclearBombEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public GammaNuclearBombEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this((EntityType) ACExEntityRegistry.GAMMA_NUCLEAR_BOMB.get(), level);
        this.setBoundingBox(this.makeBoundingBox());
    }

    public void tick() {
        if (!this.isNoGravity()) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.04D, 0.0D));
        }
        this.move(MoverType.SELF, this.getDeltaMovement());
        this.setDeltaMovement(this.getDeltaMovement().scale(0.98D));
        if (this.onGround()) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.7D, -0.7, 0.7D));
        }
        if((tickCount + this.getId()) % 10 == 0 && level() instanceof ServerLevel serverLevel){
            getNearbySirens(serverLevel, (int) (256 * 1.5)).forEach(this::activateSiren);
        }
        int i = this.getTime() + 1;
        if (i > MAX_TIME) {
            this.discard();
            if (!this.level().isClientSide && !spawnedExplosion) {
                this.explode();
                spawnedExplosion = true;
            }
        } else {
            this.setTime(i);
            this.updateInWaterStateAndDoFluidPushing();
            if (this.level().isClientSide && MAX_TIME - i > 10 && random.nextFloat() < 0.3F && this.onGround()) {
                Vec3 center = this.getEyePosition();
                level().addParticle(ACExParticleRegistry.GAMMA_PROTON.get(), center.x, center.y, center.z, center.x, center.y, center.z);
            }
        }
    }

    private void explode() {
        if (ModList.get().isLoaded("alexscavesenriched") && AlexsCavesEnriched.CONFIG.nuclear.useNewNuke){
            ACEnrichedCompat.summonNuclearExplosion2(level(),this);
        } else {
            NuclearExplosionEntity vanillaNuke = ACEntityRegistry.NUCLEAR_EXPLOSION.get().create(level());
            vanillaNuke.setSize(AlexsCaves.COMMON_CONFIG.nukeExplosionSizeModifier.get().floatValue() * 1.5F);
            ((Gammafied) vanillaNuke).setGamma(true);
            vanillaNuke.copyPosition(this);
            level().addFreshEntity(vanillaNuke);
        }
    }

    public InteractionResult interact(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.is(Tags.Items.SHEARS)) {
            ACEUtils.awardOutsideAdvancement(player,"alexscaves/defuse_nuclear_bomb","interact",AlexsCaves.MODID);

            player.swing(hand);
            this.playSound((SoundEvent) ACSoundRegistry.NUCLEAR_BOMB_DEFUSE.get());
            this.remove(RemovalReason.KILLED);
            this.spawnAtLocation(new ItemStack((ItemLike) ACExBlockRegistry.GAMMA_NUCLEAR_BOMB.get()));
            if (!player.getAbilities().instabuild) {
                itemStack.hurtAndBreak(1, player, (e) -> e.broadcastBreakEvent(hand));
            }

            return InteractionResult.SUCCESS;
        } else if (player.isSecondaryUseActive()) {
            return InteractionResult.PASS;
        } else if (!this.level().isClientSide) {
            ACEUtils.awardOutsideAdvancement(player,"alexscaves/ride_nuclear_bomb","ride_a_boat_with_a_goat",AlexsCaves.MODID);

            return player.startRiding(this) ? InteractionResult.CONSUME : InteractionResult.PASS;
        } else {
            return InteractionResult.SUCCESS;

        }
    }

    private Stream<BlockPos> getNearbySirens(ServerLevel world, int range) {
        PoiManager pointofinterestmanager = world.getPoiManager();
        return pointofinterestmanager.findAll(poiTypeHolder -> poiTypeHolder.is(ACPOIRegistry.NUCLEAR_SIREN.getKey()), Predicates.alwaysTrue(), this.blockPosition(), range, PoiManager.Occupancy.ANY);
    }

    private void activateSiren(BlockPos pos) {
        BlockEntity var3 = this.level().getBlockEntity(pos);
        if (var3 instanceof NuclearSirenBlockEntity nuclearSirenBlock) {
            nuclearSirenBlock.setNearestNuclearBomb(this);
        }
    }

}
