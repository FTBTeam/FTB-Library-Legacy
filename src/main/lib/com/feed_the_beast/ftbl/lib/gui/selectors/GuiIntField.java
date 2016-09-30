package com.feed_the_beast.ftbl.lib.gui.selectors;

import com.feed_the_beast.ftbl.api.gui.IGuiSelectors;
import com.feed_the_beast.ftbl.lib.math.Converter;

import javax.annotation.Nullable;

public class GuiIntField extends GuiAbstractField<Integer>
{
    private final IGuiSelectors.IntCallback callback;

    GuiIntField(@Nullable Object id, Integer def, IGuiSelectors.IntCallback c)
    {
        super(id, def);
        callback = c;
    }

    @Override
    protected boolean isValid(String val)
    {
        return Converter.canParseInt(val);
    }

    @Override
    protected void onCancelled(Integer def)
    {
        callback.onIntCallback(ID, def);
    }

    @Override
    protected void onCallback(String val)
    {
        callback.onIntCallback(ID, Integer.parseInt(val));
    }
}