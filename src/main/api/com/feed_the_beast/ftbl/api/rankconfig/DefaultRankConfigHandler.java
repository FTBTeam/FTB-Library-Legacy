package com.feed_the_beast.ftbl.api.rankconfig;

import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.latmod.lib.util.LMServerUtils;
import com.mojang.authlib.GameProfile;

/**
 * Created by LatvianModder on 14.09.2016.
 */
public enum DefaultRankConfigHandler implements IRankConfigHandler
{
    INSTANCE;

    @Override
    public IConfigValue getRankConfig(GameProfile profile, IRankConfig id)
    {
        return LMServerUtils.isOP(profile) ? id.getDefaultOPValue() : id.getDefaultValue();
    }
}
