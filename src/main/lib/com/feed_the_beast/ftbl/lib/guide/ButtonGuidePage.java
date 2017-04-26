package com.feed_the_beast.ftbl.lib.guide;

import com.feed_the_beast.ftbl.api.gui.IDrawableObject;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.lib.Color4I;
import com.feed_the_beast.ftbl.lib.gui.Button;
import com.feed_the_beast.ftbl.lib.gui.GuiBase;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.feed_the_beast.ftbl.lib.gui.misc.GuiGuide;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by LatvianModder on 04.03.2016.
 */
public class ButtonGuidePage extends Button
{
    public final GuidePage page;
    public String hover;
    public IDrawableObject iconRenderer;
    private boolean prevMouseOver = false;

    public ButtonGuidePage(GuiGuide g, GuidePage p, @Nullable IDrawableObject t)
    {
        super(0, 0, 0, 0);
        setWidth(g.panelWidth - 36);
        setHeight(t == null ? 13 : 18);
        page = p;
        iconRenderer = t;
        updateTitle(g);
    }

    @Override
    public void onClicked(GuiBase gui, IMouseButton button)
    {
        GuiHelper.playClickSound();
        ((GuiGuide) gui).setSelectedPage(page);
    }

    public void updateTitle(GuiGuide gui)
    {
        ITextComponent titleC = page.getDisplayName().createCopy();

        if(gui.getSelectedPage() == page)
        {
            titleC.getStyle().setBold(true);
        }

        if(gui.isMouseOver(this))
        {
            titleC.getStyle().setUnderlined(true);
        }

        setTitle(titleC.getFormattedText());
        hover = null;

        if(gui.getFont().getStringWidth(getTitle(gui)) > width)
        {
            hover = page.getDisplayName().getFormattedText();
        }
    }

    @Override
    public void addMouseOverText(GuiBase gui, List<String> list)
    {
        if(hover != null)
        {
            list.add(hover);
        }
    }

    @Override
    public void renderWidget(GuiBase gui)
    {
        boolean mouseOver = gui.isMouseOver(this);

        if(prevMouseOver != mouseOver)
        {
            updateTitle((GuiGuide) gui);
            prevMouseOver = mouseOver;
        }

        int ay = getAY();
        int ax = getAX();

        if(iconRenderer != null)
        {
            GlStateManager.color(1F, 1F, 1F, 1F);
            iconRenderer.draw(ax + 1, ay + 1, 16, 16, Color4I.NONE);
            gui.drawString(getTitle(gui), ax + 19, ay + 6);
        }
        else
        {
            GlStateManager.color(1F, 1F, 1F, 1F);
            gui.drawString(getTitle(gui), ax + 1, ay + 1);
        }

        GlStateManager.color(1F, 1F, 1F, 1F);
    }
}