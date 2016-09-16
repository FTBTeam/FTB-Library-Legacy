package com.feed_the_beast.ftbl.api.permissions;

import com.feed_the_beast.ftbl.api.permissions.context.IContext;
import com.mojang.authlib.GameProfile;

import javax.annotation.Nullable;

public interface IPermissionHandler
{
    /**
     * @see PermissionAPI#hasPermission(GameProfile, String, IContext)
     */
    boolean hasPermission(GameProfile profile, String permission, @Nullable IContext context);
}