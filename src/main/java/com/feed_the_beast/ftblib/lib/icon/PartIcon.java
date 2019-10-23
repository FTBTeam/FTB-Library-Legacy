package com.feed_the_beast.ftblib.lib.icon;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Map;

/**
 * @author LatvianModder
 */
public class PartIcon extends ImageIcon
{
	public final ImageIcon parent;
	public int posX, posY, width, height;
	public int corner, middleH, middleV;

	private Icon all, middleU, middleD, middleL, middleR, cornerNN, cornerPN, cornerNP, cornerPP, center;

	public PartIcon(ImageIcon icon, int x, int y, int w, int h, int c, int mh, int mv)
	{
		super(icon.texture);
		parent = icon;
		posX = x;
		posY = y;
		width = w;
		height = h;
		corner = c;
		middleH = mh;
		middleV = mv;
		updateParts();
	}

	public PartIcon(ImageIcon icon)
	{
		this(icon, 0, 0, 256, 256, 0, 256, 256);
	}

	public void updateParts()
	{
		int cmh = corner + middleH;
		int cmv = corner + middleV;
		all = parent.withUVfromCoords(posX, posY, corner + middleH + corner, corner + middleV + corner, width, height);
		middleU = parent.withUVfromCoords(posX + corner, posY, middleH, corner, width, height);
		middleD = parent.withUVfromCoords(posX + corner, posY + cmv, middleH, corner, width, height);
		middleL = parent.withUVfromCoords(posX, posY + corner, corner, middleV, width, height);
		middleR = parent.withUVfromCoords(posX + cmh, posY + corner, corner, middleV, width, height);
		cornerNN = parent.withUVfromCoords(posX, posY, corner, corner, width, height);
		cornerPN = parent.withUVfromCoords(posX + cmh, posY, corner, corner, width, height);
		cornerNP = parent.withUVfromCoords(posX, posY + cmv, corner, corner, width, height);
		cornerPP = parent.withUVfromCoords(posX + cmh, posY + cmv, corner, corner, width, height);
		center = parent.withUVfromCoords(posX + corner, posY + corner, middleH, middleV, width, height);
	}

	@Override
	public PartIcon copy()
	{
		PartIcon icon = new PartIcon(parent.copy());
		icon.minU = minU;
		icon.minV = minV;
		icon.maxU = maxU;
		icon.maxV = maxV;
		icon.posX = posX;
		icon.posY = posY;
		icon.width = width;
		icon.height = height;
		icon.corner = corner;
		icon.middleH = middleH;
		icon.middleV = middleV;
		return icon;
	}

	@Override
	protected void setProperties(Map<String, String> properties)
	{
		super.setProperties(properties);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void bindTexture()
	{
		parent.bindTexture();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void draw(int x, int y, int w, int h)
	{
		bindTexture();

		if (w == width && h == height)
		{
			all.draw(x, y, w, h);
			return;
		}

		int c = corner;
		int mw = w - c * 2;
		int mh = h - c * 2;

		middleU.draw(x + c, y, mw, c);
		middleR.draw(x + w - c, y + c, c, mh);
		middleD.draw(x + c, y + h - c, mw, c);
		middleL.draw(x, y + c, c, mh);

		cornerNN.draw(x, y, c, c);
		cornerNP.draw(x, y + h - c, c, c);
		cornerPN.draw(x + w - c, y, c, c);
		cornerPP.draw(x + w - c, y + h - c, c, c);

		center.draw(x + c, y + c, mw, mh);
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