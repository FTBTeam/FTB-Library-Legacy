package com.feed_the_beast.ftbl.gui.info;

import com.feed_the_beast.ftbl.api.config.ConfigEntryBool;
import com.feed_the_beast.ftbl.api.config.ConfigEntryInt;
import com.latmod.lib.annotations.Flags;
import com.latmod.lib.annotations.NumberBounds;

/**
 * Created by LatvianModder on 22.03.2016.
 */
public class InfoClientSettings
{
    public static final ConfigEntryBool unicode = new ConfigEntryBool(true);

    @Flags(Flags.USE_SLIDER)
    @NumberBounds(min = 0, max = 200)
    public static final ConfigEntryInt border_width = new ConfigEntryInt(15);

    @Flags(Flags.USE_SLIDER)
    @NumberBounds(min = 0, max = 50)
    public static final ConfigEntryInt border_height = new ConfigEntryInt(15);
}