package com.feed_the_beast.ftbl.client;

import com.feed_the_beast.ftbl.lib.config.PropertyBool;
import com.feed_the_beast.ftbl.lib.config.PropertyEnum;

/**
 * @author LatvianModder
 */
public class FTBLibClientConfig
{
	public static final PropertyBool ITEM_ORE_NAMES = new PropertyBool(false);
	public static final PropertyBool ITEM_NBT = new PropertyBool(false);
	public static final PropertyBool ACTION_BUTTONS_ON_TOP = new PropertyBool(true);
	public static final PropertyBool IGNORE_NEI = new PropertyBool(false);
	public static final PropertyEnum<EnumNotificationDisplay> NOTIFICATIONS = new PropertyEnum<>(EnumNotificationDisplay.NAME_MAP);
	public static final PropertyBool REPLACE_STATUS_MESSAGE_WITH_NOTIFICATION = new PropertyBool(true);
}