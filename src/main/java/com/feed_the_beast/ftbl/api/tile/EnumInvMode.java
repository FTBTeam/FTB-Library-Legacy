package com.feed_the_beast.ftbl.api.tile;

import com.feed_the_beast.ftbl.api.client.gui.GuiIcons;
import com.feed_the_beast.ftbl.util.TextureCoords;

public enum EnumInvMode
{
    ENABLED("enabled"),
    ONLY_IN("onlyin"),
    ONLY_OUT("onlyout"),
    DISABLED("disabled");

    public static final EnumInvMode[] VALUES = values();
    public static final String enumLangKey = "ftbl.invmode";

    public final String langKey;

    EnumInvMode(String s)
    {
        langKey = enumLangKey + '.' + s;
    }

    public TextureCoords getIcon()
    {
        return GuiIcons.inv[ordinal()];
    }

    public boolean canInsertItem()
    {
        return this == ENABLED || this == ONLY_IN;
    }

    public boolean canExtractItem()
    {
        return this == ENABLED || this == ONLY_OUT;
    }
}