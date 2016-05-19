package com.feed_the_beast.ftbl.api.permissions;

import com.feed_the_beast.ftbl.util.FTBLib;
import com.mojang.authlib.GameProfile;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by LatvianModder on 16.02.2016.
 */
public class ForgePermissionRegistry
{
    private static final Map<String, Boolean> permissions = new HashMap<>();
    private static final Map<String, RankConfig> rankConfigs = new HashMap<>();
    private static IPermissionHandler handler;

    public static void setHandler(IPermissionHandler h)
    {
        if(h != null /*&& handler == null*/)
        {
            handler = h;
        }
    }

    public static IPermissionHandler getPermissionHandler()
    {
        return handler;
    }

    public static String getID(String id)
    {
        if(id == null || id.isEmpty())
        {
            throw new NullPointerException("Permission ID can't be blank!");
        }
        else if(id.indexOf('.') == -1)
        {
            throw new IllegalArgumentException("Invalid ForgePermission: " + id + "! It must contain at least 1 '.'");
        }
        else
        {
            for(int i = 0; i < id.length(); i++)
            {
                char c = id.charAt(i);

                if(Character.isUpperCase(c))
                {
                    throw new IllegalArgumentException("Invalid ForgePermission ID: " + id + "! Can't contain uppercase letters");
                }
                else if(c == '.' || (c >= '0' && c <= '9') || (c >= 'a' || c <= 'z'))
                {
                }
                else
                {
                    throw new IllegalArgumentException("Invalid ForgePermission ID: " + id + "! Can't contain '" + c + "'");
                }
            }
        }

        return id;
    }

    public static String registerPermission(String permission, boolean defaultPlayerValue)
    {
        permission = getID(permission);

        if(rankConfigs.containsKey(permission))
        {
            throw new RuntimeException("Duplicate ForgePermission ID found: " + permission);
        }

        permissions.put(permission, defaultPlayerValue);
        return permission;
    }

    public static <E extends RankConfig> E registerRankConfig(E rankConfig)
    {
        if(rankConfigs.containsKey(rankConfig.getID()))
        {
            throw new RuntimeException("Duplicate RankConfig ID found: " + rankConfig.getID());
        }

        rankConfigs.put(rankConfig.getID(), rankConfig);
        return rankConfig;
    }

    public static Collection<String> getRegistredPermissions()
    {
        return permissions.keySet();
    }

    public static Collection<RankConfig> getRegistredConfig()
    {
        return rankConfigs.values();
    }

    public static RankConfig getConfig(String s)
    {
        return rankConfigs.get(s);
    }

    /**
     * Player can't be null, but it can be FakePlayer, if implementation supports that
     */
    public static boolean hasPermission(String permission, GameProfile profile)
    {
        if(profile == null)
        {
            throw new RuntimeException("GameProfile can't be null!");
        }

        if(ForgePermissionRegistry.getPermissionHandler() != null)
        {
            Boolean b = ForgePermissionRegistry.getPermissionHandler().handlePermission(permission, profile);

            if(b != null)
            {
                return b;
            }
        }

        if(FTBLib.isOP(profile))
        {
            return true;
        }

        return getDefaultPlayerValue(permission);
    }

    public static boolean getDefaultPlayerValue(String permission)
    {
        return permissions.containsKey(permission) && permissions.get(permission);
    }
}