package org.crimsoncrips.alexscavesexemplified.compat;


import org.crimsoncrips.alexscavesexemplified.backport.ACExData;
import java.util.UUID;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IServerDataProvider;
import org.crimsoncrips.alexscavesexemplified.server.ACExAttachments;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.MinecraftServer;
import net.minecraft.nbt.CompoundTag;
import com.github.alexmodguy.alexscaves.server.entity.living.MineGuardianEntity;
import com.github.alexmodguy.alexscaves.server.block.AmberMonolithBlock;
import com.github.alexmodguy.alexscaves.server.block.blockentity.AmberMonolithBlockEntity;
import net.minecraft.util.StringUtil;
import org.crimsoncrips.alexscavesexemplified.misc.ACExAmberMonolith;
import org.crimsoncrips.alexscavesexemplified.mixins.blocks.ACExAmberMonolithAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.client.tooltip.ACExGammaText;
import org.crimsoncrips.alexscavesexemplified.server.blocks.ACExGammaNuke;
import org.crimsoncrips.alexscavesexemplified.server.blocks.PreservedAmberBlock;
import org.crimsoncrips.alexscavesexemplified.server.blocks.PreservedCreature;
import org.crimsoncrips.alexscavesexemplified.server.entity.GammaNuclearBombEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.Identifiers;
import snownee.jade.api.WailaPlugin;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.Element;
import snownee.jade.api.ui.IElement;

import java.util.List;

@WailaPlugin(AlexsCavesExemplified.MODID)
public class ACExJadePlugin implements IWailaPlugin {

    private static final ResourceLocation GAMMA_NAME = AlexsCavesExemplified.prefix("gamma_name");
    private static final ResourceLocation MINE_GUARDIAN_OWNER = AlexsCavesExemplified.prefix("mine_guardian_owner");
    private static final ResourceLocation PRESERVED_CREATURE = AlexsCavesExemplified.prefix("preserved_creature");
    private static final ResourceLocation MONOLITH_SUMMON = AlexsCavesExemplified.prefix("monolith_summon");
    private static final String OWNER_NAME = "ACExOwnerName";
    private static final String SUMMON_TICKS = "ACExSummonTicks";

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerEntityDataProvider(MineGuardianOwner.INSTANCE, MineGuardianEntity.class);
        registration.registerBlockDataProvider(MonolithSummon.INSTANCE, AmberMonolithBlockEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(GammaBlockName.INSTANCE, ACExGammaNuke.class);
        registration.registerBlockComponent(PreservedAmberCreature.INSTANCE, PreservedAmberBlock.class);
        registration.registerBlockComponent(MonolithSummon.INSTANCE, AmberMonolithBlock.class);
        registration.registerEntityComponent(GammaEntityName.INSTANCE, GammaNuclearBombEntity.class);
        registration.registerEntityComponent(MineGuardianOwner.INSTANCE, MineGuardianEntity.class);
    }

    private static void replaceName(ITooltip tooltip, Component name) {
        if (tooltip.get(Identifiers.CORE_OBJECT_NAME).isEmpty())
            return;
        tooltip.remove(Identifiers.CORE_OBJECT_NAME);
        tooltip.add(0, new GammaNameElement(name).tag(Identifiers.CORE_OBJECT_NAME));
    }

    private enum GammaBlockName implements IBlockComponentProvider {
        INSTANCE;

        @Override
        public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
            replaceName(tooltip, accessor.getBlock().getName());
        }

        @Override
        public ResourceLocation getUid() {
            return GAMMA_NAME;
        }
    }

    private enum PreservedAmberCreature implements IBlockComponentProvider {
        INSTANCE;

        @Override
        public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
            PreservedCreature creature = accessor.getBlockState().getValue(PreservedAmberBlock.CREATURE);
            if (creature.isAvailable()) {
                tooltip.add(Component.translatable("block." + AlexsCavesExemplified.MODID + ".preserved_amber.contains", creature.getDisplayName()));
            }
        }

        @Override
        public ResourceLocation getUid() {
            return PRESERVED_CREATURE;
        }
    }

    private enum MonolithSummon implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
        INSTANCE;

        @Override
        public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
            if (!(accessor.getBlockEntity() instanceof AmberMonolithBlockEntity monolith) || ((ACExAmberMonolithAccessor) monolith).getSpawnType() == null)
                return;
            ACExAmberMonolithAccessor summon = (ACExAmberMonolithAccessor) monolith;
            tooltip.add(Component.translatable("misc." + AlexsCavesExemplified.MODID + ".monolith_creature", summon.getSpawnType().getDescription(), summon.getSpawnCount()));
            int ticks = accessor.getServerData().contains(SUMMON_TICKS) ? accessor.getServerData().getInt(SUMMON_TICKS) : ACExAmberMonolith.ticksUntilSummon(monolith);
            if (ticks > 0) {
                tooltip.add(Component.translatable("misc." + AlexsCavesExemplified.MODID + ".monolith_summon", StringUtil.formatTickDuration(ticks)));
            } else {
                tooltip.add(Component.translatable("misc." + AlexsCavesExemplified.MODID + ".monolith_summoning"));
            }
        }

        @Override
        public void appendServerData(CompoundTag data, BlockAccessor accessor) {
            if (accessor.getBlockEntity() instanceof AmberMonolithBlockEntity monolith) {
                data.putInt(SUMMON_TICKS, ACExAmberMonolith.ticksUntilSummon(monolith));
            }
        }

        @Override
        public ResourceLocation getUid() {
            return MONOLITH_SUMMON;
        }
    }

    private enum GammaEntityName implements IEntityComponentProvider {
        INSTANCE;

        @Override
        public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
            replaceName(tooltip, accessor.getEntity().getName());
        }

        @Override
        public ResourceLocation getUid() {
            return GAMMA_NAME;
        }
    }

    private enum MineGuardianOwner implements IEntityComponentProvider, IServerDataProvider<EntityAccessor> {
        INSTANCE;

        @Override
        public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
            if (!AlexsCavesExemplified.COMMON_CONFIG.REMINEDING_ENABLED.get())
                return;
            String owner = ACExData.get(accessor.getEntity(), ACExAttachments.MINE_GUARDIAN_OWNER);
            Component name;
            if (owner.equals("-1")) {
                name = Component.translatable("misc.alexscavesexemplified.mine_guardian_owner_none");
            } else if (accessor.getServerData().contains(OWNER_NAME)) {
                name = Component.literal(accessor.getServerData().getString(OWNER_NAME));
            } else {
                name = Component.literal(owner);
            }
            tooltip.add(Component.translatable("misc.alexscavesexemplified.mine_guardian_owner", name));
        }

        @Override
        public void appendServerData(CompoundTag data, EntityAccessor accessor) {
            String owner = ACExData.get(accessor.getEntity(), ACExAttachments.MINE_GUARDIAN_OWNER);
            MinecraftServer server = accessor.getLevel().getServer();
            if (owner.equals("-1") || server == null)
                return;
            try {
                UUID uuid = UUID.fromString(owner);
                Player online = server.getPlayerList().getPlayer(uuid);
                if (online != null) {
                    data.putString(OWNER_NAME, online.getGameProfile().getName());
                } else if (server.getProfileCache() != null) {
                    server.getProfileCache().get(uuid).ifPresent(profile -> data.putString(OWNER_NAME, profile.getName()));
                }
            } catch (IllegalArgumentException ignored) {
            }
        }

        @Override
        public ResourceLocation getUid() {
            return MINE_GUARDIAN_OWNER;
        }
    }

    private static class GammaNameElement extends Element {
        private final Component text;

        GammaNameElement(Component text) {
            this.text = text;
        }

        @Override
        public Vec2 getSize() {
            return new Vec2(Minecraft.getInstance().font.width(text) + ACExGammaText.SWAY * 2, Minecraft.getInstance().font.lineHeight);
        }

        @Override
        public void render(GuiGraphics guiGraphics, float x, float y, float maxX, float maxY) {
            ACExGammaText.draw(guiGraphics, Minecraft.getInstance().font, text, (int) x + ACExGammaText.SWAY, (int) y, 255);
        }
    }
}
