package com.feed_the_beast.ftblib.lib.icon;

import com.feed_the_beast.ftblib.lib.gui.GuiHelper;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author LatvianModder
 */
public class IconWithBorder extends Icon
{
	public static final Icon BUTTON_GRAY = Color4I.rgb(0x212121).withBorder(Color4I.rgb(0x141414), false);
	public static final Icon BUTTON_RED = Color4I.rgb(0x1581B6).withBorder(Color4I.rgb(0xBF3726), false);
	public static final Icon BUTTON_GREEN = Color4I.rgb(0x98C600).withBorder(Color4I.rgb(0x438700), false);
	public static final Icon BUTTON_BLUE = Color4I.rgb(0x80C7F2).withBorder(Color4I.rgb(0x1581B6), false);

	public static final Icon BUTTON_ROUND_GRAY = Color4I.rgb(0x212121).withBorder(Color4I.rgb(0x141414), true);
	public static final Icon BUTTON_ROUND_RED = Color4I.rgb(0x1581B6).withBorder(Color4I.rgb(0xBF3726), true);
	public static final Icon BUTTON_ROUND_GREEN = Color4I.rgb(0x98C600).withBorder(Color4I.rgb(0x438700), true);
	public static final Icon BUTTON_ROUND_BLUE = Color4I.rgb(0x80C7F2).withBorder(Color4I.rgb(0x1581B6), true);

	public final Icon icon;
	public final Color4I color;
	public final boolean roundEdges;

	IconWithBorder(Icon i, Color4I c, boolean r)
	{
		icon = i;
		color = c;
		roundEdges = r;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void draw(int x, int y, int w, int h)
	{
		icon.draw(x + 1, y + 1, w - 2, h - 2);
		GuiHelper.drawHollowRect(x, y, w, h, color, roundEdges);
		GlStateManager.color(1F, 1F, 1F, 1F);
	}

	@Override
	public JsonElement getJson()
	{
		JsonObject o = new JsonObject();
		o.addProperty("id", "border");
		o.add("icon", icon.getJson());
		o.add("color", color.getJson());

		if (roundEdges)
		{
			o.addProperty("round_edges", true);
		}

		return o;
	}
}