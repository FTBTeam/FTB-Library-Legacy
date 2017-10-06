package com.feed_the_beast.ftbl.lib.icon;

import com.feed_the_beast.ftbl.lib.client.ClientUtils;
import com.feed_the_beast.ftbl.lib.gui.Widget;
import com.feed_the_beast.ftbl.lib.util.misc.Color4I;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LatvianModder
 */
public abstract class Icon
{
	public static final Icon EMPTY = new Icon()
	{
		@Override
		public boolean isEmpty()
		{
			return true;
		}

		@Override
		@SideOnly(Side.CLIENT)
		public void draw(int x, int y, int w, int h, Color4I col)
		{
		}

		@Override
		@SideOnly(Side.CLIENT)
		public void draw(Widget widget, Color4I col)
		{
		}
	};

	public static Icon getIcon(JsonElement json)
	{
		if (json.isJsonNull())
		{
			return EMPTY;
		}
		else if (json.isJsonObject())
		{
			JsonObject o = json.getAsJsonObject();

			if (o.has("id"))
			{
				switch (o.get("id").getAsString())
				{
					case "loading":
						return LoadingIcon.INSTANCE;
					case "colored":
						return new ColoredIcon(getIcon(o.get("parent").getAsJsonObject()), Color4I.fromJson(o.get("color")));
					case "animation":
					{
						List<Icon> icons = new ArrayList<>();

						for (JsonElement e : o.get("icons").getAsJsonArray())
						{
							icons.add(getIcon(e));
						}

						IconAnimation list = new IconAnimation(icons);

						if (o.has("timer"))
						{
							list.timer = o.get("timer").getAsLong();
						}

						return list;
					}
					case "rect":
					{
						TexturelessRectangle icon = new TexturelessRectangle(o.has("color") ? Color4I.fromJson(o.get("color")) : Color4I.NONE);

						if (o.has("line_color"))
						{
							icon.setLineColor(Color4I.fromJson(o.get("line_color")));
						}

						if (o.has("round_edges"))
						{
							icon.setRoundEdges(o.get("round_edges").getAsBoolean());
						}

						return icon;
					}
					case "bullet":
					{
						return new BulletIcon().setColor(o.has("color") ? Color4I.fromJson(o.get("color")) : Color4I.NONE);
					}
				}
			}
		}
		else if (json.isJsonArray())
		{
			List<Icon> list = new ArrayList<>();

			for (JsonElement e : json.getAsJsonArray())
			{
				list.add(getIcon(e));
			}

			return list.isEmpty() ? EMPTY : new CombinedIcon(list);
		}

		String s = json.getAsString();

		if (s.isEmpty())
		{
			return EMPTY;
		}

		Icon icon = IconPresets.MAP.get(s);
		return icon == null ? getIcon(s) : icon;
	}

	public static Icon getIcon(String id)
	{
		if (id.isEmpty())
		{
			return EMPTY;
		}
		else if (id.equals("loading"))
		{
			return LoadingIcon.INSTANCE;
		}
		else if (id.startsWith("item:"))
		{
			return new ItemIcon(id.substring(5));
		}
		else if (id.startsWith("http:") || id.startsWith("https:"))
		{
			return new URLImageIcon(id, 0D, 0D, 1D, 1D);
		}

		if (!id.endsWith(".png"))
		{
			return new AtlasSpriteIcon(new ResourceLocation(id));
		}

		return new ImageIcon(id, 0D, 0D, 1D, 1D);
	}

	public boolean isEmpty()
	{
		return false;
	}

	@SideOnly(Side.CLIENT)
	public ITextureObject bindTexture()
	{
		return ClientUtils.bindTexture(ImageIcon.MISSING_IMAGE);
	}

	@SideOnly(Side.CLIENT)
	public abstract void draw(int x, int y, int w, int h, Color4I col);

	@SideOnly(Side.CLIENT)
	public void draw(Widget widget, Color4I col)
	{
		draw(widget.getAX(), widget.getAY(), widget.width, widget.height, col);
	}

	public JsonElement getJson()
	{
		return JsonNull.INSTANCE;
	}

	public Icon withUV(double u0, double v0, double u1, double v1)
	{
		return this;
	}

	public Icon withUVfromCoords(int x, int y, int w, int h, int tw, int th)
	{
		return withUV(x / (double) tw, y / (double) th, (x + w) / (double) tw, (y + h) / (double) th);
	}
}