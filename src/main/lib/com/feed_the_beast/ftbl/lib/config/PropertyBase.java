package com.feed_the_beast.ftbl.lib.config;

import com.feed_the_beast.ftbl.api.config.IConfigKey;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.api.config.IGuiEditConfig;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.lib.gui.misc.GuiSelectors;
import com.feed_the_beast.ftbl.lib.util.JsonUtils;
import com.google.gson.JsonElement;

/**
 * Created by LatvianModder on 12.09.2016.
 */
public abstract class PropertyBase implements IConfigValue
{
    @Override
    public void onClicked(IGuiEditConfig gui, IConfigKey key, IMouseButton button)
    {
        GuiSelectors.selectJson(this, (value, set) ->
        {
            if(set)
            {
                fromJson(value.getSerializableElement());
                gui.onChanged(key, getSerializableElement());
            }

            gui.openGui();
        });
    }

    @Override
    public boolean setValueFromString(String text, boolean simulate)
    {
        try
        {
            JsonElement json = JsonUtils.fromJson(text);

            if(!json.isJsonNull())
            {
                if(!simulate)
                {
                    fromJson(json);
                }

                return true;
            }
        }
        catch(Exception ex)
        {
        }

        return false;
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