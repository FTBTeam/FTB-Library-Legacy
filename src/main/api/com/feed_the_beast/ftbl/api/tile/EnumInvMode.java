package com.feed_the_beast.ftbl.api.tile;

import com.feed_the_beast.ftbl.api.gui.GuiIcons;
import com.latmod.lib.EnumNameMap;
import com.latmod.lib.ILangKeyContainer;
import com.latmod.lib.ITextureCoords;
import com.latmod.lib.LangKey;
import net.minecraft.util.IStringSerializable;

public enum EnumInvMode implements IStringSerializable, ILangKeyContainer
{
    IO,
    IN,
    OUT,
    NONE;

    public static final EnumInvMode[] VALUES = values();
    public static final LangKey ENUM_LANG_KEY = new LangKey("ftbl.invmode");

    private final String name;
    private final LangKey langKey;

    EnumInvMode()
    {
        name = EnumNameMap.createName(this);
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

    public ITextureCoords getIcon()
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