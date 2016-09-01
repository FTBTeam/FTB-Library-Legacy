package com.feed_the_beast.ftbl.api.rankconfig;

import com.feed_the_beast.ftbl.api.config.ConfigEntryType;
import com.google.gson.JsonPrimitive;
import com.latmod.lib.EnumNameMap;
import com.mojang.authlib.GameProfile;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 14.02.2016.
 */
public class RankConfigEnum<E extends Enum<E>> extends RankConfig
{
    public final EnumNameMap<E> nameMap;

    public RankConfigEnum(String id, @Nullable E defPlayerValue, @Nullable E defOPValue, EnumNameMap<E> m)
    {
        super(id, new JsonPrimitive(EnumNameMap.getEnumName(defPlayerValue)), new JsonPrimitive(EnumNameMap.getEnumName(defOPValue)));
        nameMap = m;
    }

    @Nullable
    public E get(GameProfile profile)
    {
        return nameMap.get(getJson(profile).getAsString());
    }

    @Override
    public ConfigEntryType getType()
    {
        return ConfigEntryType.ENUM;
    }
}