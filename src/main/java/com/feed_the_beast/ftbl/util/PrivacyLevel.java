package com.feed_the_beast.ftbl.util;

import com.feed_the_beast.ftbl.FTBLibPermissions;
import com.feed_the_beast.ftbl.api.ForgePlayer;
import com.feed_the_beast.ftbl.api.gui.GuiIcons;
import com.feed_the_beast.ftbl.api.permissions.PermissionAPI;

public enum PrivacyLevel
{
    PUBLIC,
    PRIVATE,
    FRIENDS;

    public static final PrivacyLevel[] VALUES_3 = new PrivacyLevel[] {PUBLIC, PRIVATE, FRIENDS};
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
        else if(s.equalsIgnoreCase("friends"))
        {
            return FRIENDS;
        }
        else if(s.equalsIgnoreCase("private"))
        {
            return PRIVATE;
        }
        return PUBLIC;
    }

    public boolean isPublic()
    {
        return this == PUBLIC;
    }

    public boolean isRestricted()
    {
        return this == FRIENDS;
    }

    public PrivacyLevel next(PrivacyLevel[] l)
    {
        return l[(ordinal() + 1) % l.length];
    }

    public PrivacyLevel prev(PrivacyLevel[] l)
    {
        int id = ordinal() - 1;
        if(id < 0)
        {
            id = l.length - 1;
        }
        return l[id];
    }

    public TextureCoords getIcon()
    {
        return GuiIcons.security[ordinal()];
    }

    public boolean canInteract(ForgePlayer owner, ForgePlayer player)
    {
        if(FTBLib.ftbu == null)
        {
            return true;
        }
        if(this == PrivacyLevel.PUBLIC || owner == null)
        {
            return true;
        }
        if(player == null)
        {
            return false;
        }
        if(owner.equalsPlayer(player))
        {
            return true;
        }
        if(player.isOnline() && PermissionAPI.hasPermission(player.getProfile(), FTBLibPermissions.interact_secure, false))
        {
            return true;
        }
        if(this == PrivacyLevel.PRIVATE)
        {
            return false;
        }
        return this == PrivacyLevel.FRIENDS && owner.isFriend(player);
    }
}