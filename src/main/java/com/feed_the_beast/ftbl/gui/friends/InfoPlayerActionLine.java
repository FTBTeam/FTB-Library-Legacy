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
            title = action.getDisplayName();
            width = (action.icon == null ? 8 : 24) + g.getFontRenderer().getStringWidth(title);
        }

        @Override
        public void addMouseOverText(List<String> l)
        {
        }

        @Override
        public void onClicked(MouseButton button)
        {
            FTBLibClient.playClickSound();
            action.onClicked(playerLM);
        }

        @Override
        public void renderWidget()
        {
            int ay = getAY();
            if(ay < -height || ay > guiInfo.mainPanel.height)
            {
                return;
            }
            int ax = getAX();
            float z = gui.getZLevel();

            GlStateManager.enableBlend();

            if(mouseOver())
            {
                GlStateManager.color(1F, 1F, 1F, 0.2F);
                GuiLM.drawBlankRect(ax, ay, z, width, height);
            }

            GlStateManager.color(1F, 1F, 1F, 1F);

            action.render(ax + 1, ay + 1, z);
            action.postRender(ax + 1, ay + 1, z);

            gui.getFontRenderer().drawString(title, ax + (action.icon == null ? 4 : 20), ay + 5, guiInfo.colorText);
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
