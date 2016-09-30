package com.feed_the_beast.ftbl.lib.gui.selectors;

import com.feed_the_beast.ftbl.api.gui.IGuiSelectors;

import javax.annotation.Nullable;

public class GuiStringField extends GuiAbstractField<String>
{
    private final IGuiSelectors.StringCallback callback;

    GuiStringField(@Nullable Object id, String def, IGuiSelectors.StringCallback c)
    {
        super(id, def);
        callback = c;
    }

    @Override
    protected boolean isValid(String val)
    {
        return true;
    }

    @Override
    protected void onCancelled(String def)
    {
        callback.onStringCallback(ID, def);
    }

    @Override
    protected void onCallback(String val)
    {
        callback.onStringCallback(ID, val);
    }
}