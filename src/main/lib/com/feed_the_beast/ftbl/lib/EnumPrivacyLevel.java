package com.feed_the_beast.ftbl.lib;

import com.feed_the_beast.ftbl.api.gui.IDrawableObject;
import com.feed_the_beast.ftbl.lib.gui.GuiIcons;
import net.minecraft.util.IStringSerializable;

public enum EnumPrivacyLevel implements IStringSerializable
{
    PUBLIC("public"),
    PRIVATE("private"),
    TEAM("team");

    public static final EnumPrivacyLevel[] VALUES = values();
    public static final LangKey ENUM_LANG_KEY = new LangKey("ftbl.privacy");
    public static final EnumNameMap<EnumPrivacyLevel> NAME_MAP = new EnumNameMap<>(EnumPrivacyLevel.VALUES, false);

    private final String name;
    private final LangKey langKey;

    EnumPrivacyLevel(String n)
    {
        name = n;
        langKey = new LangKey("ftbl.privacy." + name);
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
            case PRIVATE:
                return GuiIcons.SECURITY_PRIVATE;
            case TEAM:
                return GuiIcons.SECURITY_TEAM;
            default:
                return GuiIcons.SECURITY_PUBLIC;
        }
    }

    public boolean isPublic()
    {
        return this == PUBLIC;
    }
}