package com.feed_the_beast.ftbl.lib.info;

import com.feed_the_beast.ftbl.api.gui.IImageProvider;
import com.feed_the_beast.ftbl.api.info.ISpecialInfoButton;
import com.feed_the_beast.ftbl.lib.gui.GuiBase;

/**
 * Created by LatvianModder on 02.09.2016.
 */
public abstract class SpecialInfoButton implements ISpecialInfoButton
{
    private String title;
    private IImageProvider icon;

    public SpecialInfoButton(String s, IImageProvider icn)
    {
        title = s;
        icon = icn;
    }

    @Override
    public String getTitle()
    {
        return title;
    }

    @Override
    public void render(GuiBase gui, int ax, int ay)
    {
        icon.draw(ax, ay, 16, 16);
    }
}