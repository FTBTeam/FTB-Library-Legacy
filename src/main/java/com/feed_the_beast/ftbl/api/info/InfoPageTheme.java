package com.feed_the_beast.ftbl.api.info;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import latmod.lib.LMColor;
import net.minecraft.util.IJsonSerializable;

/**
 * Created by LatvianModder on 07.05.2016.
 */
public final class InfoPageTheme implements IJsonSerializable
{
    public static final InfoPageTheme DEFAULT = new InfoPageTheme().setBackgroundColor(new LMColor.RGB(247, 244, 218)).setTextColor(new LMColor.RGB(123, 101, 52)).setUseUnicodeFont(null);
    
    public LMColor.RGB backgroundColor;
    public LMColor.RGB textColor;
    public Boolean useUnicodeFont;
    
    public InfoPageTheme setBackgroundColor(LMColor.RGB col)
    {
        backgroundColor = col;
        return this;
    }
    
    public InfoPageTheme setTextColor(LMColor.RGB col)
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
    public void fromJson(JsonElement json)
    {
        JsonObject o = json.getAsJsonObject();
        backgroundColor = o.has("CBG") ? new LMColor.ImmutableColor(o.get("CBG").getAsInt()) : null;
        textColor = o.has("CT") ? new LMColor.ImmutableColor(o.get("CT").getAsInt()) : null;
        useUnicodeFont = o.has("UUF") ? o.get("UUF").getAsBoolean() : null;
    }
    
    @Override
    public JsonElement getSerializableElement()
    {
        JsonObject o = new JsonObject();
        
        if(backgroundColor != null) { o.add("CBG", new JsonPrimitive(backgroundColor.color())); }
        if(textColor != null) { o.add("CT", new JsonPrimitive(textColor.color())); }
        if(useUnicodeFont != null) { o.add("UUF", new JsonPrimitive(useUnicodeFont)); }
        
        return o;
    }
}