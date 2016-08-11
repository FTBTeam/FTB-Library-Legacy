package com.feed_the_beast.ftbl.api.tile;

import com.feed_the_beast.ftbl.api.LangKey;
import com.feed_the_beast.ftbl.api.gui.GuiIcons;
import com.latmod.lib.TextureCoords;

public enum EnumInvMode
{
    ENABLED("enabled"),
    ONLY_IN("onlyin"),
    ONLY_OUT("onlyout"),
    DISABLED("disabled");

    public static final EnumInvMode[] VALUES = values();
    public static final LangKey enumLangKey = new LangKey("ftbl.invmode");

    public final LangKey langKey;

    EnumInvMode(String s)
    {
        langKey = new LangKey("ftbl.invmode." + s);
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