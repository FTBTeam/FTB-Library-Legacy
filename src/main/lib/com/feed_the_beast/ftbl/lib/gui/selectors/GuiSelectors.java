package com.feed_the_beast.ftbl.lib.gui.selectors;

import com.feed_the_beast.ftbl.api.gui.IGuiSelectors;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 29.09.2016.
 */
public enum GuiSelectors implements IGuiSelectors
{
    INSTANCE;

    @Override
    public void selectString(@Nullable Object ID, String def, StringCallback callback)
    {
        new GuiStringField(ID, def, callback).openGui();
    }

    @Override
    public void selectInt(@Nullable Object ID, int def, IntCallback callback)
    {
        new GuiIntField(ID, def, callback).openGui();
    }

    @Override
    public void selectDouble(@Nullable Object ID, double def, DoubleCallback callback)
    {
        new GuiDoubleField(ID, def, callback).openGui();
    }

    @Override
    public void selectColor(@Nullable Object ID, byte def, ColorCallback callback)
    {
        new GuiColorField(ID, def, callback).openGui();
    }
}
