package com.feed_the_beast.ftbl.lib.config;

import com.feed_the_beast.ftbl.api.config.IConfigKey;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.api.config.IGuiEditConfig;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import io.netty.buffer.ByteBuf;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

/**
 * Created by LatvianModder on 26.08.2016.
 */
public class PropertyBool extends PropertyBase
{
    private static final List<String> VARIANTS = Arrays.asList("true", "false");
    public static final String ID = "bool";

    private boolean value;

    public PropertyBool()
    {
    }

    public PropertyBool(boolean v)
    {
        value = v;
    }

    @Override
    public String getName()
    {
        return ID;
    }

    @Override
    public boolean getBoolean()
    {
        return value;
    }

    public void setBoolean(boolean v)
    {
        value = v;
    }

    @Nullable
    @Override
    public Object getValue()
    {
        return getBoolean();
    }

    @Override
    public String getString()
    {
        return value ? "true" : "false";
    }

    @Override
    public int getInt()
    {
        return getBoolean() ? 1 : 0;
    }

    @Override
    public IConfigValue copy()
    {
        return new PropertyBool(getBoolean());
    }

    @Override
    public boolean equalsValue(IConfigValue value)
    {
        return getBoolean() == value.getBoolean();
    }

    @Override
    public int getColor()
    {
        return getBoolean() ? 0x33AA33 : 0xD52834;
    }

    @Override
    public List<String> getVariants()
    {
        return VARIANTS;
    }

    @Override
    public void onClicked(IGuiEditConfig gui, IConfigKey key, IMouseButton button)
    {
        setBoolean(!getBoolean());
        gui.onChanged(key, getSerializableElement());
    }

    @Override
    public void fromJson(JsonElement json)
    {
        setBoolean(json.getAsBoolean());
    }

    @Override
    public JsonElement getSerializableElement()
    {
        return new JsonPrimitive(getBoolean());
    }

    @Override
    public void writeData(ByteBuf data)
    {
        data.writeBoolean(getBoolean());
    }

    @Override
    public void readData(ByteBuf data)
    {
        setBoolean(data.readBoolean());
    }
}