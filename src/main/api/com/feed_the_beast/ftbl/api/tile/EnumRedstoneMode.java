package com.feed_the_beast.ftbl.api.tile;

import com.feed_the_beast.ftbl.api.gui.GuiIcons;
import com.latmod.lib.ITextureCoordsProvider;
import com.latmod.lib.LangKey;

import java.util.Locale;

public enum EnumRedstoneMode implements IRedstoneMode
{
    DISABLED,
    ACTIVE_HIGH,
    ACTIVE_LOW,
    PULSE;

    public static final EnumRedstoneMode[] VALUES = {DISABLED, ACTIVE_HIGH, ACTIVE_LOW};
    public static final EnumRedstoneMode[] VALUES_WITH_PULSE = {DISABLED, ACTIVE_HIGH, ACTIVE_LOW, PULSE};
    public static final LangKey ENUM_LANG_KEY = new LangKey("ftbl.redstonemode");

    private final LangKey langKey;
    private final String name;

    EnumRedstoneMode()
    {
        name = name().toLowerCase(Locale.ENGLISH);
        langKey = new LangKey("ftbl.redstonemode." + name);
    }

    @Override
    public LangKey getLangKey()
    {
        return langKey;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public boolean isActive(boolean rsHigh)
    {
        switch(this)
        {
            case DISABLED:
                return false;
            case ACTIVE_HIGH:
                return rsHigh;
            case ACTIVE_LOW:
                return !rsHigh;
            default:
                return false;
        }
    }

    @Override
    public ITextureCoordsProvider getIcon()
    {
        switch(this)
        {
            case ACTIVE_HIGH:
                return GuiIcons.RS_HIGH;
            case ACTIVE_LOW:
                return GuiIcons.RS_LOW;
            case PULSE:
                return GuiIcons.RS_PULSE;
            default:
                return GuiIcons.RS_NONE;
        }
    }
}