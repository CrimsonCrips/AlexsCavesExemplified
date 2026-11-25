package org.crimsoncrips.alexscavesexemplified.server.item;



import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.Items;
import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.crimsoncrips.alexscavesexemplified.backport.ByteBufCodecs;
import org.crimsoncrips.alexscavesexemplified.backport.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class IceCreamConeItem extends Item {

    private static final float MILK_CHANCE_BASE = 0.25F;
    private static final float MILK_CHANCE_PER_VANILLA = 0.25F;
    private static final float SATURATION = 0.4F;
    private static final Map<Formation, FoodProperties> FOODS = new ConcurrentHashMap<>();

    public IceCreamConeItem(Properties properties) {
        super(properties);
    }

    public enum Flavor implements StringRepresentable {
        VANILLA("vanilla", ACBlockRegistry.VANILLA_ICE_CREAM::get, 4, null, 0.0F),
        CHOCOLATE("chocolate", ACBlockRegistry.CHOCOLATE_ICE_CREAM::get, 6, MobEffects.DIG_SPEED, 10.0F),
        SWEETBERRY("sweetberry", ACBlockRegistry.SWEETBERRY_ICE_CREAM::get, 5, MobEffects.REGENERATION, 5.0F);

        public static final Codec<Flavor> CODEC = StringRepresentable.fromEnum(Flavor::values);
        public static final StreamCodec<ByteBuf, Flavor> STREAM_CODEC = ByteBufCodecs.idMapper(id -> values()[id], Flavor::ordinal);

        private final String name;
        private final Supplier<Block> block;
        private final int nutrition;
        @Nullable
        private final MobEffect effect;
        private final float effectSeconds;

        Flavor(String name, Supplier<Block> block, int nutrition, @Nullable MobEffect effect, float effectSeconds) {
            this.name = name;
            this.block = block;
            this.nutrition = nutrition;
            this.effect = effect;
            this.effectSeconds = effectSeconds;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }

        public MutableComponent displayName() {
            return Component.translatable("item." + AlexsCavesExemplified.MODID + ".ice_cream_cone.flavor." + this.name);
        }

        @Nullable
        public static Flavor of(Block block) {
            for (Flavor flavor : values()) {
                if (flavor.block.get() == block)
                    return flavor;
            }
            return null;
        }
    }

    public record Formation(Flavor top, Flavor middle, Flavor bottom) {
        public static final Codec<Formation> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Flavor.CODEC.fieldOf("top").forGetter(Formation::top),
                Flavor.CODEC.fieldOf("middle").forGetter(Formation::middle),
                Flavor.CODEC.fieldOf("bottom").forGetter(Formation::bottom)
        ).apply(instance, Formation::new));
        public static final StreamCodec<ByteBuf, Formation> STREAM_CODEC = StreamCodec.composite(
                Flavor.STREAM_CODEC, Formation::top,
                Flavor.STREAM_CODEC, Formation::middle,
                Flavor.STREAM_CODEC, Formation::bottom,
                Formation::new);

        public int modelIndex() {
            int count = Flavor.values().length;
            return this.top.ordinal() * count * count + this.middle.ordinal() * count + this.bottom.ordinal() + 1;
        }

        public int count(Flavor flavor) {
            return (this.top == flavor ? 1 : 0) + (this.middle == flavor ? 1 : 0) + (this.bottom == flavor ? 1 : 0);
        }

        public float milkChance() {
            int vanilla = this.count(Flavor.VANILLA);
            return vanilla == 0 ? 0.0F : Math.min(1.0F, MILK_CHANCE_BASE + MILK_CHANCE_PER_VANILLA * vanilla);
        }

        public FoodProperties food() {
            return FOODS.computeIfAbsent(this, Formation::buildFood);
        }

        private FoodProperties buildFood() {
            FoodProperties.Builder builder = new FoodProperties.Builder().nutrition(this.top.nutrition).saturationMod(SATURATION);
            for (Flavor flavor : Flavor.values()) {
                int scoops = this.count(flavor);
                if (flavor.effect != null && scoops > 0) {
                    int duration = ACExUtils.seconds(flavor.effectSeconds * scoops);
                    builder.effect(() -> new MobEffectInstance(flavor.effect, duration), 1.0F);
                }
            }
            return builder.build();
        }
    }

    @Nullable
    public static Formation getFormation(ItemStack stack) {
        return ACExDataComponents.ICE_CREAM_FORMATION.get().get(stack);
    }

    public static ItemStack withFormation(ItemStack stack, Formation formation) {
        ACExDataComponents.ICE_CREAM_FORMATION.get().set(stack, formation);
        return stack;
    }

    @Override
    public @Nullable FoodProperties getFoodProperties(ItemStack stack, @Nullable LivingEntity entity) {
        Formation formation = getFormation(stack);
        return formation == null ? super.getFoodProperties(stack, entity) : formation.food();
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        Formation formation = getFormation(stack);
        boolean milk = !level.isClientSide && formation != null && level.random.nextFloat() < formation.milkChance();
        ItemStack result = super.finishUsingItem(stack, level, entity);
        if (milk) {
            entity.curePotionEffects(new ItemStack(Items.MILK_BUCKET));
        }
        return result;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        Formation formation = getFormation(stack);
        if (formation != null) {
            tooltip.add(formation.top().displayName().withStyle(ChatFormatting.GRAY));
            tooltip.add(formation.middle().displayName().withStyle(ChatFormatting.GRAY));
            tooltip.add(formation.bottom().displayName().withStyle(ChatFormatting.GRAY));
        }
        FoodProperties food = this.getFoodProperties(stack, null);
        float milkChance = formation == null ? 0.0F : formation.milkChance();
        if (food == null || food.getEffects().isEmpty() && milkChance <= 0.0F)
            return;
        tooltip.add(CommonComponents.EMPTY);
        tooltip.add(Component.translatable("item." + AlexsCavesExemplified.MODID + ".ice_cream_cone.when_eaten").withStyle(ChatFormatting.DARK_PURPLE));
        for (Pair<MobEffectInstance, Float> possible : food.getEffects()) {
            MobEffectInstance instance = possible.getFirst();
            Component effect = Component.translatable("potion.withDuration", Component.translatable(instance.getDescriptionId()), MobEffectUtil.formatDuration(instance, 1.0F));
            tooltip.add(withChance(effect, possible.getSecond()).withStyle(instance.getEffect().getCategory().getTooltipFormatting()));
        }
        if (milkChance > 0.0F) {
            tooltip.add(withChance(Component.translatable("item." + AlexsCavesExemplified.MODID + ".ice_cream_cone.clears_effects"), milkChance).withStyle(ChatFormatting.BLUE));
        }
    }

    private static MutableComponent withChance(Component effect, float chance) {
        if (chance >= 1.0F)
            return effect.copy();
        return Component.translatable("item." + AlexsCavesExemplified.MODID + ".ice_cream_cone.chance", Math.round(chance * 100.0F), effect);
    }
}
