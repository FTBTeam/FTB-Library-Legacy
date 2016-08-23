package com.feed_the_beast.ftbl.api.security;

import com.feed_the_beast.ftbl.api.gui.GuiIcons;
import com.latmod.lib.EnumNameMap;
import com.latmod.lib.ILangKeyContainer;
import com.latmod.lib.LangKey;
import com.latmod.lib.TextureCoords;
import net.minecraft.util.IStringSerializable;

import javax.annotation.Nonnull;

public enum EnumPrivacyLevel implements IStringSerializable, ILangKeyContainer
{
    PUBLIC,
    PRIVATE,
    TEAM;

    public static final EnumPrivacyLevel[] VALUES = values();
    public static final LangKey enumLangKey = new LangKey("ftbl.privacy");
    public static final EnumNameMap<EnumPrivacyLevel> NAME_MAP = new EnumNameMap<>(false, EnumPrivacyLevel.VALUES);

    private final String name;
    private final LangKey langKey;

    EnumPrivacyLevel()
    {
        name = EnumNameMap.createName(this);
        langKey = new LangKey("ftbl.privacy." + name);
    }

    public static EnumPrivacyLevel get(String s)
    {
        if(s == null || s.isEmpty())
        {
            return PUBLIC;
        }
        else if(s.equalsIgnoreCase("team"))
        {
            return TEAM;
        }
        else if(s.equalsIgnoreCase("private"))
        {
            return PRIVATE;
        }
        return PUBLIC;
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
        return GuiIcons.security[ordinal()];
    }

    public boolean isPublic()
    {
        return this == PUBLIC;
    }
}