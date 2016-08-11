package com.feed_the_beast.ftbl.api.rankconfig;

import com.feed_the_beast.ftbl.api.config.ConfigEntryType;
import com.google.gson.JsonPrimitive;
import com.mojang.authlib.GameProfile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 25.05.2016.
 */
public final class RankConfigString extends RankConfig
{
    public RankConfigString(String id, @Nullable String player, @Nullable String op)
    {
        super(id, new JsonPrimitive(player), new JsonPrimitive(op));
    }

    public void setDefaultValue(boolean op, @Nonnull String value)
    {
        setDefaultValue(op, new JsonPrimitive(value));
    }

    public String get(@Nonnull GameProfile profile)
    {
        return getJson(profile).getAsString();
    }

    @Override
    public ConfigEntryType getType()
    {
        return ConfigEntryType.STRING;
    }
}