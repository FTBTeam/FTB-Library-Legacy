package com.feed_the_beast.ftblib.lib.icon;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author LatvianModder
 */
public class PartIcon extends ImageIcon
{
	public final ImageIcon parent;
	public final boolean repeat;
	public final int width, height;
	public final int corner;
	public final int middleH, middleV;

	private final Icon all, middleU, middleD, middleL, middleR, cornerNN, cornerPN, cornerNP, cornerPP, center;

	public PartIcon(ImageIcon icon, int x, int y, int w, int h, int c, int mh, int mv, boolean r)
	{
		super(icon.getJson().getAsString(), 0D, 0D, 1D, 1D);
		parent = icon;
		width = w;
		height = h;
		corner = c;
		middleH = mh;
		middleV = mv;
		repeat = r;
		int cmh = c + mh;
		int cmv = c + mv;

		all = parent.withUVfromCoords(x, y, c + mh + c, c + mv + c, w, h);
		middleU = parent.withUVfromCoords(x + c, y, mh, c, w, h);
		middleD = parent.withUVfromCoords(x + c, y + cmv, mh, c, w, h);
		middleL = parent.withUVfromCoords(x, y + c, c, mv, w, h);
		middleR = parent.withUVfromCoords(x + cmh, y + c, c, mv, w, h);
		cornerNN = parent.withUVfromCoords(x, y, c, c, w, h);
		cornerPN = parent.withUVfromCoords(x + cmh, y, c, c, w, h);
		cornerNP = parent.withUVfromCoords(x, y + cmv, c, c, w, h);
		cornerPP = parent.withUVfromCoords(x + cmh, y + cmv, c, c, w, h);
		center = parent.withUVfromCoords(x + c, y + c, mh, mv, w, h);
	}

	public PartIcon(ImageIcon icon, int s, int c, int m)
	{
		this(icon, 0, 0, s, s, c, m, m, true);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ITextureObject bindTexture()
	{
		return parent.bindTexture();
	}

	@Override
	public void draw(int x, int y, int w, int h, Color4I col)
	{
		bindTexture();

		if (w == width && h == height)
		{
			all.draw(x, y, w, h, col);
			return;
		}

		int c = corner;
		int mw = w - c * 2;
		int mh = h - c * 2;

		middleU.draw(x + c, y, mw, c, col);
		middleR.draw(x + w - c, y + c, c, mh, col);
		middleD.draw(x + c, y + h - c, mw, c, col);
		middleL.draw(x, y + c, c, mh, col);

		cornerNN.draw(x, y, c, c, col);
		cornerNP.draw(x, y + h - c, c, c, col);
		cornerPN.draw(x + w - c, y, c, c, col);
		cornerPP.draw(x + w - c, y + h - c, c, c, col);

		center.draw(x + c, y + c, mw, mh, col);
	}

	@Override
	public JsonElement getJson()
	{
		JsonObject json = new JsonObject();
		json.addProperty("id", "part");
		json.add("parent", parent.getJson());
		json.addProperty("width", width);
		json.addProperty("height", height);
		json.addProperty("corner", corner);
		json.addProperty("middle_h", middleH);
		json.addProperty("middle_v", middleV);
		return json;
	}
}