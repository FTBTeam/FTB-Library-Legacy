package com.feed_the_beast.ftbl.api.security;

import com.feed_the_beast.ftbl.api.LangKey;
import com.feed_the_beast.ftbl.api.config.EnumNameMap;
import com.feed_the_beast.ftbl.api.gui.GuiIcons;
import com.latmod.lib.TextureCoords;

public enum EnumPrivacyLevel
{
    PUBLIC,
    PRIVATE,
    TEAM;

    public static final EnumPrivacyLevel[] VALUES = values();
    public static final LangKey enumLangKey = new LangKey("ftbl.privacy");
    public static final EnumNameMap<EnumPrivacyLevel> NAME_MAP = new EnumNameMap<>(false, EnumPrivacyLevel.VALUES);

    public final LangKey langKey;

    EnumPrivacyLevel()
    {
        langKey = new LangKey("ftbl.privacy." + name().toLowerCase());
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

    public TextureCoords getIcon()
    {
        return GuiIcons.security[ordinal()];
    }

    public boolean isPublic()
    {
        return this == PUBLIC;
    }
}