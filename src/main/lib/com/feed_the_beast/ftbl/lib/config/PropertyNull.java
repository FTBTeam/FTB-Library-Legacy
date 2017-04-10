package com.feed_the_beast.ftbl.lib.config;

import com.feed_the_beast.ftbl.api.config.IConfigKey;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.api.config.IGuiEditConfig;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.lib.Color4I;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import io.netty.buffer.ByteBuf;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by LatvianModder on 12.09.2016.
 */
public enum PropertyNull implements IConfigValue
{
    INSTANCE;

    public static final String ID = "null";
    public static final Color4I COLOR = new Color4I(false, 0xFF333333);

    @Override
    public String getName()
    {
        return ID;
    }

    @Override
    @Nullable
    public Object getValue()
    {
        return null;
    }

    @Override
    public String getString()
    {
        return "null";
    }

    @Override
    public boolean getBoolean()
    {
        return false;
    }

    @Override
    public int getInt()
    {
        return 0;
    }

    @Override
    public IConfigValue copy()
    {
        return INSTANCE;
    }

    @Override
    public boolean equalsValue(IConfigValue value)
    {
        return value == this;
    }

    @Override
    public Color4I getColor()
    {
        return COLOR;
    }

    @Override
    public void addInfo(IConfigKey key, List<String> list)
    {
    }

    @Override
    public void onClicked(IGuiEditConfig gui, IConfigKey key, IMouseButton button)
    {
    }

    @Override
    public void fromJson(JsonElement json)
    {
    }

    @Override
    public JsonElement getSerializableElement()
    {
        return JsonNull.INSTANCE;
    }

    @Override
    public void writeData(ByteBuf data)
    {
    }

    @Override
    public void readData(ByteBuf data)
    {
    }

    @Override
    public boolean isNull()
    {
        return true;
    }

    @Override
    public boolean setValueFromString(String text, boolean simulate)
    {
        return false;
    }
}