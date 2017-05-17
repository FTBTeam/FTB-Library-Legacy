package com.feed_the_beast.ftbl.lib;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.util.IJsonSerializable;

/**
 * @author LatvianModder
 */
public class StatsLongValue implements IJsonSerializable
{
    private long value;

    public StatsLongValue set(long v)
    {
        value = v;
        return this;
    }

    public long get()
    {
        return value;
    }

    @Override
    public final void fromJson(JsonElement json)
    {
        set(json.getAsLong());
    }

    @Override
    public final JsonElement getSerializableElement()
    {
        return new JsonPrimitive(get());
    }
}