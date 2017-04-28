package com.feed_the_beast.ftbl.api.gui;

import com.feed_the_beast.ftbl.lib.Color4I;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by LatvianModder on 15.01.2017.
 */
public interface IImageProvider extends IDrawableObject
{
    default boolean isURL()
    {
        return false;
    }

    ResourceLocation getImage();

    @SideOnly(Side.CLIENT)
    ITextureObject bindTexture();

    @Override
    @SideOnly(Side.CLIENT)
    default void draw(int x, int y, int w, int h, Color4I col)
    {
        bindTexture();
        GuiHelper.drawTexturedRect(x, y, w, h, col.hasColor() ? col : Color4I.WHITE, getMinU(), getMinV(), getMaxU(), getMaxV());
    }

    @Override
    default JsonElement getJson()
    {
        return new JsonPrimitive(getImage().toString());
    }

    default double getMinU()
    {
        return 0D;
    }

    default double getMinV()
    {
        return 0D;
    }

    default double getMaxU()
    {
        return 1D;
    }

    default double getMaxV()
    {
        return 1D;
    }
}