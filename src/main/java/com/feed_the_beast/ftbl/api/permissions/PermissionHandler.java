package com.feed_the_beast.ftbl.api.permissions;

import com.mojang.authlib.GameProfile;
import net.minecraftforge.fml.common.eventhandler.Event;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Created by LatvianModder on 26.05.2016.
 */
@ParametersAreNonnullByDefault
public interface PermissionHandler
{
    Event.Result hasPermission(GameProfile profile, String permission, Context context);
}
