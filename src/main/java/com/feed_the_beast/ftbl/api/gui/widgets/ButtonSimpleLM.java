package com.feed_the_beast.ftbl.api.gui.widgets;

import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.api.gui.GuiLM;
import com.feed_the_beast.ftbl.api.gui.IGuiLM;
import net.minecraft.client.gui.GuiScreen;
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

    public ButtonSimpleLM(IGuiLM g, int x, int y, int w, int h)
    {
        super(g, x, y, w, h);
    }

    @Override
    public void addMouseOverText(List<String> l)
    {
    }

    @Override
    public void renderWidget()
    {
        int ax = getAX();
        int ay = getAY();
        FTBLibClient.setGLColor(mouseOver(ax, ay) ? colorButtonOver : colorButton);
        GuiLM.drawBlankRect(ax, ay, gui.getZLevel(), width, height);
        GlStateManager.color(1F, 1F, 1F, 1F);
        ((GuiScreen) gui).drawCenteredString(gui.getFontRenderer(), title, ax + width / 2, ay + (height - gui.getFontRenderer().FONT_HEIGHT) / 2, colorText);
    }
}