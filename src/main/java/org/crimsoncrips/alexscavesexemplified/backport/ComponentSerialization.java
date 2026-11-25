package org.crimsoncrips.alexscavesexemplified.backport;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;

public class ComponentSerialization {
    public static final StreamCodec<ByteBuf, Component> STREAM_CODEC = ByteBufCodecs.COMPONENT;
}
