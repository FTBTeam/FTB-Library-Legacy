package com.feed_the_beast.ftbl.gui.friends;

import com.feed_the_beast.ftbl.api.ForgePlayerSP;
import com.feed_the_beast.ftbl.api.MouseButton;
import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.api.client.gui.GuiLM;
import com.feed_the_beast.ftbl.api.client.gui.guibuttons.ActionButton;
import com.feed_the_beast.ftbl.api.info.InfoPage;
import com.feed_the_beast.ftbl.api.info.InfoTextLine;
import com.feed_the_beast.ftbl.gui.info.ButtonInfoTextLine;
import com.feed_the_beast.ftbl.gui.info.GuiInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * Created by LatvianModder on 23.03.2016.
 */
@SideOnly(Side.CLIENT)
public class InfoPlayerActionLine extends InfoTextLine
{
    public class ButtonInfoPlayerAction extends ButtonInfoTextLine
    {
        public ButtonInfoPlayerAction(GuiInfo g, InfoPlayerActionLine w)
        {
            super(g, null);
            height = 18;
            title = action.displayName.getFormattedText();
            width = (action.icon == null ? 8 : 24) + g.font.getStringWidth(title);
        }

        @Override
        public void addMouseOverText(GuiLM gui, List<String> l)
        {
        }

        @Override
        public void onClicked(GuiLM gui, MouseButton button)
        {
            FTBLibClient.playClickSound();
            action.onClicked(playerLM);
        }

        @Override
        public void renderWidget(GuiLM gui)
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

            action.render(ax + 1, ay + 1);
            action.postRender(ax + 1, ay + 1);

            guiInfo.font.drawString(title, (int) ax + (action.icon == null ? 4 : 20), (int) ay + 5, guiInfo.colorText);
        }
    }

    public final ForgePlayerSP playerLM;
    public final ActionButton action;

    public InfoPlayerActionLine(InfoPage c, ForgePlayerSP p, ActionButton a)
    {
        super(c, null);
        playerLM = p;
        action = a;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ButtonInfoTextLine createWidget(GuiInfo gui)
    {
        return new ButtonInfoPlayerAction(gui, this);
    }
}
