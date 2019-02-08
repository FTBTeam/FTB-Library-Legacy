package com.feed_the_beast.ftblib.lib.icon;

import com.feed_the_beast.ftblib.FTBLib;
import com.feed_the_beast.ftblib.lib.gui.GuiHelper;
import com.google.common.base.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.image.BufferedImage;

/**
 * @author LatvianModder
 */
public class ImageIcon extends Icon
{
	public static final ResourceLocation MISSING_IMAGE = new ResourceLocation(FTBLib.MOD_ID, "textures/gui/missing_image.png");

	public final ResourceLocation texture;
	public final double minU, minV, maxU, maxV;

	public ImageIcon(ResourceLocation tex, double u0, double v0, double u1, double v1)
	{
		texture = tex;
		minU = Math.min(u0, u1);
		minV = Math.min(v0, v1);
		maxU = Math.max(u0, u1);
		maxV = Math.max(v0, v1);
	}

	public ImageIcon(ResourceLocation tex)
	{
		this(tex, 0D, 0D, 1D, 1D);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void bindTexture()
	{
		TextureManager manager = Minecraft.getMinecraft().getTextureManager();
		ITextureObject tex = manager.getTexture(texture);

		if (tex == null)
		{
			tex = new SimpleTexture(texture);
			manager.loadTexture(texture, tex);
		}

		GlStateManager.bindTexture(tex.getGlTextureId());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void draw(int x, int y, int w, int h, Color4I col)
	{
		bindTexture();
		GuiHelper.drawTexturedRect(x, y, w, h, col.whiteIfEmpty(), minU, minV, maxU, maxV);
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
		return texture.toString();
	}

	public ImageIcon withUV(double u0, double v0, double u1, double v1)
	{
		return new ImageIcon(texture, u0, v0, u1, v1);
	}

	public ImageIcon withUVfromCoords(int x, int y, int w, int h, int tw, int th)
	{
		return withUV(x / (double) tw, y / (double) th, (x + w) / (double) tw, (y + h) / (double) th);
	}

	@Override
	public boolean canBeCached()
	{
		return true;
	}

	@Override
	public BufferedImage readImage() throws Exception
	{
		return TextureUtil.readBufferedImage(Minecraft.getMinecraft().getResourceManager().getResource(texture).getInputStream());
	}
}