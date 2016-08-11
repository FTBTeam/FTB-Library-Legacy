package com.feed_the_beast.ftbl.api.gui.widgets;

import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.api.gui.GuiLM;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;

@SideOnly(Side.CLIENT)
public abstract class ButtonSimpleLM extends ButtonLM
{
    public int colorText = 0xFFFFFFFF;
    public int colorButton = 0xFF888888;
    public int colorButtonOver = 0xFF999999;

    public ButtonSimpleLM(int x, int y, int w, int h, String t)
    {
        super(x, y, w, h, t);
    }

    @Override
    public void addMouseOverText(@Nonnull GuiLM gui, @Nonnull List<String> l)
    {
    }

    @Override
    public void renderWidget(@Nonnull GuiLM gui)
    {
        int ax = getAX();
        int ay = getAY();
        FTBLibClient.setGLColor(gui.isMouseOver(this) ? colorButtonOver : colorButton);
        GuiLM.drawBlankRect(ax, ay, width, height);
        GlStateManager.color(1F, 1F, 1F, 1F);
        gui.font.drawStringWithShadow(title, ax + (width - gui.font.getStringWidth(title)) / 2, ay + (height - gui.font.FONT_HEIGHT) / 2, colorText);
    }
}