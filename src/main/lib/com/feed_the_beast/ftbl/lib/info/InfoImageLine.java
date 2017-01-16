package com.feed_the_beast.ftbl.lib.info;

import com.feed_the_beast.ftbl.api.gui.IImageProvider;
import com.feed_the_beast.ftbl.api.gui.IWidget;
import com.feed_the_beast.ftbl.lib.client.ImageProvider;
import com.feed_the_beast.ftbl.lib.gui.misc.GuiInfo;
import com.feed_the_beast.ftbl.lib.util.LMJsonUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.event.ClickEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LatvianModder on 23.03.2016.
 */
public class InfoImageLine extends EmptyInfoPageLine
{
    public IImageProvider imageProvider = ImageProvider.NULL;
    public int imageWidth, imageHeight;
    public double imageScale = 1D;
    public ClickEvent clickEvent;
    public List<String> hover;

    @Override
    public IWidget createWidget(GuiInfo gui, InfoPage page)
    {
        return new ButtonInfoImage(this);
    }

    @Override
    public void fromJson(JsonElement e)
    {
        imageProvider = ImageProvider.NULL;
        imageWidth = imageHeight = 0;
        imageScale = 1D;
        hover = null;

        JsonObject o = e.getAsJsonObject();

        if(!o.has("image"))
        {
            return;
        }

        imageProvider = new ImageProvider(new ResourceLocation(o.get("image").getAsString()));

        if(o.has("scale"))
        {
            imageScale = o.get("scale").getAsDouble();
        }
        else
        {
            if(o.has("width"))
            {
                imageWidth = o.get("width").getAsInt();
            }
            if(o.has("height"))
            {
                imageHeight = o.get("height").getAsInt();
            }
        }

        if(o.has("hover"))
        {
            hover = new ArrayList<>();

            for(JsonElement e1 : o.get("hover").getAsJsonArray())
            {
                hover.add(LMJsonUtils.deserializeTextComponent(e1).getFormattedText());
            }
        }
    }

    @Override
    public JsonElement getSerializableElement()
    {
        JsonObject o = new JsonObject();

        if(!imageProvider.isValid())
        {
            return o;
        }

        o.add("image", new JsonPrimitive(imageProvider.getImage().toString()));

        if(imageScale != 1D)
        {
            o.add("scale", new JsonPrimitive(imageScale));
        }
        else if(imageWidth != 0 || imageHeight != 0)
        {
            o.add("width", new JsonPrimitive(imageWidth));
            o.add("height", new JsonPrimitive(imageHeight));
        }

        if(hover != null)
        {
            JsonArray a = new JsonArray();

            for(String s : hover)
            {
                a.add(new JsonPrimitive(s));
            }

            o.add("hover", a);
        }

        return o;
    }
}