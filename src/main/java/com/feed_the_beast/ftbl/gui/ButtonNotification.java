package com.feed_the_beast.ftbl.gui;

import com.feed_the_beast.ftbl.api.GuiLang;
import com.feed_the_beast.ftbl.api.MouseButton;
import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.api.gui.GuiIcons;
import com.feed_the_beast.ftbl.api.gui.GuiLM;
import com.feed_the_beast.ftbl.api.gui.widgets.ButtonLM;
import com.feed_the_beast.ftbl.api.notification.ClientNotifications;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ButtonNotification extends ButtonLM
{
    public final ClientNotifications.Perm notification;
    
    public ButtonNotification(GuiNotifications g, ClientNotifications.Perm n)
    {
        super(g, 0, 0, 0, 24);
        notification = n;
        posY += g.buttonList.size() * 25;
        title = n.notification.title.getFormattedText();
        width = gui.getFontRenderer().getStringWidth(n.notification.title.getFormattedText());
        if(n.notification.desc != null)
        { width = Math.max(width, gui.getFontRenderer().getStringWidth(n.notification.desc.getFormattedText())); }
        if(n.notification.item != null) { width += 20; }
        width += 8;
    }
    
    @Override
    public void renderWidget()
    {
        int ax = getAX();
        int ay = getAY();
        
        int tx = 4;
        ItemStack is = notification.notification.item;
        if(is != null)
        {
            tx += 20;
            GuiLM.drawItem(gui, is, ax + 4, ay + 4);
        }
        
        GlStateManager.color(1F, 1F, 1F, 1F);
        
        FTBLibClient.setGLColor(notification.notification.color, mouseOver(ax, ay) ? 255 : 185);
        GuiLM.drawBlankRect(ax, ay, gui.getZLevel(), parentPanel.width, height);
        GlStateManager.color(1F, 1F, 1F, 1F);
        
        gui.getFontRenderer().drawString(title, ax + tx, ay + 4, 0xFFFFFFFF);
        if(notification.notification.desc != null)
        { gui.getFontRenderer().drawString(notification.notification.desc.getFormattedText(), ax + tx, ay + 14, 0xFFFFFFFF); }
        
        if(mouseOver(ax, ay))
        {
            float alpha = 0.4F;
            if(gui.mouse().x >= ax + width - 16) { alpha = 1F; }
            
            GlStateManager.color(1F, 1F, 1F, alpha);
            GuiLM.render(GuiIcons.close, ax + width - 18, ay + 4, gui.getZLevel());
            GlStateManager.color(1F, 1F, 1F, 1F);
        }
    }
    
    @Override
    public void onClicked(MouseButton button)
    {
        FTBLibClient.playClickSound();
        
        if(gui.mouse().x < getAX() + width - 16) { notification.onClicked(button); }
        ClientNotifications.Perm.list.remove(notification.notification);
        
        gui.initLMGui();
        gui.refreshWidgets();
    }
    
    @Override
    public void addMouseOverText(List<String> l)
    {
        int ax = getAX();
        if(mouseOver(ax, getAY()) && gui.mouse().x >= ax + width - 16)
        {
            l.add(GuiLang.button_close.translate());
            return;
        }
        
        if(notification.notification.mouse != null) { notification.notification.mouse.addHoverText(l); }
    }
}