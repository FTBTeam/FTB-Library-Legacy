package com.feed_the_beast.ftbl.lib.info;

import com.feed_the_beast.ftbl.api.gui.IGui;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.api.info.IPageIconRenderer;
import com.feed_the_beast.ftbl.lib.gui.ButtonLM;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.feed_the_beast.ftbl.lib.gui.misc.GuiInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by LatvianModder on 04.03.2016.
 */
public class ButtonInfoPage extends ButtonLM
{
    public final GuiInfo guiInfo;
    public final InfoPage page;
    public String hover;
    public IPageIconRenderer iconRenderer;
    private boolean prevMouseOver = false;

    public ButtonInfoPage(GuiInfo g, InfoPage p, @Nullable IPageIconRenderer t)
    {
        super(0, 0, g.panelWidth - 36, t == null ? 13 : 18);
        guiInfo = g;
        page = p;
        iconRenderer = t;
        updateTitle(g);
    }

    @Override
    public void onClicked(IGui gui, IMouseButton button)
    {
        GuiHelper.playClickSound();
        guiInfo.setSelectedPage(page);
    }

    public void updateTitle(IGui gui)
    {
        ITextComponent titleC = page.getDisplayName().createCopy();

        if(guiInfo.getSelectedPage() == page)
        {
            titleC.getStyle().setBold(true);
        }

        if(gui.isMouseOver(this))
        {
            titleC.getStyle().setUnderlined(true);
        }

        setTitle(titleC.getFormattedText());
        hover = null;

        if(guiInfo.getFont().getStringWidth(getTitle(gui)) > getWidth())
        {
            hover = page.getDisplayName().getFormattedText();
        }
    }

    @Override
    public void addMouseOverText(IGui gui, List<String> l)
    {
        if(hover != null)
        {
            l.add(hover);
        }
    }

    @Override
    public boolean shouldRender(IGui gui)
    {
        return getParentPanel().isInside(this);
    }

    @Override
    public void renderWidget(IGui gui)
    {
        boolean mouseOver = gui.isMouseOver(this);

        if(prevMouseOver != mouseOver)
        {
            updateTitle(gui);
            prevMouseOver = mouseOver;
        }

        int ay = getAY();
        int ax = getAX();

        if(iconRenderer != null)
        {
            GlStateManager.color(1F, 1F, 1F, 1F);
            iconRenderer.renderIcon(gui, this, ax + 1, ay + 1);
            guiInfo.getFont().drawString(getTitle(gui), ax + 19, ay + 6, guiInfo.colorText);
        }
        else
        {
            GlStateManager.color(1F, 1F, 1F, 1F);
            guiInfo.getFont().drawString(getTitle(gui), ax + 1, ay + 1, guiInfo.colorText);
        }

        GlStateManager.color(1F, 1F, 1F, 1F);
    }
}