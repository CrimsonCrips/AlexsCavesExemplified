package org.crimsoncrips.alexscavesexemplified.server.item;

import com.mojang.serialization.Codec;
import net.minecraft.core.UUIDUtil;
import net.minecraft.util.Unit;
import org.crimsoncrips.alexscavesexemplified.backport.ByteBufCodecs;
import org.crimsoncrips.alexscavesexemplified.backport.DataComponentType;
import org.crimsoncrips.alexscavesexemplified.backport.StreamCodec;

import java.util.UUID;

public class ACExDataComponents {

    public static final DataComponentType<UUID> LINKED_SUBMARINE = DataComponentType.register("linked_submarine",
            () -> DataComponentType.<UUID>builder().persistent(UUIDUtil.CODEC).networkSynchronized(ByteBufCodecs.UUID).build());

    public static final DataComponentType<Long> STAFF_CAST = DataComponentType.register("staff_cast",
            () -> DataComponentType.<Long>builder().networkSynchronized(ByteBufCodecs.VAR_LONG).build());

    public static final DataComponentType<Unit> IRRADIATED = DataComponentType.register("irradiated",
            () -> DataComponentType.<Unit>builder().persistent(Codec.unit(Unit.INSTANCE)).networkSynchronized(StreamCodec.unit(Unit.INSTANCE)).build());

    public static final DataComponentType<IceCreamConeItem.Formation> ICE_CREAM_FORMATION = DataComponentType.register("ice_cream_formation",
            () -> DataComponentType.<IceCreamConeItem.Formation>builder().persistent(IceCreamConeItem.Formation.CODEC).networkSynchronized(IceCreamConeItem.Formation.STREAM_CODEC).build());
}
