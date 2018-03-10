package com.feed_the_beast.ftblib.lib.icon;

import com.feed_the_beast.ftblib.lib.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.File;
import java.net.URI;

/**
 * @author LatvianModder
 */
public class URLImageIcon extends ImageIcon
{
	public final String url;

	public URLImageIcon(String _url, double u0, double v0, double u1, double v1)
	{
		super(new ResourceLocation(_url), u0, v0, u1, v1);
		url = _url;
	}

	public URLImageIcon(String _url)
	{
		this(_url, 0, 0, 1, 1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void bindTexture()
	{
		TextureManager manager = ClientUtils.MC.getTextureManager();
		ITextureObject img = manager.getTexture(texture);

		if (img == null)
		{
			if (url.startsWith("http:") || url.startsWith("https:"))
			{
				img = new ThreadDownloadImageData(null, url, MISSING_IMAGE, null);
			}
			else
			{
				File file = null;

				if (url.startsWith("file:"))
				{
					try
					{
						file = new File(new URI(url).getPath());
					}
					catch (Exception ex)
					{
						ex.printStackTrace();
					}
				}

				if (file == null)
				{
					file = new File(url);
				}

				img = new ThreadDownloadImageData(file, url, MISSING_IMAGE, null);
			}

			manager.loadTexture(texture, img);
		}

		GlStateManager.bindTexture(img.getGlTextureId());
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