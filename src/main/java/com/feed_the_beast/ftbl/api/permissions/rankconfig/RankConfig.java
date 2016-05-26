package com.feed_the_beast.ftbl.api.permissions.rankconfig;

import com.feed_the_beast.ftbl.api.config.ConfigEntryType;
import com.feed_the_beast.ftbl.util.FTBLib;
import com.google.gson.JsonElement;
import com.mojang.authlib.GameProfile;
import latmod.lib.util.FinalIDObject;

import javax.annotation.Nonnull;

/**
 * Created by LatvianModder on 13.02.2016.
 */
public class RankConfig extends FinalIDObject
{
    private JsonElement defaultPlayerValue;
    private JsonElement defaultOPValue;

    public RankConfig(String id, @Nonnull JsonElement player, @Nonnull JsonElement op)
    {
        super(id);
        setDefaultValue(false, player);
        setDefaultValue(true, op);
    }

    public final void setDefaultValue(boolean op, @Nonnull JsonElement value)
    {
        if(op)
        {
            defaultOPValue = value;
        }
        else
        {
            defaultPlayerValue = value;
        }
    }

    public final JsonElement getDefaultValue(boolean op)
    {
        return op ? defaultOPValue : defaultPlayerValue;
    }

    public JsonElement getJson(@Nonnull GameProfile profile)
    {
        if(RankConfigAPI.rankConfigHandler != null)
        {
            return RankConfigAPI.rankConfigHandler.getRankConfig(profile, this);
        }

        return getDefaultValue(FTBLib.isOP(profile));
    }

    public ConfigEntryType getType()
    {
        return ConfigEntryType.CUSTOM;
    }
}