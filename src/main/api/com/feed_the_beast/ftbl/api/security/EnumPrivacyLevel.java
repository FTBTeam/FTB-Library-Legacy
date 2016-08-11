package com.feed_the_beast.ftbl.api.security;

import com.feed_the_beast.ftbl.api.ForgePlayer;
import com.feed_the_beast.ftbl.api.LangKey;
import com.feed_the_beast.ftbl.api.client.gui.GuiIcons;
import com.feed_the_beast.ftbl.api.config.EnumNameMap;
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

    public boolean canInteract(ForgePlayer owner, ForgePlayer player)
    {
        if(this == EnumPrivacyLevel.PUBLIC || owner == null)
        {
            return true;
        }
        else if(player == null)
        {
            return false;
        }
        else if(owner.equalsPlayer(player))
        {
            return true;
        }
        else if(this == EnumPrivacyLevel.PRIVATE)
        {
            return false;
        }

        return this == EnumPrivacyLevel.TEAM && owner.hasTeam() && owner.getTeam().getStatus(player).isAlly();
    }
}