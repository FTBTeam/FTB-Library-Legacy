package com.feed_the_beast.ftbl.lib.info;

import com.feed_the_beast.ftbl.api.RegistryObject;
import com.feed_the_beast.ftbl.api.gui.IWidget;
import com.feed_the_beast.ftbl.api.info.IGuiInfoPage;
import com.feed_the_beast.ftbl.api.info.IImageProvider;
import com.feed_the_beast.ftbl.api.info.IInfoTextLineProvider;
import com.feed_the_beast.ftbl.gui.GuiInfo;
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
    @RegistryObject("image")
    public static final IInfoTextLineProvider PROVIDER = (page, json) -> new InfoImageLine();

    private String imageURL;
    IImageProvider imageProvider;
    int imageWidth, imageHeight;
    double imageScale = 1D;
    ClickEvent clickEvent;
    List<String> hover;

    public InfoImageLine()
    {
    }

    public InfoImageLine(IImageProvider img, int w, int h)
    {
        this();
        imageProvider = img;
        imageWidth = w;
        imageHeight = h;
    }

    public InfoImageLine setImage(IImageProvider img)
    {
        imageProvider = img;
        return this;
    }

    public InfoImageLine setDisplaySize(int w, int h)
    {
        imageWidth = w;
        imageHeight = h;
        return this;
    }

    @Override
    public IWidget createWidget(GuiInfo gui, IGuiInfoPage page)
    {
        return new ButtonInfoImage(gui, this);
    }

    @Override
    public void fromJson(JsonElement e)
    {
        imageWidth = imageHeight = 64;
        imageScale = 1D;
        hover = null;

        JsonObject o = e.getAsJsonObject();
        boolean loadFromURL = o.has("image_url") && o.get("image_url").getAsBoolean();
        setImage(o.has("image") ? (loadFromURL ? new URLImageProvider(o.get("image").getAsString()) : new WrappedImageProvider(new ResourceLocation(o.get("image").getAsString()))) : EmptyImageProvider.INSTANCE);

        if(o.has("width"))
        {
            imageWidth = o.get("width").getAsInt();
        }

        if(o.has("height"))
        {
            imageHeight = o.get("height").getAsInt();
        }

        if(o.has("scale"))
        {
            imageScale = o.get("scale").getAsDouble();
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

        if(imageURL != null && !imageURL.isEmpty())
        {
            o.add("image", new JsonPrimitive(imageURL));
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

        //FIXME: width, height, scale

        return o;
    }
}