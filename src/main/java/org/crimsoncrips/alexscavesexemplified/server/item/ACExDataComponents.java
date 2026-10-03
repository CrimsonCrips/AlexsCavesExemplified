package org.crimsoncrips.alexscavesexemplified.server.item;

import net.minecraft.core.UUIDUtil;
import java.util.UUID;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Unit;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;

public class ACExDataComponents {

    public static final DeferredRegister<DataComponentType<?>> DEF_REG = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, AlexsCavesExemplified.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<UUID>> LINKED_SUBMARINE = DEF_REG.register("linked_submarine",
            () -> DataComponentType.<UUID>builder().persistent(UUIDUtil.CODEC).networkSynchronized(UUIDUtil.STREAM_CODEC).build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Long>> STAFF_CAST = DEF_REG.register("staff_cast",
            () -> DataComponentType.<Long>builder().networkSynchronized(ByteBufCodecs.VAR_LONG).build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Unit>> IRRADIATED = DEF_REG.register("irradiated",
            () -> DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(StreamCodec.unit(Unit.INSTANCE)).build());
}
