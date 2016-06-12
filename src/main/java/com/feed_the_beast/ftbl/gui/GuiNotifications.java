package com.feed_the_beast.ftbl.gui;

import com.feed_the_beast.ftbl.api.client.gui.GuiLM;
import com.feed_the_beast.ftbl.api.notification.ClientNotifications;
import com.feed_the_beast.ftbl.client.FTBLibActions;
import latmod.lib.MathHelperLM;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiNotifications extends GuiLM
{
    public final List<ButtonNotification> buttonList;

    public GuiNotifications()
    {
        heightW = 25 * 7;
        buttonList = new ArrayList<>();
    }

    @Override
    public void onInit()
    {
        widthW = 0;

        buttonList.clear();

        Collections.sort(ClientNotifications.Perm.list, null);

        int s = Math.min(ClientNotifications.Perm.list.size(), 7);

        for(int i = 0; i < s; i++)
        {
            ClientNotifications.Perm p = ClientNotifications.Perm.list.get(i);
            ButtonNotification b = new ButtonNotification(this, p);
            buttonList.add(b);
            widthW = Math.max(widthW, b.widthW);
        }

        widthW = MathHelperLM.clamp(widthW, 200D, 300D);

        for(ButtonNotification b : buttonList)
        {
            b.widthW = widthW;
        }
    }

    @Override
    public void addWidgets()
    {
        addAll(buttonList);
    }

    @Override
    public void drawBackground()
    {
        super.drawBackground();

        font.drawString(FTBLibActions.NOTIFICATIONS.displayName.getFormattedText(), (int) posX + 4, (int) posY - 11, 0xFFFFFFFF);

        GlStateManager.color(0F, 0F, 0F, 0.4F);
        drawBlankRect(posX, posY, widthW, heightW);

        for(int i = 1; i < 7; i++)
        {
            drawBlankRect(posX, posY + i * 25 - 1, widthW, 1);
        }

        GlStateManager.color(1F, 1F, 1F, 1F);

        for(ButtonNotification b : buttonList)
        {
            b.renderWidget(this);
        }
    }
}