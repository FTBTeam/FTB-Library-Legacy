package com.feed_the_beast.ftbl.api.info;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.util.IJsonSerializable;

import javax.annotation.Nonnull;

/**
 * Created by LatvianModder on 07.05.2016.
 */
public final class InfoPageTheme implements IJsonSerializable
{
    public static final InfoPageTheme DEFAULT = new InfoPageTheme().setBackgroundColor(0xFFF7F4DA).setTextColor(0xFF7B6534).setUseUnicodeFont(null);
    public static final InfoPageTheme DARK_NON_UNICODE = new InfoPageTheme().setBackgroundColor(0xFF1E1E1E).setTextColor(0xFFC8C8C8).setUseUnicodeFont(false);

    public Integer backgroundColor;
    public Integer textColor;
    public Boolean useUnicodeFont;

    public InfoPageTheme setBackgroundColor(Integer col)
    {
        backgroundColor = col;
        return this;
    }

    public InfoPageTheme setTextColor(Integer col)
    {
        textColor = col;
        return this;
    }

    public InfoPageTheme setUseUnicodeFont(Boolean b)
    {
        useUnicodeFont = b;
        return this;
    }

    public InfoPageTheme copy()
    {
        return null;
    }

    @Override
    public void fromJson(@Nonnull JsonElement json)
    {
        JsonObject o = json.getAsJsonObject();
        backgroundColor = o.has("CBG") ? o.get("CBG").getAsInt() : null;
        textColor = o.has("CT") ? o.get("CT").getAsInt() : null;
        useUnicodeFont = o.has("UUF") ? o.get("UUF").getAsBoolean() : null;
    }

    @Nonnull
    @Override
    public JsonElement getSerializableElement()
    {
        JsonObject o = new JsonObject();

        if(backgroundColor != null)
        {
            o.add("CBG", new JsonPrimitive(backgroundColor));
        }
        if(textColor != null)
        {
            o.add("CT", new JsonPrimitive(textColor));
        }
        if(useUnicodeFont != null)
        {
            o.add("UUF", new JsonPrimitive(useUnicodeFont));
        }

        return o;
    }
}