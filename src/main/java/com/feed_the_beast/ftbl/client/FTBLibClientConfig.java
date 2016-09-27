package com.feed_the_beast.ftbl.client;

import com.feed_the_beast.ftbl.FTBLibFinals;
import com.feed_the_beast.ftbl.api.config.ConfigValue;
import com.feed_the_beast.ftbl.lib.EnumNameMap;
import com.feed_the_beast.ftbl.lib.config.PropertyBool;
import com.feed_the_beast.ftbl.lib.config.PropertyEnum;
import com.feed_the_beast.ftbl.lib.config.PropertyInt;

/**
 * Created by LatvianModder on 13.09.2016.
 */
public class FTBLibClientConfig
{
    @ConfigValue(id = "item_ore_names", file = FTBLibFinals.MOD_ID, client = true)
    public static final PropertyBool ITEM_ORE_NAMES = new PropertyBool(false);

    @ConfigValue(id = "action_buttons_on_top", file = FTBLibFinals.MOD_ID, client = true)
    public static final PropertyBool ACTION_BUTTONS_ON_TOP = new PropertyBool(true);

    @ConfigValue(id = "notifications", file = FTBLibFinals.MOD_ID, client = true)
    public static final PropertyEnum<EnumNotificationDisplay> NOTIFICATIONS = new PropertyEnum<>(new EnumNameMap<>(EnumNotificationDisplay.values(), false), EnumNotificationDisplay.SCREEN);

    // InfoGUI //

    @ConfigValue(id = "info.unicode", file = FTBLibFinals.MOD_ID, client = true)
    public static final PropertyBool INFO_UNICODE = new PropertyBool(true);

    @ConfigValue(id = "info.border_width", file = FTBLibFinals.MOD_ID, client = true, useScrollBar = true)
    public static final PropertyInt INFO_BORDER_WIDTH = new PropertyInt(15).setMin(0).setMax(200);

    @ConfigValue(id = "info.border_height", file = FTBLibFinals.MOD_ID, client = true, useScrollBar = true)
    public static final PropertyInt INFO_BORDER_HEIGHT = new PropertyInt(15).setMin(0).setMax(200);
}