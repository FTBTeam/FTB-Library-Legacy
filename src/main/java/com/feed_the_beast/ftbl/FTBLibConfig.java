package com.feed_the_beast.ftbl;

import com.feed_the_beast.ftbl.lib.config.PropertyBool;
import com.feed_the_beast.ftbl.lib.config.PropertyShort;

/**
 * Created by LatvianModder on 28.09.2016.
 */
public class FTBLibConfig
{
    public static final PropertyBool AUTOCREATE_TEAMS = new PropertyBool(false);
    public static final PropertyBool MIRROR_FTB_COMMANDS = new PropertyBool(true);
    public static final PropertyShort MAX_TEAM_CHAT_HISTORY = new PropertyShort(300, 0, 10000);
}