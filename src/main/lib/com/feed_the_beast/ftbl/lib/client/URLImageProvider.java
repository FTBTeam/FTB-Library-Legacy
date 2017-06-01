package com.feed_the_beast.ftbl.lib.client;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author LatvianModder
 */
public class URLImageProvider extends ImageProvider
{
    public final String url;

    URLImageProvider(String _url, double u0, double v0, double u1, double v1)
    {
        super(_url, u0, v0, u1, v1);
        url = _url;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ITextureObject bindTexture()
    {
        ITextureObject obj = FTBLibClient.getDownloadImage(texture, url, ImageProvider.NULL.texture, null);
        GlStateManager.bindTexture(obj.getGlTextureId());
        return obj;
    }

    @Override
    public JsonElement getJson()
    {
        return new JsonPrimitive(url);
    }

    @Override
    public ImageProvider withUV(double u0, double v0, double u1, double v1)
    {
        return new URLImageProvider(url, u0, v0, u1, v1);
    }
}