package com.feed_the_beast.ftbl.api.permissions;

import com.mojang.authlib.GameProfile;
import net.minecraftforge.fml.common.eventhandler.Event;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Created by LatvianModder on 26.05.2016.
 */
@ParametersAreNonnullByDefault
public interface PermissionHandler
{
    /**
     * @param profile    Profile of player who's permission is requested
     * @param permission Permission node ID
     * @param context    Context
     * @return ALLOW if player has permission, DENY if he does not. DEFAULT if default permission should be used
     */
    @Nonnull
    Event.Result hasPermission(GameProfile profile, String permission, Context context);
}