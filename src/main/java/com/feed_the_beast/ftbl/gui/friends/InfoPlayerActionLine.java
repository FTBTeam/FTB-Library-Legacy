package com.feed_the_beast.ftbl.gui.friends;

import com.feed_the_beast.ftbl.api.gui.GuiLM;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.api.gui.guibuttons.SidebarButton;
import com.feed_the_beast.ftbl.api.gui.widgets.ButtonLM;
import com.feed_the_beast.ftbl.api.info.IGuiInfoPageTree;
import com.feed_the_beast.ftbl.api.info.impl.ButtonInfoTextLine;
import com.feed_the_beast.ftbl.api.info.impl.EmptyInfoPageLine;
import com.feed_the_beast.ftbl.gui.GuiInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Created by LatvianModder on 23.03.2016.
 */
@SideOnly(Side.CLIENT)
public class InfoPlayerActionLine extends EmptyInfoPageLine
{
    public class ButtonInfoPlayerAction extends ButtonInfoTextLine
    {
        public ButtonInfoPlayerAction(GuiInfo g)
        {
            super(g, null);
            height = 18;
            ITextComponent c = action.getDisplayNameOverride();
            title = ((c == null) ? new TextComponentTranslation("sidebar_button." + actionID) : c).getFormattedText();
            width = (action.icon == null ? 8 : 24) + g.font.getStringWidth(title);
        }

        @Override
        public void addMouseOverText(@Nonnull GuiLM gui, @Nonnull List<String> l)
        {
        }

        @Override
        public void onClicked(@Nonnull GuiLM gui, @Nonnull IMouseButton button)
        {
            GuiLM.playClickSound();
            action.onClicked(button);
        }

        @Override
        public void renderWidget(@Nonnull GuiLM gui)
        {
            double ay = getAY();
            double ax = getAX();

            GlStateManager.enableBlend();

            if(gui.isMouseOver(this))
            {
                GlStateManager.color(1F, 1F, 1F, 0.2F);
                GuiLM.drawBlankRect(ax, ay, width, height);
            }

            GlStateManager.color(1F, 1F, 1F, 1F);

            action.render(gui.mc, ax + 1, ay + 1);
            action.postRender(gui.mc, ax + 1, ay + 1);

            gui.font.drawString(title, (int) ax + (action.icon == null ? 4 : 20), (int) ay + 5, ((GuiInfo) gui).colorText);
        }
    }

    public final ResourceLocation actionID;
    public final SidebarButton action;

    public InfoPlayerActionLine(ResourceLocation id, SidebarButton a)
    {
        actionID = id;
        action = a;
    }

    @Override
    @Nonnull
    @SideOnly(Side.CLIENT)
    public ButtonLM createWidget(GuiInfo gui, IGuiInfoPageTree page)
    {
        return new ButtonInfoPlayerAction(gui);
    }
}
