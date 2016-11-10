package com.feed_the_beast.ftbl.lib.gui;

import com.feed_the_beast.ftbl.api.config.ConfigValue;
import com.feed_the_beast.ftbl.lib.config.PropertyBool;
import com.feed_the_beast.ftbl.lib.config.PropertyInt;
import com.feed_the_beast.ftbl.lib.internal.FTBLibFinals;

/**
 * Created by LatvianModder on 11.11.2016.
 */
public class GuiInfoConfig
{
    @ConfigValue(id = "info.unicode", file = FTBLibFinals.MOD_ID, client = true)
    public static final PropertyBool INFO_UNICODE = new PropertyBool(true);

    @ConfigValue(id = "info.border_width", file = FTBLibFinals.MOD_ID, client = true, useScrollBar = true)
    public static final PropertyInt INFO_BORDER_WIDTH = new PropertyInt(15).setMin(0).setMax(200);

    @ConfigValue(id = "info.border_height", file = FTBLibFinals.MOD_ID, client = true, useScrollBar = true)
    public static final PropertyInt INFO_BORDER_HEIGHT = new PropertyInt(15).setMin(0).setMax(200);
}