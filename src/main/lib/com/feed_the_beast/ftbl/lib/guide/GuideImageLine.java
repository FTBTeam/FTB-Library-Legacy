package com.feed_the_beast.ftbl.lib.guide;

import com.feed_the_beast.ftbl.api.gui.IImageProvider;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.api.guide.IGuideTextLine;
import com.feed_the_beast.ftbl.lib.Color4I;
import com.feed_the_beast.ftbl.lib.client.ImageProvider;
import com.feed_the_beast.ftbl.lib.gui.Button;
import com.feed_the_beast.ftbl.lib.gui.GuiBase;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.feed_the_beast.ftbl.lib.gui.Panel;
import com.feed_the_beast.ftbl.lib.gui.Widget;
import com.feed_the_beast.ftbl.lib.util.JsonUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.event.ClickEvent;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author LatvianModder
 */
public class GuideImageLine extends EmptyGuidePageLine
{
    public IImageProvider imageProvider = ImageProvider.NULL;
    public int imageWidth, imageHeight;
    public double imageScale = 1D;
    public ClickEvent clickEvent;
    public List<String> hover;

    public GuideImageLine()
    {
    }

    public GuideImageLine(JsonElement e)
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
        else if(o.has("size"))
        {
            imageWidth = imageHeight = o.get("size").getAsInt();
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
            clickEvent = JsonUtils.deserializeClickEvent(o.get("click"));
        }

        if(o.has("hover"))
        {
            hover = new ArrayList<>();

            for(JsonElement e1 : o.get("hover").getAsJsonArray())
            {
                ITextComponent c = JsonUtils.deserializeTextComponent(e1);
                hover.add(c == null ? "" : c.getFormattedText());
            }
        }

        if(hover == null || hover.isEmpty())
        {
            hover = Collections.emptyList();
        }
    }

    @Override
    public Widget createWidget(GuiBase gui, Panel parent)
    {
        return new ButtonInfoImage(parent);
    }

    @Override
    public IGuideTextLine copy(GuidePage page)
    {
        GuideImageLine line = new GuideImageLine();
        line.imageProvider = imageProvider;
        line.imageWidth = imageWidth;
        line.imageHeight = imageHeight;
        line.imageScale = imageScale;
        line.clickEvent = clickEvent;
        line.hover = hover.isEmpty() ? Collections.emptyList() : new ArrayList<>(hover);
        return line;
    }

    @Override
    public JsonElement getJson()
    {
        JsonObject o = new JsonObject();
        o.add("id", new JsonPrimitive("img"));
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

        if(clickEvent != null)
        {
            o.add("click", JsonUtils.serializeClickEvent(clickEvent));
        }

        if(!hover.isEmpty())
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

    @Override
    public boolean isEmpty()
    {
        return false;
    }

    private class ButtonInfoImage extends Button
    {
        private final Panel parent;

        private ButtonInfoImage(Panel p)
        {
            super(0, 0, 0, 0);
            parent = p;
            checkSize();
        }

        private void checkSize()
        {
            imageProvider.bindTexture();

            if(width == 1 || height == 1)
            {
                width = Math.max(imageWidth == 0 ? GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH) : imageWidth, 2);
                height = Math.max(imageHeight == 0 ? GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT) : imageHeight, 2);
                double scale = imageScale < 0 ? (1D / -imageScale) : imageScale;
                double w = Math.min(parent.width, width * scale);
                double h = height * (w / (width * scale));

                setWidth((int) w);
                setHeight((int) h);
                parent.updateWidgetPositions();
            }
        }

        @Override
        public void renderWidget(GuiBase gui)
        {
            checkSize();
            imageProvider.draw(this, Color4I.NONE);
        }

        @Override
        public void addMouseOverText(GuiBase gui, List<String> list)
        {
            if(!hover.isEmpty())
            {
                list.addAll(hover);
            }
        }

        @Override
        public void onClicked(GuiBase gui, IMouseButton button)
        {
            if(GuiHelper.onClickEvent(clickEvent))
            {
                GuiHelper.playClickSound();
            }
        }
    }
}