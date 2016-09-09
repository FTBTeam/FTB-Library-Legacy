package com.feed_the_beast.ftbl.api.info.impl;

import com.feed_the_beast.ftbl.api.gui.GuiHelper;
import com.feed_the_beast.ftbl.api.gui.IGui;
import com.feed_the_beast.ftbl.api.info.ISpecialInfoButton;
import com.latmod.lib.ITextureCoords;

/**
 * Created by LatvianModder on 02.09.2016.
 */
public abstract class SpecialInfoButton implements ISpecialInfoButton
{
    private String title;
    private ITextureCoords icon;

    public SpecialInfoButton(String s, ITextureCoords icn)
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
    public void render(IGui gui, int ax, int ay)
    {
        GuiHelper.render(icon, ax, ay, 16, 16);
    }
}
