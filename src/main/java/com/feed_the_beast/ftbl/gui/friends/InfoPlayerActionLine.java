package com.feed_the_beast.ftbl.gui.friends;

import com.feed_the_beast.ftbl.api.ForgePlayerSP;
import com.feed_the_beast.ftbl.api.MouseButton;
import com.feed_the_beast.ftbl.api.client.gui.GuiLM;
import com.feed_the_beast.ftbl.api.client.gui.guibuttons.ActionButton;
import com.feed_the_beast.ftbl.api.client.gui.widgets.ButtonLM;
import com.feed_the_beast.ftbl.api.info.IGuiInfoPageTree;
import com.feed_the_beast.ftbl.api.info.impl.ButtonInfoTextLine;
import com.feed_the_beast.ftbl.api.info.impl.EmptyInfoPageLine;
import com.feed_the_beast.ftbl.gui.GuiInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
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
            title = action.getDisplayName(actionID).getFormattedText();
            width = (action.icon == null ? 8 : 24) + g.font.getStringWidth(title);
        }

        @Override
        public void addMouseOverText(@Nonnull GuiLM gui, @Nonnull List<String> l)
        {
        }

        @Override
        public void onClicked(@Nonnull GuiLM gui, @Nonnull MouseButton button)
        {
            GuiLM.playClickSound();
            action.onClicked(playerLM);
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

    public final ForgePlayerSP playerLM;
    public final ResourceLocation actionID;
    public final ActionButton action;

    public InfoPlayerActionLine(ForgePlayerSP p, ResourceLocation id, ActionButton a)
    {
        playerLM = p;
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
