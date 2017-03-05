package com.feed_the_beast.ftbl.lib.config;

import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.lib.util.LMColorUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import io.netty.buffer.ByteBuf;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 26.08.2016.
 */
public class PropertyColor extends PropertyBase
{
    public static final String ID = "color";

    private int value;

    public PropertyColor()
    {
    }

    public PropertyColor(int v)
    {
        value = v;
    }

    @Override
    public String getName()
    {
        return ID;
    }

    public int getColorValue()
    {
        return value;
    }

    public void setColorValue(int v)
    {
        value = v;
    }

    @Nullable
    @Override
    public Object getValue()
    {
        return getColorValue();
    }

    @Override
    public String getString()
    {
        return LMColorUtils.getHex(getColorValue());
    }

    @Override
    public boolean getBoolean()
    {
        return getColorValue() != 0;
    }

    @Override
    public int getInt()
    {
        return getColorValue();
    }

    @Override
    public IConfigValue copy()
    {
        return new PropertyColor(getColorValue());
    }

    @Override
    public int getColor()
    {
        return 0xFF000000 | getColorValue();
    }

    @Override
    public boolean setValueFromString(String text, boolean simulate)
    {
        try
        {
            if(text.indexOf(',') != -1)
            {
                if(text.length() < 5)
                {
                    return false;
                }

                String[] s = text.split(",");

                if(s.length == 3 || s.length == 4)
                {
                    int c[] = new int[4];
                    c[3] = 255;

                    for(int i = 0; i < s.length; i++)
                    {
                        c[i] = Integer.parseInt(s[i]);
                    }

                    if(!simulate)
                    {
                        setColorValue(LMColorUtils.getRGBA(c[0], c[1], c[2], c[3]));
                    }

                    return true;
                }
            }
            else
            {
                if(text.length() < 6)
                {
                    return false;
                }
                else if(text.startsWith("#"))
                {
                    text = text.substring(1);
                }

                int hex = Integer.parseInt(text, 16);

                if(!simulate)
                {
                    setColorValue(hex);
                }

                return true;
            }
        }
        catch(Exception ex)
        {
        }

        return false;
    }

    @Override
    public void fromJson(JsonElement json)
    {
        setColorValue(json.getAsInt());
    }

    @Override
    public JsonElement getSerializableElement()
    {
        return new JsonPrimitive(getInt());
    }

    @Override
    public void writeData(ByteBuf data)
    {
        data.writeInt(getInt());
    }

    @Override
    public void readData(ByteBuf data)
    {
        setColorValue(data.readInt());
    }
}