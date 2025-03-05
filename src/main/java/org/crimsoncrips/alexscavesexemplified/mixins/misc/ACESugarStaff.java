package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import com.github.alexmodguy.alexscaves.server.enchantment.ACEnchantmentRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.item.FrostmintSpearEntity;
import com.github.alexmodguy.alexscaves.server.entity.item.SpinningPeppermintEntity;
import com.github.alexmodguy.alexscaves.server.entity.item.SugarStaffHexEntity;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.item.SeaStaffItem;
import com.github.alexmodguy.alexscaves.server.item.SugarStaffItem;
import com.github.alexmodguy.alexscaves.server.misc.ACMath;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.misc.ACEUtils;
import org.spongepowered.asm.mixin.Mixin;


@Mixin(SugarStaffItem.class)
public abstract class ACESugarStaff extends Item {

    public ACESugarStaff(Properties pProperties) {
        super(pProperties);
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            boolean hex = player.isShiftKeyDown();
            player.swing(hand);
            Entity lookingAtEntity = SeaStaffItem.getClosestLookingAtEntityFor(level, player, (double)32.0F);

            if (player.getItemInHand(InteractionHand.OFF_HAND).is(ACItemRegistry.RADIANT_ESSENCE.get()) && AlexsCavesExemplified.COMMON_CONFIG.RADIANT_WRATH_ENABLED.get()){
                if(hex){
                    int humunguous = itemstack.getEnchantmentLevel(ACEnchantmentRegistry.HUMUNGOUS_HEX.get());
                    float maxDist = 128;
                    HitResult realHitResult = ProjectileUtil.getHitResultOnViewVector(player, Entity::canBeHitByProjectile, maxDist);
                    if(realHitResult.getType() == HitResult.Type.MISS){
                        realHitResult = ProjectileUtil.getHitResultOnViewVector(player, Entity::canBeHitByProjectile,  42);
                    }
                    BlockPos mutableSkyPos = new BlockPos.MutableBlockPos(realHitResult.getLocation().x, realHitResult.getLocation().y + 1.5, realHitResult.getLocation().z);
                    int maxFallHeight = 15;
                    int k = 0;
                    while(mutableSkyPos.getY() < level.getMaxBuildHeight() && level.isEmptyBlock(mutableSkyPos) && k < maxFallHeight){
                        mutableSkyPos = mutableSkyPos.above();
                        k++;
                    }

                    int maxSpears = 20 + (humunguous <= 0 ? 0 : 10 * humunguous);
                    for(int j = 0; j < maxSpears; j++){
                        FrostmintSpearEntity frostmintSpearEntity = ACEntityRegistry.FROSTMINT_SPEAR.get().spawn((ServerLevel) level, BlockPos.containing(0, 0, 0), MobSpawnType.MOB_SUMMONED);
                        if(frostmintSpearEntity != null){
                            frostmintSpearEntity.pickup = FrostmintSpearEntity.Pickup.CREATIVE_ONLY;
                            frostmintSpearEntity.setBaseDamage(frostmintSpearEntity.getBaseDamage() * 2);

                            Vec3 vec3 = mutableSkyPos.getCenter().add(level.random.nextFloat() * 16 - 8, level.random.nextFloat() * 4 - 2, level.random.nextFloat() * 16 - 8);
                            int clearTries = 0;
                            while (clearTries < 6 && !level.isEmptyBlock(BlockPos.containing(vec3)) && level.getFluidState(BlockPos.containing(vec3)).isEmpty()) {
                                clearTries++;
                                vec3 = mutableSkyPos.getCenter().add(level.random.nextFloat() * 16 - 8, level.random.nextFloat() * 4 - 2, level.random.nextFloat() * 16 - 8);
                            }
                            if (!level.isEmptyBlock(BlockPos.containing(vec3)) && level.getFluidState(BlockPos.containing(vec3)).isEmpty()) {
                                vec3 = mutableSkyPos.getCenter();
                            }
                            frostmintSpearEntity.setPos(vec3);
                            Vec3 vec31 = realHitResult.getLocation().subtract(vec3);
                            frostmintSpearEntity.shoot(vec31.x, vec31.y, vec31.z, 0.5F + 1.5F * level.random.nextFloat(), level.random.nextFloat() * 10);
                            frostmintSpearEntity.getPersistentData().putBoolean("FrostRadiant", true);
                        }
                    }

                    SugarStaffHexEntity sugarStaffHexEntity = ACEntityRegistry.SUGAR_STAFF_HEX.get().create(player.level());
                    sugarStaffHexEntity.setOwner(player);
                    sugarStaffHexEntity.setPos(realHitResult.getLocation());
                    sugarStaffHexEntity.setHexScale(1.0F + 0.25F * (float) humunguous);
                    level.addFreshEntity(sugarStaffHexEntity);
                    level.playSound(null, player.blockPosition(), ACSoundRegistry.SUGAR_STAFF_CAST_HEX.get(), SoundSource.PLAYERS, 1.0F, 0.0F);
                    sugarStaffHexEntity.setLifespan(300 + 60 * itemstack.getEnchantmentLevel(ACEnchantmentRegistry.SPELL_LASTING.get()));
                    player.getCooldowns().addCooldown(this, 10);

                    if (!player.isCreative()) {
                        player.getOffhandItem().shrink(1);
                    }
                    player.getCooldowns().addCooldown(this, humunguous >= 2 ? 1400 : (humunguous == 1 ? 1200 : 1000));

                    level.playSound((Player) null, player.blockPosition(), (SoundEvent) ACSoundRegistry.SUGAR_STAFF_CAST_HEX.get(), SoundSource.PLAYERS, 1.0F, 1.0F);

                    ACEUtils.awardAdvancement(player,"radiant_wrath","powered");
                }else{

                    boolean seeking = itemstack.getEnchantmentLevel(ACEnchantmentRegistry.SEEKCANDY.get()) > 0;
                    boolean straight = itemstack.getEnchantmentLevel(ACEnchantmentRegistry.PEPPERMINT_PUNTING.get()) > 0;
                    int multipleMint = itemstack.getEnchantmentLevel(ACEnchantmentRegistry.MULTIPLE_MINT.get());

                    for (int layers = 1; layers < (multipleMint != 0 ? multipleMint + 2 : 2);layers++){
                        for (int l = 0; l < 5 * (2 + layers); l++) {
                            makeRadiantPeppermint(5 * (2 + layers), l, level, player, 12 / layers  != 0 ? (float) 12 / layers  : 1, 2.5F + layers - 1, 200, straight,seeking,lookingAtEntity);
                        }
                    }
                    if (!player.isCreative()) {
                        player.getOffhandItem().shrink(1);
                    }
                    player.getCooldowns().addCooldown(this, seeking ? (multipleMint >= 2 ? 800 : (multipleMint == 1 ? 700 : 600)) : (multipleMint >= 2 ? 600 : (multipleMint == 1 ? 500 : 400)));
                    level.playSound((Player) null, player.blockPosition(), (SoundEvent) ACSoundRegistry.SUGAR_STAFF_CAST_PEPPERMINT.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
                    ACEUtils.awardAdvancement(player,"radiant_wrath","powered");
                }
            } else {

                if (hex) {
                    Vec3 ground = ACMath.getGroundBelowPosition(player.level(), player.getEyePosition());
                    SugarStaffHexEntity sugarStaffHexEntity = (SugarStaffHexEntity) ((EntityType) ACEntityRegistry.SUGAR_STAFF_HEX.get()).create(player.level());
                    sugarStaffHexEntity.setOwner(player);
                    sugarStaffHexEntity.setPos(ground.x, ground.y, ground.z);
                    sugarStaffHexEntity.setHexScale(1.0F + 0.25F * (float) itemstack.getEnchantmentLevel((Enchantment) ACEnchantmentRegistry.HUMUNGOUS_HEX.get()));
                    level.addFreshEntity(sugarStaffHexEntity);
                    level.playSound((Player) null, player.blockPosition(), (SoundEvent) ACSoundRegistry.SUGAR_STAFF_CAST_HEX.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
                    sugarStaffHexEntity.setLifespan(100 + 60 * itemstack.getEnchantmentLevel((Enchantment) ACEnchantmentRegistry.SPELL_LASTING.get()));
                    player.getCooldowns().addCooldown(this, 100);

                    level.playSound((Player) null, player.blockPosition(), (SoundEvent) ACSoundRegistry.SUGAR_STAFF_CAST_PEPPERMINT.get(), SoundSource.PLAYERS, 1.0F, 1.0F);

                } else {
                    int spawnIn = 3 + itemstack.getEnchantmentLevel((Enchantment) ACEnchantmentRegistry.MULTIPLE_MINT.get());
                    int despawnTime = 80;

                    for (int i = 0; i < spawnIn; ++i) {
                        SpinningPeppermintEntity spinningPeppermintEntity = (SpinningPeppermintEntity) ((EntityType) ACEntityRegistry.SPINNING_PEPPERMINT.get()).create(player.level());
                        spinningPeppermintEntity.setPos(player.position().add((double) 0.0F, (double) (player.getBbHeight() * 0.45F), (double) 0.0F));
                        if (itemstack.getEnchantmentLevel((Enchantment) ACEnchantmentRegistry.PEPPERMINT_PUNTING.get()) > 0) {
                            spinningPeppermintEntity.setStraight(true);
                            spinningPeppermintEntity.setYRot(180.0F + player.getYHeadRot() + (float) ((i - 1) * 15));
                            despawnTime = 20;
                        } else {
                            spinningPeppermintEntity.setStraight(false);
                            spinningPeppermintEntity.setYRot((float) (180 + (i - 1) * 30));
                        }

                        if (lookingAtEntity != null && itemstack.getEnchantmentLevel((Enchantment) ACEnchantmentRegistry.SEEKCANDY.get()) > 0) {
                            spinningPeppermintEntity.setSeekingEntityId(lookingAtEntity.getId());
                            despawnTime = 50;
                        }

                        spinningPeppermintEntity.setSpinSpeed(7.0F);
                        spinningPeppermintEntity.setSpinRadius(3F);
                        spinningPeppermintEntity.setOwner(player);
                        spinningPeppermintEntity.setStartAngle((float) (i * 360) / (float) spawnIn);
                        spinningPeppermintEntity.setLifespan(80);
                        level.addFreshEntity(spinningPeppermintEntity);
                    }

                    level.playSound((Player) null, player.blockPosition(), (SoundEvent) ACSoundRegistry.SUGAR_STAFF_CAST_PEPPERMINT.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
                    player.getCooldowns().addCooldown(this, despawnTime);
                }
            }

            itemstack.hurtAndBreak(1, player, (player1) -> player1.broadcastBreakEvent(player1.getUsedItemHand()));
        }

        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }

    public void makeRadiantPeppermint(int amountOfPeppermint, float loopInt, Level level, LivingEntity living, float spinSpeed, float spinRadius, int setLifespan, boolean setStraight, boolean seeking, Entity lookingAtEntity){

        SpinningPeppermintEntity peppermint = ACEntityRegistry.SPINNING_PEPPERMINT.get().create(living.level());
        peppermint.setPos(living.position().add(0, living.getBbHeight() * 0.45F, 0));
        if (seeking) {
            peppermint.setSeekingEntityId(lookingAtEntity.getId());
        }
        peppermint.getPersistentData().putBoolean("PepperRadiant", true);
        peppermint.setStraight(setStraight);
        peppermint.setYRot(180 + (loopInt - 1) * 30);
        peppermint.setSpinSpeed(spinSpeed);
        peppermint.setSpinRadius(spinRadius);
        peppermint.setOwner(living);
        peppermint.setStartAngle(loopInt * 360 / (float) amountOfPeppermint);
        peppermint.setLifespan(setLifespan);

        level.addFreshEntity(peppermint);
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        if (pAttacker.getOffhandItem().is(ACItemRegistry.RADIANT_ESSENCE.get())){
            pTarget.addEffect(new MobEffectInstance(MobEffects.HUNGER,1500,9));
            if (pAttacker instanceof Player player && !player.isCreative()) {
                pAttacker.getOffhandItem().shrink(1);
                player.getCooldowns().addCooldown(this, 400);
            }
        }
        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }
}
