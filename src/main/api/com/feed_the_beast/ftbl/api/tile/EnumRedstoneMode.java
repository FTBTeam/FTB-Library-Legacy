package com.feed_the_beast.ftbl.api.tile;

import com.feed_the_beast.ftbl.api.LangKey;
import com.feed_the_beast.ftbl.api.gui.GuiIcons;
import com.latmod.lib.TextureCoords;

public enum EnumRedstoneMode
{
    DISABLED("disabled"),
    ACTIVE_HIGH("high"),
    ACTIVE_LOW("low");

    public static final EnumRedstoneMode[] VALUES = values();
    public static final LangKey enumLangKey = new LangKey("ftbl.redstonemode");

    public final int ID;
    public final LangKey langKey;

    EnumRedstoneMode(String s)
    {
        ID = ordinal();
        langKey = new LangKey("ftbl.redstonemode." + s);
    }

    public boolean cancel(boolean b)
    {
        if(this == DISABLED)
        {
            return false;
        }
        return this == ACTIVE_HIGH && !b || this == ACTIVE_LOW && b;
    }

    public TextureCoords getIcon()
    {
        return GuiIcons.redstone[ordinal()];
    }
}