package com.feed_the_beast.ftbl.api.gui.widgets;

import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.api.gui.GuiLM;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

@SideOnly(Side.CLIENT)
public abstract class ButtonSimpleLM extends ButtonLM
{
    public ButtonSimpleLM(int x, int y, int w, int h, String t)
    {
        super(x, y, w, h, t);
    }

    @Override
    public void addMouseOverText(GuiLM gui, List<String> l)
    {
    }

    public int getTextColor(GuiLM gui)
    {
        return 0xFFFFFFFF;
    }

    public int getButtonColor(GuiLM gui, boolean over)
    {
        return over ? 0xFF999999 : 0xFF888888;
    }

    @Override
    public void renderWidget(GuiLM gui)
    {
        int ax = getAX();
        int ay = getAY();
        FTBLibClient.setGLColor(getButtonColor(gui, gui.isMouseOver(this)));
        GuiLM.drawBlankRect(ax, ay, width, height);
        GlStateManager.color(1F, 1F, 1F, 1F);
        String title = getTitle(gui);

        if(title != null)
        {
            gui.font.drawStringWithShadow(title, ax + (width - gui.font.getStringWidth(title)) / 2, ay + (height - gui.font.FONT_HEIGHT) / 2, getTextColor(gui));
        }
    }
}