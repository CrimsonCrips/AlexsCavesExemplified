//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.crimsoncrips.alexscavesexemplified.server.blocks.cauldron;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;

public class PurpleSodaCauldronBlock extends ACExCauldron {


    public PurpleSodaCauldronBlock(Properties p_153498_) {
        super(p_153498_, ACExCauldronInteraction.PURPLE_SODA);
    }

    protected double getContentHeight(BlockState p_153500_) {
        return 0.9375D;
    }



    public void entityInside(BlockState p_153506_, Level p_153507_, BlockPos p_153508_, Entity entity) {
        if (this.isEntityInsideContent(p_153506_, p_153508_, entity) && AlexsCavesExemplified.COMMON_CONFIG.STICKY_SODA_ENABLED.get() && entity instanceof LivingEntity livingEntity) {
            livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 90, 0));
        }

    }

    public int getAnalogOutputSignal(BlockState p_153502_, Level p_153503_, BlockPos p_153504_) {
        return 3;
    }



}
