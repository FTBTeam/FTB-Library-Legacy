package com.feed_the_beast.ftbl.api.client.gui.widgets;

import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.api.client.gui.GuiLM;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

@SideOnly(Side.CLIENT)
public abstract class ButtonSimpleLM extends ButtonLM
{
    public int colorText = 0xFFFFFFFF;
    public int colorButton = 0xFF888888;
    public int colorButtonOver = 0xFF999999;

    public ButtonSimpleLM(double x, double y, double w, double h)
    {
        super(x, y, w, h);
    }

    @Override
    public void addMouseOverText(GuiLM gui, List<String> l)
    {
    }

    @Override
    public void renderWidget(GuiLM gui)
    {
        double ax = getAX();
        double ay = getAY();
        FTBLibClient.setGLColor(gui.isMouseOver(this) ? colorButtonOver : colorButton);
        GuiLM.drawBlankRect(ax, ay, width, height);
        GlStateManager.color(1F, 1F, 1F, 1F);
        gui.font.drawStringWithShadow(title, (int) (ax + (width - gui.font.getStringWidth(title)) / 2D), (int) (ay + (height - gui.font.FONT_HEIGHT) / 2D), colorText);
    }
}