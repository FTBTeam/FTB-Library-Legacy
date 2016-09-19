package com.feed_the_beast.ftbl.api.info.impl;

import com.feed_the_beast.ftbl.api.info.IInfoPageTheme;
import com.feed_the_beast.ftbl.client.FTBLibClientConfig;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/**
 * Created by LatvianModder on 07.05.2016.
 */
public final class InfoPageTheme implements IInfoPageTheme
{
    public static final IInfoPageTheme DEFAULT = new InfoPageTheme(0xFFF7F4DA, 0xFF7B6534, null);
    public static final IInfoPageTheme DARK_NON_UNICODE = new InfoPageTheme(0xFF1E1E1E, 0xFFC8C8C8, false);

    private int backgroundColor;
    private int textColor;
    private Boolean useUnicodeFont;

    public InfoPageTheme()
    {
    }

    public InfoPageTheme(int bgColor, int txtColor, Boolean unicode)
    {
        backgroundColor = 0xFF000000 | bgColor;
        textColor = 0xFF000000 | txtColor;
        useUnicodeFont = unicode;
    }

    @Override
    public int getBackgroundColor()
    {
        return backgroundColor;
    }

    @Override
    public int getTextColor()
    {
        return textColor;
    }

    @Override
    public boolean getUseUnicodeFont()
    {
        return useUnicodeFont == null ? FTBLibClientConfig.INFO_UNICODE.getBoolean() : useUnicodeFont;
    }

    @Override
    public void fromJson(JsonElement json)
    {
        JsonArray a = json.getAsJsonArray();
        backgroundColor = a.get(0).getAsInt();
        textColor = a.get(1).getAsInt();
        useUnicodeFont = (a.size() >= 3) ? a.get(2).getAsBoolean() : null;
    }

    @Override
    public JsonElement getSerializableElement()
    {
        JsonArray a = new JsonArray();

        a.add(new JsonPrimitive(backgroundColor));
        a.add(new JsonPrimitive(textColor));

        if(useUnicodeFont != null)
        {
            a.add(new JsonPrimitive(useUnicodeFont));
        }

        return a;
    }
}