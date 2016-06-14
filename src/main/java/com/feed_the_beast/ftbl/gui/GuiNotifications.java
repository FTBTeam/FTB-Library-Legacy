package com.feed_the_beast.ftbl.gui;

import com.feed_the_beast.ftbl.api.client.gui.GuiLM;
import com.feed_the_beast.ftbl.api.notification.ClientNotifications;
import com.feed_the_beast.ftbl.client.FTBLibActions;
import latmod.lib.math.MathHelperLM;
import latmod.lib.util.LMMapUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiNotifications extends GuiLM
{
    public final List<ButtonNotification> buttonList;

    public GuiNotifications()
    {
        height = 25 * 7;
        buttonList = new ArrayList<>();
    }

    @Override
    public void onInit()
    {
        width = 0;
        buttonList.clear();
        LMMapUtils.sortMap(ClientNotifications.Perm.map, (o1, o2) -> Long.compare(o2.getValue().timeAdded, o1.getValue().timeAdded));

        int s = 0;

        for(ClientNotifications.Perm p : ClientNotifications.Perm.map.values())
        {
            ButtonNotification b = new ButtonNotification(this, p);
            buttonList.add(b);
            width = Math.max(width, b.width);
            s++;

            if(s == 7)
            {
                break;
            }
        }

        width = MathHelperLM.clamp(width, 200D, 300D);

        for(ButtonNotification b : buttonList)
        {
            b.width = width;
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
        drawBlankRect(posX, posY, width, height);

        for(int i = 1; i < 7; i++)
        {
            drawBlankRect(posX, posY + i * 25 - 1, width, 1);
        }

        GlStateManager.color(1F, 1F, 1F, 1F);

        for(ButtonNotification b : buttonList)
        {
            b.renderWidget(this);
        }
    }
}