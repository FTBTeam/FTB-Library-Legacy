package com.feed_the_beast.ftbl.lib.config;

import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.lib.util.LMNetUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 26.08.2016.
 */
public class PropertyJson extends PropertyBase
{
    public static final String ID = "json";

    private JsonElement value;

    public PropertyJson()
    {
        this(new JsonObject());
    }

    public PropertyJson(JsonElement v)
    {
        value = v;
    }

    @Override
    public String getName()
    {
        return ID;
    }

    @Nullable
    @Override
    public Object getValue()
    {
        return getJsonElement();
    }

    public void setJsonElement(JsonElement v)
    {
        value = v;
    }

    public JsonElement getJsonElement()
    {
        return value;
    }

    @Override
    public void writeData(ByteBuf data)
    {
        LMNetUtils.writeJsonElement(data, getJsonElement());
    }

    @Override
    public void readData(ByteBuf data)
    {
        setJsonElement(LMNetUtils.readJsonElement(data));
    }

    @Override
    public String getString()
    {
        return getJsonElement().toString();
    }

    @Override
    public boolean getBoolean()
    {
        return getJsonElement().getAsBoolean();
    }

    @Override
    public int getInt()
    {
        return getJsonElement().getAsInt();
    }

    @Override
    public IConfigValue copy()
    {
        return new PropertyJson(getJsonElement());
    }

    @Override
    public int getColor()
    {
        return 0xFFAA49;
    }

    @Override
    public void fromJson(JsonElement json)
    {
        setJsonElement(json);
    }

    @Override
    public JsonElement getSerializableElement()
    {
        return getJsonElement();
    }
}