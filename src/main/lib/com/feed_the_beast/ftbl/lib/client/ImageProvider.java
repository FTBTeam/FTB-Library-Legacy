package com.feed_the_beast.ftbl.lib.client;

import com.feed_the_beast.ftbl.api.gui.IDrawableObject;
import com.feed_the_beast.ftbl.api.gui.IImageProvider;
import com.feed_the_beast.ftbl.lib.Color4I;
import com.feed_the_beast.ftbl.lib.gui.Widget;
import com.feed_the_beast.ftbl.lib.item.ItemStackSerializer;
import com.google.common.base.Objects;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by LatvianModder on 12.06.2016.
 */
public class ImageProvider implements IImageProvider
{
    public static final ImageProvider NULL = new ImageProvider(new ResourceLocation("textures/misc/unknown_pack.png"))
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

    public static IDrawableObject get(String id)
    {
        return id.isEmpty() ? NULL : get(new ResourceLocation(id));
    }

    public static IDrawableObject get(ResourceLocation id)
    {
        if(id.getResourceDomain().equals("item"))
        {
            ItemStack stack = ItemStackSerializer.parseItem(id.getResourcePath());

            if(stack != null)
            {
                return new DrawableItem(stack);
            }
        }
        else if(id.getResourcePath().isEmpty())
        {
            return NULL;
        }

        return new ImageProvider(id);
    }

    private final ResourceLocation texture;
    private final String url;

    public ImageProvider(ResourceLocation tex)
    {
        texture = tex;
        url = (texture.getResourceDomain().equals("http") || texture.getResourceDomain().equals("https")) ? texture.toString() : "";
    }

    @Override
    public boolean isURL()
    {
        return url.length() > 0;
    }

    @Override
    public final ResourceLocation getImage()
    {
        return texture;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ITextureObject bindTexture()
    {
        if(isURL())
        {
            ITextureObject obj = FTBLibClient.getDownloadImage(texture, url, ImageProvider.NULL.getImage(), null);
            GlStateManager.bindTexture(obj.getGlTextureId());
            return obj;
        }
        else
        {
            return FTBLibClient.bindTexture(texture);
        }
    }

    @Override
    public boolean equals(Object o)
    {
        if(o == this)
        {
            return true;
        }
        else if(o instanceof ITextureObject)
        {
            IImageProvider img = (IImageProvider) o;
            return texture.equals(img.getImage()) && getMinU() == img.getMinU() && getMinV() == img.getMinV() && getMaxU() == img.getMaxU() && getMaxV() == img.getMaxV();
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(texture, getMinU(), getMinV(), getMaxU(), getMaxV());
    }

    @Override
    public String toString()
    {
        return Double.toString(getMinU()) + ',' + getMinV() + ',' + getMaxU() + ',' + getMaxV();
    }
}