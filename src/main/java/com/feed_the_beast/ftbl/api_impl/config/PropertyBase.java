package com.feed_the_beast.ftbl.api_impl.config;

import com.feed_the_beast.ftbl.api.config.IConfigKey;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.api.config.IGuiEditConfig;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.gui.GuiSelectField;
import com.latmod.lib.util.LMJsonUtils;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

/**
 * Created by LatvianModder on 12.09.2016.
 */
public abstract class PropertyBase implements IConfigValue
{
    @Override
    public double getDouble()
    {
        return getInt();
    }

    @Override
    public int getColor()
    {
        return 0x999999;
    }

    @Override
    @Nullable
    public String getMinValueString()
    {
        return null;
    }

    @Override
    @Nullable
    public String getMaxValueString()
    {
        return null;
    }

    @Override
    @Nullable
    public List<String> getVariants()
    {
        return null;
    }

    @Override
    public void onClicked(IGuiEditConfig gui, IConfigKey key, IMouseButton button)
    {
        GuiSelectField.display(null, GuiSelectField.FieldType.STRING, getSerializableElement().toString(), (id, val) ->
        {
            fromJson(LMJsonUtils.fromJson(val.toString()));
            gui.onChanged(key, getSerializableElement());
            gui.openGui();
        });
    }

    @Override
    public boolean equalsValue(IConfigValue value)
    {
        return Objects.equals(getValue(), value.getValue());
    }

    @Override
    public boolean equals(Object o)
    {
        return o instanceof IConfigValue && equalsValue((IConfigValue) o);
    }

    @Override
    public String toString()
    {
        return getString();
    }
}