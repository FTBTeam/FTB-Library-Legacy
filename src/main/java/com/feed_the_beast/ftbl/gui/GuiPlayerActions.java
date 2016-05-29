package com.feed_the_beast.ftbl.gui;

import com.feed_the_beast.ftbl.api.ForgePlayer;
import com.feed_the_beast.ftbl.api.MouseButton;
import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.api.gui.GuiLM;
import com.feed_the_beast.ftbl.api.gui.PlayerAction;
import com.feed_the_beast.ftbl.api.gui.widgets.ButtonLM;
import com.feed_the_beast.ftbl.api.gui.widgets.WidgetLM;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiPlayerActions extends GuiLM
{
    public static class ButtonPlayerActionSmall extends ButtonLM
    {
        public final GuiPlayerActions gui;
        public final PlayerAction action;

        public ButtonPlayerActionSmall(GuiPlayerActions g, PlayerAction a)
        {
            super(g, 0, g.mainPanel.height, 0, 18);
            gui = g;
            action = a;
            title = a.getDisplayName();
            width = 22 + g.getFontRenderer().getStringWidth(title);
        }

        @Override
        public void onClicked(MouseButton button)
        {
            FTBLibClient.mc().thePlayer.closeScreen();
            action.onClicked(gui.self, gui.other);
        }

        @Override
        public void renderWidget()
        {
            int ax = getAX();
            int ay = getAY();

            GlStateManager.color(0.46F, 0.46F, 0.46F, 0.53F);
            GuiLM.drawBlankRect(ax, ay, gui.getZLevel(), width, height);
            GuiLM.render(action.icon, ax + 1, ay + 1, gui.getZLevel());

            gui.getFontRenderer().drawString(title, ax + 20, ay + 6, 0xFFFFFFFF);
            GlStateManager.color(1F, 1F, 1F, 0.2F);
            if(mouseOver(ax, ay))
            {
                GuiLM.drawBlankRect(ax, ay, gui.getZLevel(), width, height);
            }
            GlStateManager.color(1F, 1F, 1F, 1F);
        }

        @Override
        public void addMouseOverText(List<String> l)
        {
        }
    }

    public final ForgePlayer self, other;
    public final List<PlayerAction> actions;

    public GuiPlayerActions(ForgePlayer s, ForgePlayer o, List<PlayerAction> a)
    {
        super(null, null);
        self = s;
        other = s;
        actions = a;
    }

    @Override
    public void addWidgets()
    {
        mainPanel.width = 0;
        mainPanel.height = 0;

        for(int i = 0; i < actions.size(); i++)
        {
            ButtonPlayerActionSmall b = new ButtonPlayerActionSmall(this, actions.get(i));
            mainPanel.add(b);
            mainPanel.width = Math.max(mainPanel.width, b.width);
            if(i != actions.size() - 1)
            {
                mainPanel.height += b.height + 4;
            }
        }

        for(WidgetLM w : mainPanel.widgets)
        {
            w.width = mainPanel.width;
        }

        mainPanel.posY -= 20;
    }

    @Override
    public void drawBackground()
    {
        for(WidgetLM w : mainPanel.widgets)
        {
            w.renderWidget();
        }
    }
}