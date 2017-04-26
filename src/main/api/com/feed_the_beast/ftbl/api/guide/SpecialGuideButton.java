package com.feed_the_beast.ftbl.api.guide;

import com.feed_the_beast.ftbl.api.gui.IDrawableObject;
import com.feed_the_beast.ftbl.lib.client.ImageProvider;
import com.feed_the_beast.ftbl.lib.util.JsonUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.ClickEvent;

/**
 * Created by LatvianModder on 02.09.2016.
 */
public class SpecialGuideButton
{
    public final ITextComponent title;
    public final IDrawableObject icon;
    public final ClickEvent clickEvent;

    public SpecialGuideButton(ITextComponent t, IDrawableObject icn, ClickEvent c)
    {
        title = t;
        icon = icn;
        clickEvent = c;
    }

    public SpecialGuideButton(JsonObject o)
    {
        title = o.has("title") ? JsonUtils.deserializeTextComponent(o.get("title")) : new TextComponentString("");
        icon = o.has("icon") ? ImageProvider.get(o.get("icon").getAsString()) : ImageProvider.NULL;
        clickEvent = o.has("clickEvent") ? JsonUtils.deserializeClickEvent(o.get("clickEvent")) : null;
    }

    public JsonObject serialize()
    {
        JsonObject o = new JsonObject();
        o.add("title", JsonUtils.serializeTextComponent(title));
        o.add("icon", new JsonPrimitive(icon.getImage().toString()));
        if(clickEvent != null)
        {
            o.add("clickEvent", JsonUtils.serializeClickEvent(clickEvent));
        }
        return o;
    }
}