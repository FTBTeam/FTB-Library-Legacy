package com.feed_the_beast.ftblib.lib.icon;

import com.feed_the_beast.ftblib.lib.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author LatvianModder
 */
public class URLImageIcon extends ImageIcon
{
	public final String url;

	URLImageIcon(String _url, double u0, double v0, double u1, double v1)
	{
		super(_url, u0, v0, u1, v1);
		url = _url;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ITextureObject bindTexture()
	{
		ITextureObject obj = ClientUtils.getDownloadImage(texture, url, MISSING_IMAGE, null);
		GlStateManager.bindTexture(obj.getGlTextureId());
		return obj;
	}

	public String toString()
	{
		return url;
	}

	@Override
	public URLImageIcon withUV(double u0, double v0, double u1, double v1)
	{
		return new URLImageIcon(url, u0, v0, u1, v1);
	}

	@Override
	public URLImageIcon withUVfromCoords(int x, int y, int w, int h, int tw, int th)
	{
		return withUV(x / (double) tw, y / (double) th, (x + w) / (double) tw, (y + h) / (double) th);
	}
}