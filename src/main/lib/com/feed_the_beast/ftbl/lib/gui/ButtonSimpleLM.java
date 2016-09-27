package com.feed_the_beast.ftbl.lib.gui;

import com.feed_the_beast.ftbl.api.gui.IGui;
import com.feed_the_beast.ftbl.lib.util.LMColorUtils;
import net.minecraft.client.renderer.GlStateManager;

import java.util.List;

public abstract class ButtonSimpleLM extends ButtonLM
{
    public ButtonSimpleLM(int x, int y, int w, int h, String t)
    {
        super(x, y, w, h, t);
    }

    @Override
    public void addMouseOverText(IGui gui, List<String> l)
    {
    }

    public int getTextColor(IGui gui)
    {
        return 0xFFFFFFFF;
    }

    public int getButtonColor(IGui gui, boolean over)
    {
        return over ? 0xFF999999 : 0xFF888888;
    }

    @Override
    public void renderWidget(IGui gui)
    {
        int ax = getAX();
        int ay = getAY();
        LMColorUtils.setGLColor(getButtonColor(gui, gui.isMouseOver(this)));
        GuiHelper.drawBlankRect(ax, ay, getWidth(), getHeight());
        GlStateManager.color(1F, 1F, 1F, 1F);
        String title = getTitle(gui);

        if(title != null)
        {
            gui.getFont().drawStringWithShadow(title, ax + (getWidth() - gui.getFont().getStringWidth(title)) / 2, ay + (getHeight() - gui.getFont().FONT_HEIGHT) / 2, getTextColor(gui));
        }
    }
}