package com.feed_the_beast.ftbl.api.permissions;

import com.feed_the_beast.ftbl.api.permissions.context.IContext;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.annotation.Nullable;

/**
 * Default implementation of PermissionAPI.
 * {@link #hasPermission(GameProfile, String, IContext)} is based on DefaultPermissionLevel
 *
 * @see PermissionAPI#hasPermission(GameProfile, String, IContext)
 * @see PermissionAPI#registerPermission(String, DefaultPermissionLevel, String...)
 */
public enum DefaultPermissionHandler implements IPermissionHandler
{
    INSTANCE;

    @Override
    public boolean hasPermission(GameProfile profile, String permission, @Nullable IContext context)
    {
        switch(PermissionAPI.getDefaultPermissionLevel(permission))
        {
            case NONE:
                return false;
            case ALL:
                return true;
            default:
            {
                MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
                return server != null && server.getPlayerList().canSendCommands(profile);
            }
        }
    }
}