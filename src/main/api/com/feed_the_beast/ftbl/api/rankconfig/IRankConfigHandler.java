package com.feed_the_beast.ftbl.api.rankconfig;

import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.mojang.authlib.GameProfile;

/**
 * Created by LatvianModder on 14.09.2016.
 */
public interface IRankConfigHandler
{
    IConfigValue getRankConfig(GameProfile profile, IRankConfig id);
}