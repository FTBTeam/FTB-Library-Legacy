package com.feed_the_beast.ftbl.api.info.impl;

import com.feed_the_beast.ftbl.api.gui.widgets.ButtonLM;
import com.feed_the_beast.ftbl.api.info.IGuiInfoPage;
import com.feed_the_beast.ftbl.api.info.IImageProvider;
import com.feed_the_beast.ftbl.gui.GuiInfo;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 23.03.2016.
 */
public class InfoImageLine extends InfoExtendedTextLine
{
    public String imageURL;
    private IImageProvider imageProvider;
    private int imageWidth, imageHeight;
    private double imageScale;

    public InfoImageLine()
    {
        super(null);
    }

    public InfoImageLine(IImageProvider img, int w, int h)
    {
        this();
        imageProvider = img;
        imageWidth = w;
        imageHeight = h;
    }

    public IImageProvider getImageProvider()
    {
        return imageProvider;
    }

    public int getImageWidth()
    {
        return imageWidth;
    }

    public int getImageHeight()
    {
        return imageHeight;
    }

    public double getImageScale()
    {
        return imageScale;
    }

    public InfoImageLine setImage(@Nullable IImageProvider img)
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
    public ButtonLM createWidget(GuiInfo gui, IGuiInfoPage page)
    {
        return new ButtonInfoImage(gui, this, imageProvider, imageWidth, imageHeight, imageScale);
    }

    @Override
    public void fromJson(JsonElement e)
    {
        super.fromJson(e);

        imageWidth = imageHeight = 0;
        imageScale = 0D;

        JsonObject o = e.getAsJsonObject();

        setImage(o.has("image") ? new URLImageProvider(o.get("image").getAsString()) : EmptyImageProvider.INSTANCE);

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
    }

    @Override
    public JsonElement getSerializableElement()
    {
        JsonObject o = (JsonObject) super.getSerializableElement();

        if(imageURL != null && !imageURL.isEmpty())
        {
            o.add("image", new JsonPrimitive(imageURL));
        }

        return o;
    }
}