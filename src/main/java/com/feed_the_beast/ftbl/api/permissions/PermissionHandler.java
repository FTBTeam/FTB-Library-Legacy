package com.feed_the_beast.ftbl.api.permissions;

import com.mojang.authlib.GameProfile;
import net.minecraftforge.fml.common.eventhandler.Event;

import javax.annotation.Nonnull;

/**
 * Created by LatvianModder on 26.05.2016.
 */
public interface PermissionHandler
{
    Event.Result hasPermission(@Nonnull GameProfile profile, @Nonnull String permission, @Nonnull Context context);
}
