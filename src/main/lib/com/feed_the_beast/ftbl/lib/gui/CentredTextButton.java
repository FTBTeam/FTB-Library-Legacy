package com.feed_the_beast.ftbl.lib.gui;

import com.feed_the_beast.ftbl.api.gui.IGui;

/**
 * Created by LatvianModder on 02.03.2017.
 */
public abstract class CentredTextButton extends ButtonLM
{
    public CentredTextButton(int x, int y, int w, int h, String txt)
    {
        super(x, y, w, h, txt);
        setIcon(DEFAULT_BACKGROUND);
    }

    @Override
    public int renderTitleInCenter(IGui gui)
    {
        return gui.getTextColor();
    }

    @Override
    public void renderWidget(IGui gui)
    {
        super.renderWidget(gui);

        if(gui.isMouseOver(this))
        {
            DEFAULT_MOUSE_OVER.draw(this);
        }
    }
}