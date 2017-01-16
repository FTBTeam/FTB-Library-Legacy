package com.feed_the_beast.ftbl.lib.info;

import com.feed_the_beast.ftbl.api.gui.IGui;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.lib.gui.ButtonLM;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.feed_the_beast.ftbl.lib.gui.misc.GuiInfo;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.util.List;

/**
 * Created by LatvianModder on 04.03.2016.
 */
class ButtonInfoImage extends ButtonLM
{
    private final InfoImageLine line;

    ButtonInfoImage(InfoImageLine l)
    {
        super(0, 0, 0, 0);
        line = l;
        setWidth(line.imageWidth);
        setHeight(line.imageHeight);
    }

    @Override
    public void renderWidget(IGui gui)
    {
        GlStateManager.color(1F, 1F, 1F, 1F);
        line.imageProvider.bindTexture();

        int width = getWidth();
        int height = getHeight();

        if(width == 0 || height == 0)
        {
            width = line.imageWidth == 0 ? GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH) : line.imageWidth;
            height = line.imageHeight == 0 ? GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT) : line.imageHeight;

            double w = Math.min(((GuiInfo) gui).panelText.getWidth(), width * line.imageScale);
            double h = height * (w / (width * line.imageScale)) + 1D;

            setWidth(width = (int) w);
            setHeight(height = (int) h);
            ((GuiInfo) gui).updateTextPanelPositions();
        }

        GuiHelper.render(line.imageProvider, getAX(), getAY(), width, height);
    }

    @Override
    public void addMouseOverText(IGui gui, List<String> l)
    {
        if(line.hover != null)
        {
            l.addAll(line.hover);
        }
    }

    @Override
    public void onClicked(IGui gui, IMouseButton button)
    {
        if(line.clickEvent != null)
        {
            GuiHelper.playClickSound();
            ButtonInfoExtendedTextLine.onClickEvent(line.clickEvent);
        }
    }
}