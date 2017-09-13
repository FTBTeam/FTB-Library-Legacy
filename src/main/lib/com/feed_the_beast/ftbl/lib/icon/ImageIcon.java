package com.feed_the_beast.ftbl.lib.icon;

import com.feed_the_beast.ftbl.lib.Color4I;
import com.feed_the_beast.ftbl.lib.client.ClientUtils;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.feed_the_beast.ftbl.lib.internal.FTBLibFinals;
import com.google.common.base.Objects;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author LatvianModder
 */
public class ImageIcon extends Icon
{
	public static final ResourceLocation MISSING_IMAGE = FTBLibFinals.get("textures/gui/missing_image.png");

	public final ResourceLocation texture;
	public final double minU, minV, maxU, maxV;

	ImageIcon(String tex, double u0, double v0, double u1, double v1)
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
		return ClientUtils.bindTexture(texture);
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
		if (o == this)
		{
			return true;
		}
		else if (o instanceof ImageIcon)
		{
			ImageIcon img = (ImageIcon) o;
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
	public Icon withUV(double u0, double v0, double u1, double v1)
	{
		return new ImageIcon(texture.toString(), u0, v0, u1, v1);
	}
}