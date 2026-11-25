package org.crimsoncrips.alexscavesexemplified.mixins.backport;

import com.mojang.math.Transformation;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Display.class)
public interface ACExDisplayAccessor {

    @Accessor("DATA_INTERPOLATION_START_DELTA_TICKS_ID")
    static EntityDataAccessor<Integer> getInterpolationStartId() {
        throw new AssertionError();
    }

    @Invoker("setTransformation")
    void invokeSetTransformation(Transformation transformation);

    @Invoker("setInterpolationDuration")
    void invokeSetInterpolationDuration(int duration);
}
