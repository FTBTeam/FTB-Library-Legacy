package com.feed_the_beast.ftbl.lib.info;

import com.feed_the_beast.ftbl.api.gui.IGui;
import com.feed_the_beast.ftbl.api.gui.IImageProvider;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.api.gui.IPanel;
import com.feed_the_beast.ftbl.api.gui.IWidget;
import com.feed_the_beast.ftbl.api.info.IInfoTextLine;
import com.feed_the_beast.ftbl.lib.client.ImageProvider;
import com.feed_the_beast.ftbl.lib.gui.ButtonLM;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.feed_the_beast.ftbl.lib.util.LMJsonUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.event.ClickEvent;
import org.lwjgl.opengl.GL11;

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

    public InfoImageLine()
    {
    }

    public InfoImageLine(JsonElement e)
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

        if(o.has("click"))
        {
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
    public IWidget createWidget(IGui gui, IPanel parent)
    {
        return new ButtonInfoImage(parent);
    }

    @Override
    public IInfoTextLine copy(InfoPage page)
    {
        InfoImageLine line = new InfoImageLine();
        line.imageProvider = imageProvider;
        line.imageWidth = imageWidth;
        line.imageHeight = imageHeight;
        line.imageScale = imageScale;
        line.clickEvent = clickEvent;
        line.hover = new ArrayList<>(hover);
        return line;
    }

    @Override
    public JsonElement getJson()
    {
        JsonObject o = new JsonObject();
        o.add("id", new JsonPrimitive("img"));

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

    private class ButtonInfoImage extends ButtonLM
    {
        private final IPanel parent;

        private ButtonInfoImage(IPanel p)
        {
            super(0, 0, 0, 0);
            parent = p;
            checkSize();
        }

        private void checkSize()
        {
            imageProvider.bindTexture();

            int width = getWidth();
            int height = getHeight();

            if(width == 0 || height == 0)
            {
                width = imageWidth == 0 ? GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH) : imageWidth;
                height = imageHeight == 0 ? GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT) : imageHeight;

                double w = Math.min(parent.getWidth(), width * imageScale);
                double h = height * (w / (width * imageScale)) + 1D;

                setWidth((int) w);
                setHeight((int) h);
                parent.updateWidgetPositions();
            }
        }

        @Override
        public void renderWidget(IGui gui)
        {
            GlStateManager.color(1F, 1F, 1F, 1F);
            checkSize();
            GuiHelper.render(imageProvider, getAX(), getAY(), getWidth(), getHeight());
        }

        @Override
        public void addMouseOverText(IGui gui, List<String> l)
        {
            if(hover != null)
            {
                l.addAll(hover);
            }
        }

        @Override
        public void onClicked(IGui gui, IMouseButton button)
        {
            if(clickEvent != null)
            {
                GuiHelper.playClickSound();
                InfoPageHelper.onClickEvent(clickEvent);
            }
        }
    }
}