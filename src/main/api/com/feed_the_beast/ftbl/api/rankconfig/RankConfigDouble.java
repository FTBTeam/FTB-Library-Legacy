package com.feed_the_beast.ftbl.api.rankconfig;

import com.feed_the_beast.ftbl.api.config.ConfigEntryType;
import com.google.gson.JsonPrimitive;
import com.mojang.authlib.GameProfile;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 25.05.2016.
 */
public final class RankConfigDouble extends RankConfig
{
    private final double minVal, maxVal;

    public RankConfigDouble(String id, double player, double op, @Nullable Double min, @Nullable Double max)
    {
        super(id, new JsonPrimitive(player), new JsonPrimitive(op));
        minVal = min == null ? Integer.MIN_VALUE : min;
        maxVal = max == null ? Integer.MAX_VALUE : max;
    }

    public void setDefaultValue(boolean op, double value)
    {
        setDefaultValue(op, new JsonPrimitive(value));
    }

    public double get(@Nonnull GameProfile profile)
    {
        return MathHelper.clamp_double(getJson(profile).getAsDouble(), minVal, maxVal);
    }

    @Override
    public ConfigEntryType getType()
    {
        return ConfigEntryType.DOUBLE;
    }
}