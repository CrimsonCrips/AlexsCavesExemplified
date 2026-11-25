package org.crimsoncrips.alexscavesexemplified.server;


import org.crimsoncrips.alexscavesexemplified.backport.ACExData;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.github.alexmodguy.alexscaves.server.entity.util.DeepOneReaction;
import com.github.alexmodguy.alexscaves.server.level.storage.ACWorldData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.event.RegisterCommandsEvent;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.server.item.ACExSugarStaffAbilities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class ACExCommands {

    public static void register(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal(AlexsCavesExemplified.MODID)
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("max_sugar_staff").executes(ACExCommands::maxSugarStaff))
                .then(Commands.literal("sweet_release")
                        .executes(context -> sweetRelease(context, List.of(context.getSource().getEntityOrException())))
                        .then(Commands.argument("targets", EntityArgument.entities())
                                .executes(context -> sweetRelease(context, EntityArgument.getEntities(context, "targets")))))
                .then(Commands.literal("deep_reputation")
                        .executes(context -> deepReputation(context, List.of(context.getSource().getPlayerOrException())))
                        .then(Commands.argument("targets", EntityArgument.players())
                                .executes(context -> deepReputation(context, EntityArgument.getPlayers(context, "targets")))))
                .then(Commands.literal("max_sectumsempra")
                        .executes(context -> maxSectumsempra(context, List.of(context.getSource().getPlayerOrException())))
                        .then(Commands.argument("targets", EntityArgument.players())
                                .executes(context -> maxSectumsempra(context, EntityArgument.getPlayers(context, "targets")))))
                .then(Commands.literal("radiant_charges")
                        .then(Commands.argument("amount", IntegerArgumentType.integer(0))
                                .executes(context -> setRadiantCharges(context, List.of(context.getSource().getPlayerOrException())))
                                .then(Commands.argument("targets", EntityArgument.players())
                                        .executes(context -> setRadiantCharges(context, EntityArgument.getPlayers(context, "targets")))))));
    }

    private static int sweetRelease(CommandContext<CommandSourceStack> context, Collection<? extends Entity> targets) {
        LivingEntity source = context.getSource().getEntity() instanceof LivingEntity living ? living : null;
        List<LivingEntity> started = new ArrayList<>();
        for (Entity entity : targets) {
            if (entity instanceof LivingEntity target && ACExSugarStaffAbilities.startSweetRelease(target, target == source ? null : source, false)) {
                started.add(target);
            }
        }
        if (started.isEmpty()) {
            context.getSource().sendFailure(Component.translatable("misc.alexscavesexemplified.sweet_release_failed"));
            return 0;
        }
        if (started.size() == 1) {
            Component name = started.get(0).getDisplayName();
            context.getSource().sendSuccess(() -> Component.translatable("misc.alexscavesexemplified.sweet_release_started", name), true);
        } else {
            context.getSource().sendSuccess(() -> Component.translatable("misc.alexscavesexemplified.sweet_release_started_multiple", started.size()), true);
        }
        return started.size();
    }

    private static int deepReputation(CommandContext<CommandSourceStack> context, Collection<ServerPlayer> players) {
        ACWorldData worldData = ACWorldData.get(context.getSource().getLevel());
        if (worldData == null) {
            context.getSource().sendFailure(Component.translatable("misc.alexscavesexemplified.deep_reputation_failed"));
            return 0;
        }
        for (ServerPlayer player : players) {
            int reputation = worldData.getDeepOneReputation(player.getUUID());
            Component reaction = Component.translatable("misc.alexscavesexemplified.deep_reaction_" + DeepOneReaction.fromReputation(reputation).toString().toLowerCase(Locale.ROOT));
            Component name = player.getDisplayName();
            context.getSource().sendSuccess(() -> Component.translatable("misc.alexscavesexemplified.deep_reputation", name, reputation, reaction), false);
        }
        return players.size();
    }

    private static int maxSectumsempra(CommandContext<CommandSourceStack> context, Collection<ServerPlayer> players) {
        for (ServerPlayer player : players) {
            ACExAttachments.set(player, ACExAttachments.SECTUMSEMPRA_CHARGE, 1.0F);
        }
        if (players.size() == 1) {
            Component name = players.iterator().next().getDisplayName();
            context.getSource().sendSuccess(() -> Component.translatable("misc.alexscavesexemplified.max_sectumsempra", name), true);
        } else {
            context.getSource().sendSuccess(() -> Component.translatable("misc.alexscavesexemplified.max_sectumsempra_multiple", players.size()), true);
        }
        return players.size();
    }

    private static int setRadiantCharges(CommandContext<CommandSourceStack> context, Collection<ServerPlayer> players) {
        int amount = IntegerArgumentType.getInteger(context, "amount");
        for (ServerPlayer player : players) {
            ACExData.set(player, ACExAttachments.RADIANT_METER, amount);
        }
        if (players.size() == 1) {
            Component name = players.iterator().next().getDisplayName();
            context.getSource().sendSuccess(() -> Component.translatable("misc.alexscavesexemplified.radiant_charges_set", name, amount), true);
        } else {
            context.getSource().sendSuccess(() -> Component.translatable("misc.alexscavesexemplified.radiant_charges_set_multiple", players.size(), amount), true);
        }
        return players.size();
    }
    

    private static int maxSugarStaff(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        boolean holding = player.getMainHandItem().is(ACItemRegistry.SUGAR_STAFF.get());
        ItemStack staff = holding ? player.getMainHandItem() : new ItemStack(ACItemRegistry.SUGAR_STAFF.get());
        for (var key : ACExSugarStaffAbilities.STAFF_ENCHANTMENTS) {
            if (key.isPresent()) {
                staff.enchant(key.get(), key.get().getMaxLevel());
            }
        }
        if (!holding && !player.getInventory().add(staff)) {
            player.drop(staff, false);
        }
        player.swing(InteractionHand.MAIN_HAND, true);
        context.getSource().sendSuccess(() -> Component.translatable("misc.alexscavesexemplified.max_sugar_staff"), false);
        return 1;
    }
}
