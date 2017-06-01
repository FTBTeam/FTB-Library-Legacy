package com.feed_the_beast.ftbl.lib;

import com.feed_the_beast.ftbl.api.gui.IDrawableObject;
import com.feed_the_beast.ftbl.lib.gui.GuiIcons;
import net.minecraft.util.IStringSerializable;

public enum EnumInvMode implements IStringSerializable
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

    public LangKey getLangKey()
    {
        return langKey;
    }

    public IDrawableObject getIcon()
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

    public boolean canInsert()
    {
        return this == IO || this == IN;
    }

    public boolean canExtract()
    {
        return this == IO || this == OUT;
    }
}