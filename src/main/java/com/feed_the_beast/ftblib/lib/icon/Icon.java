package com.feed_the_beast.ftblib.lib.icon;

import com.feed_the_beast.ftblib.lib.util.JsonUtils;
import com.feed_the_beast.ftblib.lib.util.StringUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import javafx.scene.image.Image;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author LatvianModder
 */
public abstract class Icon
{
	public static final Color4I EMPTY = new Color4I(255, 255, 255, 255)
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
		public void draw3D(Color4I col)
		{
		}

		@Override
		public MutableColor4I mutable()
		{
			return new MutableColor4I.None();
		}

		@Override
		@Nullable
		public Image loadInstantJFXImage()
		{
			return null;
		}

		public int hashCode()
		{
			return 0;
		}

		public boolean equals(Object o)
		{
			return o == this;
		}
	};

	public static Icon getIcon(@Nullable JsonElement json)
	{
		if (JsonUtils.isNull(json))
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
					case "color":
					{
						Color4I color = Color4I.fromJson(o.get("color"));
						return (o.has("mutable") && o.get("mutable").getAsBoolean()) ? color.mutable() : color;
					}
					case "border":
						return getIcon(o.get("parent")).withBorder(o.has("border") ? o.get("border").getAsInt() : 0);
					case "tint":
						return getIcon(o.get("parent")).withTint(Color4I.fromJson(o.get("color")));
					case "animation":
					{
						List<Icon> icons = new ArrayList<>();

						for (JsonElement e : o.get("icons").getAsJsonArray())
						{
							icons.add(getIcon(e));
						}

						return IconAnimation.fromList(icons, true);
					}
					case "outline":
					{
						Icon icon = EMPTY;
						Color4I outline = EMPTY;
						boolean roundEdges = false;

						if (o.has("icon"))
						{
							icon = getIcon(o.get("icon"));
						}

						if (o.has("color"))
						{
							outline = Color4I.fromJson(o.get("color"));
						}

						if (o.has("round_edges"))
						{
							roundEdges = o.get("round_edges").getAsBoolean();
						}

						return icon.withOutline(outline, roundEdges);
					}
					case "bullet":
					{
						return new BulletIcon().setColor(o.has("color") ? Color4I.fromJson(o.get("color")) : EMPTY);
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

			return CombinedIcon.getCombined(list);
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
		else if (id.charAt(0) == '#')
		{
			return Color4I.fromJson(new JsonPrimitive(id));
		}
		String[] ida = id.split(":", 2);

		if (ida.length == 2)
		{
			switch (ida[0])
			{
				case "item":
					return ItemIcon.getItemIcon(ida[1]);
				case "http":
				case "https":
				case "file":
					try
					{
						return new URLImageIcon(new URI(id));
					}
					catch (Exception ex)
					{
					}
				case "player":
					return new PlayerHeadIcon(StringUtils.fromString(ida[1]));
			}
		}

		return (id.endsWith(".png") || id.endsWith(".jpg")) ? new ImageIcon(new ResourceLocation(id)) : new AtlasSpriteIcon(id);
	}

	public boolean isEmpty()
	{
		return false;
	}

	@SideOnly(Side.CLIENT)
	public void bindTexture()
	{
	}

	public Icon copy()
	{
		return this;
	}

	@SideOnly(Side.CLIENT)
	public abstract void draw(int x, int y, int w, int h, Color4I col);

	@SideOnly(Side.CLIENT)
	public final void draw(int x, int y, int w, int h)
	{
		draw(x, y, w, h, EMPTY);
	}

	@SideOnly(Side.CLIENT)
	public void drawStatic(int x, int y, int w, int h)
	{
		draw(x, y, w, h);
	}

	@SideOnly(Side.CLIENT)
	public void draw3D(Color4I col)
	{
		GlStateManager.pushMatrix();
		GlStateManager.scale(1D / 16D, 1D / 16D, 1D);
		draw(-8, -8, 16, 16, col);
		GlStateManager.popMatrix();
	}

	public JsonElement getJson()
	{
		return new JsonPrimitive(toString());
	}

	public final Icon combineWith(Icon icon)
	{
		if (icon.isEmpty())
		{
			return this;
		}
		else if (isEmpty())
		{
			return icon;
		}

		return new CombinedIcon(this, icon);
	}

	public final Icon combineWith(Icon... icons)
	{
		if (icons.length == 0)
		{
			return this;
		}
		else if (icons.length == 1)
		{
			return combineWith(icons[0]);
		}

		List<Icon> list = new ArrayList<>(icons.length + 1);
		list.add(this);
		list.addAll(Arrays.asList(icons));
		return CombinedIcon.getCombined(list);
	}

	public final Icon withOutline(Color4I color, boolean roundEdges)
	{
		if (color.isEmpty())
		{
			return withBorder(1);
		}

		return new IconWithOutline(this, color, roundEdges);
	}

	public final Icon withBorder(int border)
	{
		return border == 0 ? this : new IconWithBorder(this, border);
	}

	public Icon withTint(Color4I color)
	{
		return (isEmpty() || color == Color4I.WHITE) ? this : new IconWithTint(this, color);
	}

	public int hashCode()
	{
		return getJson().hashCode();
	}

	public boolean equals(Object o)
	{
		return o == this || o instanceof Icon && getJson().equals(((Icon) o).getJson());
	}

	/**
	 * @return false if this should be queued for rendering
	 */
	public boolean isLoadedJFXImageInstant()
	{
		return false;
	}

	/**
	 * @return null if failed to load
	 */
	@Nullable
	public Image loadInstantJFXImage()
	{
		return null;
	}

	@Nullable
	public Object getIngredient()
	{
		return null;
	}
}