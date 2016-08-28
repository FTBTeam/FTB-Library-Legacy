package com.feed_the_beast.ftbl.api.tile;

import com.feed_the_beast.ftbl.api.gui.GuiIcons;
import com.latmod.lib.ILangKeyContainer;
import com.latmod.lib.LangKey;
import com.latmod.lib.TextureCoords;
import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum EnumRedstoneMode implements IStringSerializable, ILangKeyContainer
{
    DISABLED,
    ACTIVE_HIGH,
    ACTIVE_LOW;

    public static final EnumRedstoneMode[] VALUES = values();
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

    public boolean cancel(boolean b)
    {
        return this != DISABLED && (this == ACTIVE_HIGH && !b || this == ACTIVE_LOW && b);
    }

    public TextureCoords getIcon()
    {
        return GuiIcons.REDSTONE[ordinal()];
    }
}