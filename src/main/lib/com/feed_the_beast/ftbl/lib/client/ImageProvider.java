package com.feed_the_beast.ftbl.lib.client;

import com.feed_the_beast.ftbl.api.gui.IDrawableObject;
import com.feed_the_beast.ftbl.lib.Color4I;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.feed_the_beast.ftbl.lib.gui.Widget;
import com.feed_the_beast.ftbl.lib.item.ItemStackSerializer;
import com.feed_the_beast.ftbl.lib.util.ColorUtils;
import com.google.common.base.Objects;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import mcjty.lib.tools.ItemStackTools;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LatvianModder
 */
public class ImageProvider implements IDrawableObject
{
    public static final ImageProvider NULL = new ImageProvider("textures/misc/unknown_pack.png", 0D, 0D, 1D, 1D)
    {
        @Override
        @SideOnly(Side.CLIENT)
        public void draw(int x, int y, int w, int h, Color4I col)
        {
        }

        @Override
        @SideOnly(Side.CLIENT)
        public void draw(Widget widget, Color4I col)
        {
        }
    };

    public static IDrawableObject get(JsonElement json)
    {
        if(json.isJsonNull())
        {
            return NULL;
        }
        else if(json.isJsonObject())
        {
            JsonObject o = json.getAsJsonObject();

            if(o.has("id"))
            {
                switch(o.get("id").getAsString())
                {
                    case "colored":
                        Color4I col = new Color4I(true, ColorUtils.deserialize(o.get("color")));
                        return new ColoredObject(get(o.get("parent").getAsJsonObject()), o.has("color_alpha") ? new Color4I(true, col, o.get("color_alpha").getAsInt()) : col);
                }
            }
        }
        else if(json.isJsonArray())
        {
            List<IDrawableObject> list = new ArrayList<>();

            for(JsonElement e : json.getAsJsonArray())
            {
                list.add(get(e));
            }

            return list.isEmpty() ? NULL : new DrawableObjectList(list);
        }

        return get(json.getAsString());
    }

    public static IDrawableObject get(String id)
    {
        if(id.isEmpty())
        {
            return NULL;
        }
        else if(id.startsWith("item:"))
        {
            ItemStack stack = ItemStackSerializer.parseItem(id.substring(5));

            if(!ItemStackTools.isEmpty(stack))
            {
                return new DrawableItem(stack);
            }
        }
        else if(id.startsWith("http:") || id.startsWith("https:"))
        {
            return new URLImageProvider(id, 0D, 0D, 1D, 1D);
        }

        return new ImageProvider(id, 0D, 0D, 1D, 1D);
    }

    public final ResourceLocation texture;
    public final double minU, minV, maxU, maxV;

    ImageProvider(String tex, double u0, double v0, double u1, double v1)
    {
        texture = new ResourceLocation(tex);
        minU = Math.min(u0, u1);
        minV = Math.min(v0, v1);
        maxU = Math.max(u0, u1);
        maxV = Math.max(v0, v1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ITextureObject bindTexture()
    {
        return FTBLibClient.bindTexture(texture);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void draw(int x, int y, int w, int h, Color4I col)
    {
        bindTexture();
        GuiHelper.drawTexturedRect(x, y, w, h, col.hasColor() ? col : Color4I.WHITE, minU, minV, maxU, maxV);
    }

    @Override
    public JsonElement getJson()
    {
        return new JsonPrimitive(texture.toString());
    }

    @Override
    public boolean equals(Object o)
    {
        if(o == this)
        {
            return true;
        }
        else if(o instanceof ImageProvider)
        {
            ImageProvider img = (ImageProvider) o;
            return texture.equals(img.texture) && minU == img.minU && minV == img.minV && maxU == img.maxU && maxV == img.maxV;
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(texture, minU, minV, maxU, maxV);
    }

    @Override
    public String toString()
    {
        return Double.toString(minU) + ',' + minV + ',' + maxU + ',' + maxV;
    }

    @Override
    public IDrawableObject withUV(double u0, double v0, double u1, double v1)
    {
        return new ImageProvider(texture.toString(), u0, v0, u1, v1);
    }
}