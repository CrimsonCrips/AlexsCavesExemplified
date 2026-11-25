package org.crimsoncrips.alexscavesexemplified.server.item;







import org.crimsoncrips.alexscavesexemplified.backport.ACExData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionResult;
import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;
import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.entity.item.*;
import com.github.alexmodguy.alexscaves.server.item.dispenser.FluidContainerDispenseItemBehavior;
import com.github.alexmodguy.alexscaves.server.potion.ACEffectRegistry;
import com.github.alexthe666.citadel.server.block.LecternBooks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.common.ForgeSpawnEggItem;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.registries.RegistryObject;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.server.blocks.ACExBlockRegistry;
import org.crimsoncrips.alexscavesexemplified.server.entity.ACExEntityRegistry;
import org.crimsoncrips.alexscavesexemplified.server.entity.GammaNuclearBombEntity;

public class ACExItemRegistry {
    public static final DeferredRegister<Item> DEF_REG = DeferredRegister.create(Registries.ITEM, AlexsCavesExemplified.MODID);

    public static final RegistryObject<Item> ICE_CREAM_CONE = DEF_REG.register("ice_cream_cone", () -> new IceCreamConeItem(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationMod(0.4F).effect(() -> new MobEffectInstance(ACEffectRegistry.SUGAR_RUSH.get(), ACExUtils.seconds(15.0F)), 0.04F).build())));
    public static final RegistryObject<Item> GAMMA_TREMORZILLA_SPAWN_EGG = DEF_REG.register("gamma_tremorzilla_spawn_egg", () -> new ForgeSpawnEggItem(() -> ACEntityRegistry.TREMORZILLA.get(), 0x0F1E3D, 0x4FE0FF, new Item.Properties()) {
        @Override
        public InteractionResult useOn(UseOnContext context) {
            applyGammaData(context.getItemInHand());
            return super.useOn(context);
        }

        @Override
        public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
            applyGammaData(player.getItemInHand(hand));
            return super.use(level, player, hand);
        }
    });
    public static final RegistryObject<Item> GUMWIRM_SPAWN_EGG = DEF_REG.register("gumwirm_spawn_egg", () -> new ForgeSpawnEggItem(ACExEntityRegistry.GUMWIRM, 9633753, 16753116, new Item.Properties()));
    public static final RegistryObject<Item> DINO_EGGS = DEF_REG.register("dino_eggs", () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));


    public static void setup() {
        DispenserBlock.registerBehavior(ACExBlockRegistry.GAMMA_NUCLEAR_BOMB.get(), new DefaultDispenseItemBehavior() {
            protected ItemStack execute(BlockSource blockSource, ItemStack itemStack) {
                Level level = blockSource.getLevel();
                BlockPos blockpos = blockSource.getPos().relative(blockSource.getBlockState().getValue(DispenserBlock.FACING));
                GammaNuclearBombEntity bomb = ACExEntityRegistry.GAMMA_NUCLEAR_BOMB.get().create(level);
                bomb.setPos((double)blockpos.getX() + (double)0.5F, (double)blockpos.getY(), (double)blockpos.getZ() + (double)0.5F);
                level.addFreshEntity(bomb);
                level.playSound((Player)null, bomb.getX(), bomb.getY(), bomb.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.gameEvent((Entity)null, GameEvent.ENTITY_PLACE, blockpos);
                itemStack.shrink(1);
                return itemStack;

            }
        });
    }

    private static void applyGammaData(ItemStack stack) {
        CompoundTag entityTag = stack.getOrCreateTagElement("EntityTag");
        CompoundTag attachments = entityTag.getCompound(ACExData.TAG);
        attachments.putBoolean(AlexsCavesExemplified.MODID + ":gamma", true);
        entityTag.put(ACExData.TAG, attachments);
    }
}
