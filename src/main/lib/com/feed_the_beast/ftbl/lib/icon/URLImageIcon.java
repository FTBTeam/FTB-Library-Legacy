package com.feed_the_beast.ftbl.lib.icon;

import com.feed_the_beast.ftbl.lib.client.ClientUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
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

	@Override
	public JsonElement getJson()
	{
		return new JsonPrimitive(url);
	}

	@Override
	public ImageIcon withUV(double u0, double v0, double u1, double v1)
	{
		return new URLImageIcon(url, u0, v0, u1, v1);
	}
}