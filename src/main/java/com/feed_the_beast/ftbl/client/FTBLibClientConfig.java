package com.feed_the_beast.ftbl.client;

import com.feed_the_beast.ftbl.api.config.impl.PropertyBool;
import com.feed_the_beast.ftbl.api.config.impl.PropertyEnum;
import com.latmod.lib.EnumNameMap;

/**
 * Created by LatvianModder on 13.09.2016.
 */
public class FTBLibClientConfig
{
    public static final PropertyBool ITEM_ORE_NAMES = new PropertyBool(false);
    public static final PropertyBool ACTION_BUTTONS_ON_TOP = new PropertyBool(true);
    public static final PropertyEnum<EnumNotificationDisplay> NOTIFICATIONS = new PropertyEnum<>(new EnumNameMap<>(EnumNotificationDisplay.values(), false), EnumNotificationDisplay.SCREEN);
}