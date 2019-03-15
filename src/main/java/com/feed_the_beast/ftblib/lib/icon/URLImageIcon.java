package com.feed_the_beast.ftblib.lib.icon;

import com.feed_the_beast.ftblib.lib.io.DataReader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URI;

/**
 * @author LatvianModder
 */
public class URLImageIcon extends ImageIcon
{
	public final URI uri;
	private final String url;

	public URLImageIcon(ResourceLocation tex, URI _uri, double u0, double v0, double u1, double v1)
	{
		super(tex, u0, v0, u1, v1);
		uri = _uri;
		url = uri.toString();
	}

	public URLImageIcon(ResourceLocation tex, URI uri)
	{
		this(tex, uri, 0, 0, 1, 1);
	}

	public URLImageIcon(URI uri)
	{
		this(new ResourceLocation(uri.toString()), uri);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void bindTexture()
	{
		TextureManager manager = Minecraft.getMinecraft().getTextureManager();
		ITextureObject img = manager.getTexture(texture);

		if (img == null)
		{
			if (uri.getScheme().equals("http") || uri.getScheme().equals("https"))
			{
				img = new ThreadDownloadImageData(null, url, MISSING_IMAGE, null);
			}
			else
			{
				File file = null;

				if (uri.getScheme().equals("file"))
				{
					try
					{
						file = new File(uri.getPath());
					}
					catch (Exception ex)
					{
						ex.printStackTrace();
					}
				}

				if (file == null)
				{
					file = new File(uri);
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
		return new URLImageIcon(texture, uri, u0, v0, u1, v1);
	}

	@Override
	public URLImageIcon withUVfromCoords(int x, int y, int w, int h, int tw, int th)
	{
		return withUV(x / (double) tw, y / (double) th, (x + w) / (double) tw, (y + h) / (double) th);
	}

	@Override
	public BufferedImage readImage() throws Exception
	{
		return DataReader.get(uri, Minecraft.getMinecraft().getProxy()).image();
	}
}