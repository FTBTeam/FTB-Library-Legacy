package com.feed_the_beast.ftbl.lib.gui.misc;

import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.lib.config.PropertyColor;
import com.feed_the_beast.ftbl.lib.util.LMJsonUtils;

/**
 * Created by LatvianModder on 29.09.2016.
 */
public class GuiSelectors
{
    public static void selectJson(IConfigValue value, IGuiFieldCallback callback)
    {
        new GuiAbstractField(value, callback)
        {
            @Override
            protected boolean isValidText(IConfigValue value, String val)
            {
                return true;
            }

            @Override
            protected void setValue(IConfigValue value, String text)
            {
                value.fromJson(LMJsonUtils.fromJson(text));
            }
        }.openGui();
    }

    public static void selectColor(PropertyColor value, IGuiFieldCallback callback)
    {
        new GuiColorField(value, callback).openGui();
    }
}
