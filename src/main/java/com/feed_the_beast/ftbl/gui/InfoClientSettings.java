package com.feed_the_beast.ftbl.gui;

import com.feed_the_beast.ftbl.api.config.impl.PropertyBool;
import com.feed_the_beast.ftbl.api.config.impl.PropertyInt;
import com.latmod.lib.annotations.Flags;

/**
 * Created by LatvianModder on 22.03.2016.
 */
public class InfoClientSettings
{
    public static final PropertyBool UNICODE = new PropertyBool(true);

    @Flags(Flags.USE_SLIDER)
    public static final PropertyInt BORDER_WIDTH = new PropertyInt(15).setMin(0).setMax(200);

    @Flags(Flags.USE_SLIDER)
    public static final PropertyInt BORDER_HEIGHT = new PropertyInt(15).setMin(0).setMax(200);
}