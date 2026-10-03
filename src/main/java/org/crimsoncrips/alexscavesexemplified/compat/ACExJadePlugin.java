package org.crimsoncrips.alexscavesexemplified.compat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.client.tooltip.ACExGammaText;
import org.crimsoncrips.alexscavesexemplified.server.blocks.ACExGammaNuke;
import org.crimsoncrips.alexscavesexemplified.server.entity.GammaNuclearBombEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.JadeIds;
import snownee.jade.api.WailaPlugin;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.Element;
import snownee.jade.api.ui.IElement;

import java.util.List;

@WailaPlugin(AlexsCavesExemplified.MODID)
public class ACExJadePlugin implements IWailaPlugin {

    private static final ResourceLocation GAMMA_NAME = AlexsCavesExemplified.prefix("gamma_name");

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(GammaBlockName.INSTANCE, ACExGammaNuke.class);
        registration.registerEntityComponent(GammaEntityName.INSTANCE, GammaNuclearBombEntity.class);
    }

    private static void replaceName(ITooltip tooltip, Component name) {
        tooltip.replace(JadeIds.CORE_OBJECT_NAME, lines -> {
            for (List<IElement> line : lines) {
                line.replaceAll(element -> new GammaNameElement(name).tag(element.getTag()));
            }
            return lines;
        });
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
