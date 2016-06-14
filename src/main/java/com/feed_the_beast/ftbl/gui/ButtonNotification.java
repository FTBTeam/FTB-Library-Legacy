package com.feed_the_beast.ftbl.gui;

import com.feed_the_beast.ftbl.api.MouseButton;
import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.api.client.gui.GuiIcons;
import com.feed_the_beast.ftbl.api.client.gui.GuiLM;
import com.feed_the_beast.ftbl.api.client.gui.GuiLang;
import com.feed_the_beast.ftbl.api.client.gui.widgets.ButtonLM;
import com.feed_the_beast.ftbl.api.notification.ClientNotifications;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

public class ButtonNotification extends ButtonLM
{
    public final ClientNotifications.Perm notification;

    public ButtonNotification(GuiNotifications g, ClientNotifications.Perm n)
    {
        super(0, 0, 0, 24);
        notification = n;
        posY += g.buttonList.size() * 25;
        title = n.notification.text.get(0).getFormattedText();
        width = g.font.getStringWidth(title);

        if(n.notification.hasItem())
        {
            width += 20;
        }

        width += 8;
    }

    @Override
    public void renderWidget(GuiLM gui)
    {
        double ax = getAX();
        double ay = getAY();

        int tx = 4;
        ItemStack is = notification.notification.getItem();
        if(is != null)
        {
            tx += 20;
            FTBLibClient.renderGuiItem(is, ax + 4D, ay + 4D);
        }

        GlStateManager.color(1F, 1F, 1F, 1F);

        FTBLibClient.setGLColor(notification.notification.getColor(), gui.isMouseOver(this) ? 255 : 185);
        GuiLM.drawBlankRect(ax, ay, parentPanel.width, height);
        GlStateManager.color(1F, 1F, 1F, 1F);

        gui.font.drawString(title, (int) (ax + tx), (int) ay + 4, 0xFFFFFFFF);
        if(notification.notification.text.size() > 1)
        {
            gui.font.drawString(notification.notification.text.get(1).getFormattedText(), (int) (ax + tx), (int) ay + 14, 0xFFFFFFFF);
        }

        if(gui.isMouseOver(this))
        {
            float alpha = 0.4F;
            if(gui.mouseX >= ax + width - 16)
            {
                alpha = 1F;
            }

            GlStateManager.color(1F, 1F, 1F, alpha);
            GuiLM.render(GuiIcons.close, ax + width - 18, ay + 4, 32, 32);
            GlStateManager.color(1F, 1F, 1F, 1F);
        }
    }

    @Override
    public void onClicked(@Nonnull GuiLM gui, @Nonnull MouseButton button)
    {
        FTBLibClient.playClickSound();

        if(gui.mouseX < getAX() + width - 16)
        {
            notification.onClicked(button);
        }

        ClientNotifications.Perm.map.remove(notification.notification.getID());

        gui.onInit();
        gui.refreshWidgets();
    }

    @Override
    public void addMouseOverText(GuiLM gui, List<String> l)
    {
        if(gui.isMouseOver(this) && gui.mouseX >= getAX() + width - 16)
        {
            l.add(GuiLang.button_close.translate());
        }
    }
}