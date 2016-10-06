package com.feed_the_beast.ftbl.lib.gui.selectors;

import com.feed_the_beast.ftbl.lib.math.Converter;

import javax.annotation.Nullable;

public class GuiDoubleField extends GuiAbstractField<Double>
{
    private final GuiSelectors.DoubleCallback callback;

    GuiDoubleField(@Nullable Object id, Double def, GuiSelectors.DoubleCallback c)
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
    protected void onCancelled(Double def)
    {
        callback.onDoubleCallback(ID, def);
    }

    @Override
    protected void onCallback(String val)
    {
        callback.onDoubleCallback(ID, Double.parseDouble(val));
    }
}