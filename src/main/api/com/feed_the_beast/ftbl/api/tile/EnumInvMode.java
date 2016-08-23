package com.feed_the_beast.ftbl.api.tile;

import com.feed_the_beast.ftbl.api.gui.GuiIcons;
import com.latmod.lib.EnumNameMap;
import com.latmod.lib.ILangKeyContainer;
import com.latmod.lib.LangKey;
import com.latmod.lib.TextureCoords;
import net.minecraft.util.IStringSerializable;

import javax.annotation.Nonnull;

public enum EnumInvMode implements IStringSerializable, ILangKeyContainer
{
    ENABLED,
    ONLY_IN,
    ONLY_OUT,
    DISABLED;

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
    @Nonnull
    public String getName()
    {
        return name;
    }

    @Override
    @Nonnull
    public LangKey getLangKey()
    {
        return langKey;
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