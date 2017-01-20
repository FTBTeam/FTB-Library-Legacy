package com.feed_the_beast.ftbl.lib.tile;

import com.feed_the_beast.ftbl.api.gui.IImageProvider;
import com.feed_the_beast.ftbl.api.tile.IInvMode;
import com.feed_the_beast.ftbl.lib.LangKey;
import com.feed_the_beast.ftbl.lib.gui.GuiIcons;

public enum EnumInvMode implements IInvMode
{
    IO("io"),
    IN("in"),
    OUT("out"),
    NONE("none");

    public static final EnumInvMode[] VALUES = values();
    public static final LangKey ENUM_LANG_KEY = new LangKey("ftbl.invmode");

    private final String name;
    private final LangKey langKey;

    EnumInvMode(String n)
    {
        name = n;
        langKey = new LangKey("ftbl.invmode." + name);
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public LangKey getLangKey()
    {
        return langKey;
    }

    @Override
    public IImageProvider getIcon()
    {
        switch(this)
        {
            case IO:
                return GuiIcons.INV_IO;
            case IN:
                return GuiIcons.INV_IN;
            case OUT:
                return GuiIcons.INV_OUT;
            default:
                return GuiIcons.INV_NONE;
        }
    }

    @Override
    public boolean canInsert()
    {
        return this == IO || this == IN;
    }

    @Override
    public boolean canExtract()
    {
        return this == IO || this == OUT;
    }
}