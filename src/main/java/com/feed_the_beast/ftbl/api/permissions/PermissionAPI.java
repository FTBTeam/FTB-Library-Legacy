package com.feed_the_beast.ftbl.api.permissions;

import com.feed_the_beast.ftbl.FTBLibMod;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.Event;

import javax.annotation.Nonnull;

/**
 * Created by LatvianModder on 24.05.2016.
 */
public class PermissionAPI
{
    private static Handler permissionHandler;

    public interface Handler
    {
        Event.Result hasPermission(@Nonnull GameProfile profile, @Nonnull String permission);
    }

    public static void setHandler(@Nonnull Handler handler)
    {
        if(permissionHandler != null)
        {
            FTBLibMod.logger.warn("Replacing " + permissionHandler.getClass().getName() + " with " + handler.getClass().getName());
        }

        permissionHandler = handler;
    }

    /**
     * @param profile          GameProfile of the player who is requesting permission
     * @param permission       Permission node, best if lowercase and contains '.'
     * @param defaultForPlayer Default value for players
     * @return true, if player has permission, false if he does not.
     */
    public static boolean hasPermission(@Nonnull GameProfile profile, @Nonnull String permission, boolean defaultForPlayer)
    {
        if(permissionHandler == null)
        {
            return defaultForPlayer;
        }

        if(permission.isEmpty())
        {
            throw new NullPointerException("Permission string can't be empty!");
        }

        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();

        if(server == null)
        {
            return true;
        }

        if(!server.isDedicatedServer() || server.getPlayerList().getOppedPlayers().getPermissionLevel(profile) > 0)
        {
            return true;
        }

        switch(permissionHandler.hasPermission(profile, permission))
        {
            case ALLOW:
            {
                return true;
            }
            case DENY:
            {
                return false;
            }
            default:
            {
                return defaultForPlayer;
            }
        }
    }
}