package com.feed_the_beast.ftbl.util;

import com.feed_the_beast.ftbl.api.config.EnumNameMap;

public enum EnumNotificationDisplay
{
    OFF,
    SCREEN,
    CHAT,
    CHAT_ALL;

    public static final EnumNameMap<EnumNotificationDisplay> NAME_MAP = new EnumNameMap<>(false, values());
}