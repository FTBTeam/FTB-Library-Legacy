package com.feed_the_beast.ftbl.api;

import com.feed_the_beast.ftbl.FTBLibPermissions;
import com.feed_the_beast.ftbl.api.client.gui.GuiIcons;
import com.feed_the_beast.ftbl.api.permissions.Context;
import com.feed_the_beast.ftbl.api.permissions.PermissionAPI;
import com.feed_the_beast.ftbl.util.TextureCoords;

public enum EnumPrivacyLevel
{
    PUBLIC,
    PRIVATE,
    TEAM;

    public static final EnumPrivacyLevel[] VALUES = values();
    public static final String enumLangKey = "ftbl.security";

    public final String langKey;

    EnumPrivacyLevel()
    {
        langKey = enumLangKey + '.' + name().toLowerCase();
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

    public boolean canInteract(ForgePlayer owner, ForgePlayer player, Context context)
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
        else if(player.isOnline() && PermissionAPI.hasPermission(player.getProfile(), FTBLibPermissions.INTERACT_SECURE, false, context))
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