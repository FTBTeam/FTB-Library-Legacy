package com.feed_the_beast.ftbl.api.permissions;

import com.feed_the_beast.ftbl.FTBLibMod;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Created by LatvianModder on 24.05.2016.
 */
@ParametersAreNonnullByDefault
public class PermissionAPI
{
    private static PermissionHandler permissionHandler;

    public static void setPermissionHandler(PermissionHandler handler)
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
     * @param context          Context for this permission. Do not use null, when there is no context available, use Context.EMPTY!
     * @return true, if player has permission, false if he does not.
     */
    public static boolean hasPermission(GameProfile profile, String permission, boolean defaultForPlayer, Context context)
    {
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

        if(permissionHandler == null)
        {
            return defaultForPlayer;
        }

        switch(permissionHandler.hasPermission(profile, permission, context))
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