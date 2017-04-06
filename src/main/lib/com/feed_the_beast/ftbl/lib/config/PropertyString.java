package com.feed_the_beast.ftbl.lib.config;

import com.feed_the_beast.ftbl.api.config.IConfigKey;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.lib.Color4I;
import com.feed_the_beast.ftbl.lib.ImmutableColor4I;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by LatvianModder on 26.08.2016.
 */
public class PropertyString extends PropertyBase
{
    public static final String ID = "string";
    public static final Color4I COLOR = new ImmutableColor4I(0xFFFFAA49);

    private String value;
    private int charLimit;

    public PropertyString()
    {
        this("");
    }

    public PropertyString(String v)
    {
        this(v, 0);
    }

    public PropertyString(String v, int limit)
    {
        value = v;
        charLimit = limit;
    }

    @Override
    public String getName()
    {
        return ID;
    }

    @Override
    public String getString()
    {
        return value;
    }

    public void setString(String v)
    {
        value = v;
    }

    @Nullable
    @Override
    public Object getValue()
    {
        return getString();
    }

    @Override
    public boolean getBoolean()
    {
        return getString().equals("true");
    }

    @Override
    public int getInt()
    {
        return Integer.parseInt(getString());
    }

    @Override
    public IConfigValue copy()
    {
        return new PropertyString(getString(), charLimit);
    }

    @Override
    public Color4I getColor()
    {
        return COLOR;
    }

    @Override
    public boolean setValueFromString(String text, boolean simulate)
    {
        setString(text);
        return true;
    }

    @Override
    public void addInfo(IConfigKey key, List<String> list)
    {
        super.addInfo(key, list);

        if(charLimit > 0)
        {
            list.add(TextFormatting.AQUA + "Char Limit: " + charLimit);
        }
    }

    @Override
    public void fromJson(JsonElement json)
    {
        setString(json.getAsString());
    }

    @Override
    public JsonElement getSerializableElement()
    {
        return new JsonPrimitive(getString());
    }

    @Override
    public void writeData(ByteBuf data)
    {
        ByteBufUtils.writeUTF8String(data, getString());
        data.writeShort(charLimit);
    }

    @Override
    public void readData(ByteBuf data)
    {
        setString(ByteBufUtils.readUTF8String(data));
        charLimit = data.readUnsignedShort();
    }
}