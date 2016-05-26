package com.feed_the_beast.ftbl.api.permissions.rankconfig;

import com.feed_the_beast.ftbl.api.config.ConfigEntryType;
import com.google.gson.JsonPrimitive;
import com.mojang.authlib.GameProfile;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 25.05.2016.
 */
public final class RankConfigInt extends RankConfig
{
    private final int minVal, maxVal;

    public RankConfigInt(String id, int player, int op, @Nullable Integer min, @Nullable Integer max)
    {
        super(id, new JsonPrimitive(player), new JsonPrimitive(op));
        minVal = min == null ? Integer.MIN_VALUE : min;
        maxVal = max == null ? Integer.MAX_VALUE : max;
    }

    public void setDefaultValue(boolean op, int value)
    {
        setDefaultValue(op, new JsonPrimitive(value));
    }

    public int get(@Nonnull GameProfile profile)
    {
        return MathHelper.clamp_int(getJson(profile).getAsInt(), minVal, maxVal);
    }

    @Override
    public ConfigEntryType getType()
    {
        return ConfigEntryType.INT;
    }
}