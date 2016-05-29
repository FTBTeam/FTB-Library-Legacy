package com.feed_the_beast.ftbl.util;

import com.feed_the_beast.ftbl.FTBLibPermissions;
import com.feed_the_beast.ftbl.api.ForgePlayer;
import com.feed_the_beast.ftbl.api.gui.GuiIcons;
import com.feed_the_beast.ftbl.api.permissions.Context;
import com.feed_the_beast.ftbl.api.permissions.PermissionAPI;

public enum PrivacyLevel
{
    PUBLIC,
    PRIVATE,
    TEAM;

    public static final PrivacyLevel[] VALUES_3 = new PrivacyLevel[] {PUBLIC, PRIVATE, TEAM};
    public static final PrivacyLevel[] VALUES_2 = new PrivacyLevel[] {PUBLIC, PRIVATE};
    public static final String enumLangKey = "ftbl.security";

    public final String langKey;

    PrivacyLevel()
    {
        langKey = enumLangKey + '.' + name().toLowerCase();
    }

    public static PrivacyLevel get(String s)
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
        if(this == PrivacyLevel.PUBLIC || owner == null)
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
        else if(this == PrivacyLevel.PRIVATE)
        {
            return false;
        }

        return this == PrivacyLevel.TEAM && owner.hasTeam() && owner.getTeam().getStatus(player).isAlly();
    }
}