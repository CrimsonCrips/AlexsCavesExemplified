package org.crimsoncrips.alexscavesexemplified.misc;

import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.Mth;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.core.component.DataComponents;
import com.github.alexmodguy.alexscaves.server.entity.item.GumballEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.server.ACExAttachments;

public class ACExBuckshot {

    public static final int PELLETS = 8;
    public static final float SPREAD = 10.0F;
    private static final float MAX_DAMAGE = 3.0F;
    private static final float MIN_DAMAGE = 0.6F;
    private static final double FULL_DAMAGE_RANGE = 3.0D;
    private static final double MIN_DAMAGE_RANGE = 12.0D;

    public static boolean enabled() {
        return AlexsCavesExemplified.COMMON_CONFIG.BUCKSHOT_GUM_ENABLED.get() && !AlexsCavesExemplified.COMMON_CONFIG.SHOTNUKE_ENABLED.get();
    }

    public static void markPellet(GumballEntity gumball, Entity shooter) {
        gumball.setData(ACExAttachments.PELLET_ORIGIN, shooter.getEyePosition());
        gumball.setDamage(MAX_DAMAGE);
    }

    public static boolean isPellet(GumballEntity gumball) {
        return gumball.hasData(ACExAttachments.PELLET_ORIGIN);
    }

    public static float falloffDamage(GumballEntity gumball, Entity target) {
        double distance = gumball.getData(ACExAttachments.PELLET_ORIGIN).distanceTo(target.position());
        double t = (distance - FULL_DAMAGE_RANGE) / (MIN_DAMAGE_RANGE - FULL_DAMAGE_RANGE);
        t = Math.max(0.0D, Math.min(1.0D, t));
        return (float) (MAX_DAMAGE + (MIN_DAMAGE - MAX_DAMAGE) * t);
    }

    public static void fireExtraPellets(Level level, LivingEntity shooter, boolean leftHand, boolean ricochet, boolean split, int maximumBounces) {
        for (int i = 2; i < PELLETS; i++) {
            GumballEntity pellet = new GumballEntity(level, shooter);
            float sideOffset = (i % 2 == 0 ? 0.15F : -0.15F) + (leftHand ? 0.35F : -0.35F);
            Vec3 relativePos = new Vec3(sideOffset, 0.0D, 0.75D)
                    .xRot(-shooter.getXRot() * (float) (Math.PI / 180.0))
                    .yRot(-shooter.getYHeadRot() * (float) (Math.PI / 180.0));
            pellet.setPos(shooter.position().add(0.0D, shooter.getBbHeight() * 0.8F, 0.0D).add(relativePos));
            pellet.setTargetsOnBounce(ricochet);
            pellet.setSplitsOnHit(split);
            pellet.shootFromRotation(shooter, shooter.getXRot(), shooter.getYRot(), 0.0F, 1.5F, SPREAD);
            pellet.setMaximumBounces(maximumBounces);
            markPellet(pellet, shooter);
            level.addFreshEntity(pellet);
        }
    }

    private static final double BLAST_BACK = 0.7D;
    private static final double BLAST_LIFT = 0.25D;

    public static boolean knockbackEnabled() {
        return AlexsCavesExemplified.COMMON_CONFIG.SHOTGUM_KNOCKBACK_ENABLED.get();
    }

    public static void blastBack(LivingEntity shooter) {
        Vec3 back = Vec3.directionFromRotation(0.0F, shooter.getYRot()).scale(-BLAST_BACK);
        shooter.setDeltaMovement(back.x, Math.max(shooter.getDeltaMovement().y, 0.0D) + BLAST_LIFT, back.z);
        shooter.hasImpulse = true;
        shooter.hurtMarked = true;
    }

    public static final int RECOIL_TICKS = ACExUtils.seconds(0.8F);
    private static final float PUMP_START = ACExUtils.seconds(0.2F);
    private static final float PUMP_BACK_END = ACExUtils.seconds(0.5F);
    public static final int PUMP_SOUND_TICK = RECOIL_TICKS - (int) PUMP_START;
    private static final String RECOIL = "ACExRecoil";
    private static final String PREV_RECOIL = "ACExPrevRecoil";

    public static int getRecoil(ItemStack stack) {
        return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getInt(RECOIL);
    }

    public static void setRecoil(ItemStack stack, int recoil, int prevRecoil) {
        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> {
            tag.putInt(RECOIL, recoil);
            tag.putInt(PREV_RECOIL, prevRecoil);
        });
    }

    public static int tickRecoil(ItemStack stack) {
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        int recoil = tag.getInt(RECOIL);
        if (recoil <= 0 && tag.getInt(PREV_RECOIL) <= 0)
            return 0;
        int next = Math.max(0, recoil - 1);
        setRecoil(stack, next, recoil);
        return next;
    }

    public static float shotAge(ItemStack stack, float partialTick) {
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        float recoil = Mth.lerp(partialTick, tag.getInt(PREV_RECOIL), tag.getInt(RECOIL));
        return recoil <= 0.0F ? RECOIL_TICKS : RECOIL_TICKS - recoil;
    }

    public static float pump(float age) {
        if (age < PUMP_START || age >= RECOIL_TICKS)
            return 0.0F;
        float t = age < PUMP_BACK_END ? (age - PUMP_START) / (PUMP_BACK_END - PUMP_START) : 1.0F - (age - PUMP_BACK_END) / (RECOIL_TICKS - PUMP_BACK_END);
        return t * t * (3.0F - 2.0F * t);
    }

    public static float pumpCrank(float age) {
        if (age < PUMP_START)
            return 0.0F;
        return Math.min(1.0F, (age - PUMP_START) / (RECOIL_TICKS - PUMP_START)) * 180.0F;
    }
}
