package com.feed_the_beast.ftbl.api.permissions.rankconfig;

import com.feed_the_beast.ftbl.api.config.ConfigEntryType;
import com.google.gson.JsonPrimitive;
import com.mojang.authlib.GameProfile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by LatvianModder on 14.02.2016.
 */
public class RankConfigEnum<E extends Enum<E>> extends RankConfig
{
    private final Map<String, E> enumMap;

    public RankConfigEnum(String id, @Nullable E defPlayerValue, @Nullable E defOPValue, @Nonnull E[] validEnums, boolean addNull)
    {
        super(id, new JsonPrimitive(getName(defPlayerValue)), new JsonPrimitive(getName(defOPValue)));
        enumMap = new HashMap<>();

        for(E e : validEnums)
        {
            enumMap.put(getName(e), e);
        }

        if(addNull)
        {
            enumMap.put("-", null);
        }
    }

    private static String getName(Enum<?> e)
    {
        return e == null ? "-" : e.name().toLowerCase();
    }

    public E get(@Nonnull GameProfile profile)
    {
        return enumMap.get(getJson(profile).getAsString());
    }

    @Override
    public ConfigEntryType getType()
    {
        return ConfigEntryType.ENUM;
    }
}