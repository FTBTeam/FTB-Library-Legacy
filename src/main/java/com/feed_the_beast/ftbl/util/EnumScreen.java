package com.feed_the_beast.ftbl.util;

import com.feed_the_beast.ftbl.api.config.EnumNameMap;

public enum EnumScreen
{
    OFF,
    SCREEN,
    CHAT;

    public static final EnumNameMap<EnumScreen> NAME_MAP = new EnumNameMap<>(false, values());
}