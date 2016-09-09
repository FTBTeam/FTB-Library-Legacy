package com.feed_the_beast.ftbl.gui.friends;

import com.feed_the_beast.ftbl.api.gui.GuiHelper;
import com.feed_the_beast.ftbl.api.gui.GuiIcons;
import com.feed_the_beast.ftbl.api.gui.GuiLang;
import com.feed_the_beast.ftbl.api.gui.IGui;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.api.info.IGuiInfoPage;
import com.feed_the_beast.ftbl.api.info.impl.ButtonInfoTextLine;
import com.feed_the_beast.ftbl.api.info.impl.EmptyInfoPageLine;
import com.feed_the_beast.ftbl.client.ClientNotifications;
import com.feed_the_beast.ftbl.gui.GuiInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * Created by LatvianModder on 23.03.2016.
 */
public class InfoNotificationLine extends EmptyInfoPageLine
{
    public class ButtonInfoNotification extends ButtonInfoTextLine
    {
        public ClientNotifications.NotificationWidget widget;

        public ButtonInfoNotification(GuiInfo g)
        {
            super(g, null);
            widget = new ClientNotifications.NotificationWidget(notification.notification);
            setHeight(widget.height);
        }

        @Override
        public void addMouseOverText(IGui gui, List<String> l)
        {
            if(gui.getMouseX() >= getAX() + getWidth() - 32)
            {
                l.add(GuiLang.BUTTON_CLOSE.translate());
            }
        }

        @Override
        public void onClicked(IGui gui, IMouseButton button)
        {
            GuiHelper.playClickSound();
            ClientNotifications.Perm.MAP.remove(widget.notification.getID());
            gui.refreshWidgets();
        }

        @Override
        public void renderWidget(IGui gui)
        {
            setWidth(((GuiInfo) gui).panelText.getWidth() - 4);
            widget.width = getWidth();

            int ay = getAY();
            int ax = getAX();

            widget.render(Minecraft.getMinecraft(), ax, ay);

            if(gui.isMouseOver(this))
            {
                GlStateManager.color(1F, 1F, 1F, 0.2F);
                GuiHelper.drawBlankRect(ax, ay, getWidth(), getHeight());

                if(gui.getMouseX() >= ax + getWidth() - 32)
                {
                    GuiHelper.drawBlankRect(ax + getWidth() - 32, ay, 32, getHeight());
                    GlStateManager.color(1F, 1F, 1F, 1F);
                }

                GuiHelper.render(GuiIcons.CLOSE, ax + getWidth() - 32, ay + (getHeight() - 32) / 2, 32, 32);
            }
        }
    }

    public final ClientNotifications.Perm notification;

    public InfoNotificationLine(ClientNotifications.Perm p)
    {
        notification = p;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ButtonInfoTextLine createWidget(GuiInfo gui, IGuiInfoPage page)
    {
        return new ButtonInfoNotification(gui);
    }
}
