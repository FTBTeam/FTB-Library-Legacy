package com.feed_the_beast.ftbl.api.gui;

import com.feed_the_beast.ftbl.lib.Color4I;
import com.feed_the_beast.ftbl.lib.gui.Widget;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author LatvianModder
 */
public interface IDrawableObject
{
	default boolean isNull()
	{
		return false;
	}

	@SideOnly(Side.CLIENT)
	default ITextureObject bindTexture()
	{
		return null;
	}

	@SideOnly(Side.CLIENT)
	void draw(int x, int y, int w, int h, Color4I col);

	@SideOnly(Side.CLIENT)
	default void draw(Widget widget, Color4I col)
	{
		draw(widget.getAX(), widget.getAY(), widget.width, widget.height, col);
	}

	default JsonElement getJson()
	{
		return JsonNull.INSTANCE;
	}

	default IDrawableObject withUV(double u0, double v0, double u1, double v1)
	{
		return this;
	}

	default IDrawableObject withUVfromCoords(int x, int y, int w, int h, int tw, int th)
	{
		return withUV(x / (double) tw, y / (double) th, (x + w) / (double) tw, (y + h) / (double) th);
	}
}