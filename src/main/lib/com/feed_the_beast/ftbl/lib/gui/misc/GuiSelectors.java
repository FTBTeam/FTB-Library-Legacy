package com.feed_the_beast.ftbl.lib.gui.misc;

import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.lib.config.PropertyColor;

/**
 * Created by LatvianModder on 29.09.2016.
 */
public class GuiSelectors
{
    public static void selectJson(IConfigValue value, IGuiFieldCallback callback)
    {
        new GuiConfigValueField(value, callback).openGui();
    }

    public static void selectColor(PropertyColor value, IGuiFieldCallback callback)
    {
        new GuiColorField(value, callback).openGui();
    }
}