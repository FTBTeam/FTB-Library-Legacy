package com.feed_the_beast.ftbl.api.permissions;

import com.google.gson.JsonElement;
import com.mojang.authlib.GameProfile;

/**
 * Created by LatvianModder on 13.02.2016.
 */
public interface IPermissionHandler
{
    /**
     * Return null for default value, otherwise override to true of false
     */
    Boolean handlePermission(String permission, GameProfile profile);

    /**
     * Return null or JsonNull.INSTANCE for default value, otherwise override
     */
    JsonElement handleRankConfig(RankConfig config, GameProfile profile);
}
