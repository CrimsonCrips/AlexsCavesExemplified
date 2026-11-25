package org.crimsoncrips.alexscavesexemplified.backport;

import com.mojang.datafixers.util.Function3;
import com.mojang.datafixers.util.Function4;
import com.mojang.datafixers.util.Function5;
import com.mojang.datafixers.util.Function6;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface StreamCodec<B, V> {

    V decode(B buf);

    void encode(B buf, V value);

    interface CodecOperation<B, S, T> {
        StreamCodec<B, T> apply(StreamCodec<B, S> codec);
    }

    default <O> StreamCodec<B, O> apply(CodecOperation<B, V, O> operation) {
        return operation.apply(this);
    }

    static <B, V> StreamCodec<B, V> of(BiConsumer<B, V> encoder, Function<B, V> decoder) {
        return new StreamCodec<>() {
            @Override
            public V decode(B buf) {
                return decoder.apply(buf);
            }

            @Override
            public void encode(B buf, V value) {
                encoder.accept(buf, value);
            }
        };
    }

    static <B, V> StreamCodec<B, V> ofMember(BiConsumer<V, B> encoder, Function<B, V> decoder) {
        return of((buf, value) -> encoder.accept(value, buf), decoder);
    }

    static <B, V> StreamCodec<B, V> unit(V instance) {
        return of((buf, value) -> {
        }, buf -> instance);
    }

    default <O> StreamCodec<B, O> map(Function<? super V, ? extends O> to, Function<? super O, ? extends V> from) {
        StreamCodec<B, V> self = this;
        return of((buf, value) -> self.encode(buf, from.apply(value)), buf -> to.apply(self.decode(buf)));
    }

    @SuppressWarnings("unchecked")
    default <S extends B> StreamCodec<S, V> cast() {
        return (StreamCodec<S, V>) this;
    }

    static <B, C, T1> StreamCodec<B, C> composite(StreamCodec<? super B, T1> c1, Function<C, T1> g1, Function<T1, C> factory) {
        return of((buf, value) -> c1.encode(buf, g1.apply(value)), buf -> factory.apply(c1.decode(buf)));
    }

    static <B, C, T1, T2> StreamCodec<B, C> composite(StreamCodec<? super B, T1> c1, Function<C, T1> g1, StreamCodec<? super B, T2> c2, Function<C, T2> g2, BiFunction<T1, T2, C> factory) {
        return of((buf, value) -> {
            c1.encode(buf, g1.apply(value));
            c2.encode(buf, g2.apply(value));
        }, buf -> {
            T1 t1 = c1.decode(buf);
            T2 t2 = c2.decode(buf);
            return factory.apply(t1, t2);
        });
    }

    static <B, C, T1, T2, T3> StreamCodec<B, C> composite(StreamCodec<? super B, T1> c1, Function<C, T1> g1, StreamCodec<? super B, T2> c2, Function<C, T2> g2, StreamCodec<? super B, T3> c3, Function<C, T3> g3, Function3<T1, T2, T3, C> factory) {
        return of((buf, value) -> {
            c1.encode(buf, g1.apply(value));
            c2.encode(buf, g2.apply(value));
            c3.encode(buf, g3.apply(value));
        }, buf -> {
            T1 t1 = c1.decode(buf);
            T2 t2 = c2.decode(buf);
            T3 t3 = c3.decode(buf);
            return factory.apply(t1, t2, t3);
        });
    }

    static <B, C, T1, T2, T3, T4> StreamCodec<B, C> composite(StreamCodec<? super B, T1> c1, Function<C, T1> g1, StreamCodec<? super B, T2> c2, Function<C, T2> g2, StreamCodec<? super B, T3> c3, Function<C, T3> g3, StreamCodec<? super B, T4> c4, Function<C, T4> g4, Function4<T1, T2, T3, T4, C> factory) {
        return of((buf, value) -> {
            c1.encode(buf, g1.apply(value));
            c2.encode(buf, g2.apply(value));
            c3.encode(buf, g3.apply(value));
            c4.encode(buf, g4.apply(value));
        }, buf -> {
            T1 t1 = c1.decode(buf);
            T2 t2 = c2.decode(buf);
            T3 t3 = c3.decode(buf);
            T4 t4 = c4.decode(buf);
            return factory.apply(t1, t2, t3, t4);
        });
    }

    static <B, C, T1, T2, T3, T4, T5> StreamCodec<B, C> composite(StreamCodec<? super B, T1> c1, Function<C, T1> g1, StreamCodec<? super B, T2> c2, Function<C, T2> g2, StreamCodec<? super B, T3> c3, Function<C, T3> g3, StreamCodec<? super B, T4> c4, Function<C, T4> g4, StreamCodec<? super B, T5> c5, Function<C, T5> g5, Function5<T1, T2, T3, T4, T5, C> factory) {
        return of((buf, value) -> {
            c1.encode(buf, g1.apply(value));
            c2.encode(buf, g2.apply(value));
            c3.encode(buf, g3.apply(value));
            c4.encode(buf, g4.apply(value));
            c5.encode(buf, g5.apply(value));
        }, buf -> {
            T1 t1 = c1.decode(buf);
            T2 t2 = c2.decode(buf);
            T3 t3 = c3.decode(buf);
            T4 t4 = c4.decode(buf);
            T5 t5 = c5.decode(buf);
            return factory.apply(t1, t2, t3, t4, t5);
        });
    }

    static <B, C, T1, T2, T3, T4, T5, T6> StreamCodec<B, C> composite(StreamCodec<? super B, T1> c1, Function<C, T1> g1, StreamCodec<? super B, T2> c2, Function<C, T2> g2, StreamCodec<? super B, T3> c3, Function<C, T3> g3, StreamCodec<? super B, T4> c4, Function<C, T4> g4, StreamCodec<? super B, T5> c5, Function<C, T5> g5, StreamCodec<? super B, T6> c6, Function<C, T6> g6, Function6<T1, T2, T3, T4, T5, T6, C> factory) {
        return of((buf, value) -> {
            c1.encode(buf, g1.apply(value));
            c2.encode(buf, g2.apply(value));
            c3.encode(buf, g3.apply(value));
            c4.encode(buf, g4.apply(value));
            c5.encode(buf, g5.apply(value));
            c6.encode(buf, g6.apply(value));
        }, buf -> {
            T1 t1 = c1.decode(buf);
            T2 t2 = c2.decode(buf);
            T3 t3 = c3.decode(buf);
            T4 t4 = c4.decode(buf);
            T5 t5 = c5.decode(buf);
            T6 t6 = c6.decode(buf);
            return factory.apply(t1, t2, t3, t4, t5, t6);
        });
    }
}
