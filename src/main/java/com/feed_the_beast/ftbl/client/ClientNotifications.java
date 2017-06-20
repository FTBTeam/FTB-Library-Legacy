package com.feed_the_beast.ftbl.client;

import com.feed_the_beast.ftbl.api.INotification;
import com.feed_the_beast.ftbl.lib.Color4I;
import com.feed_the_beast.ftbl.lib.client.ImageProvider;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

public class ClientNotifications
{
	private static final Minecraft MC = Minecraft.getMinecraft();
	private static Temp current;

	public static class NotificationWidget
	{
		public final INotification notification;
		public final List<String> text;
		public final int height;
		public int width;
		public final FontRenderer font;

		public NotificationWidget(INotification n, FontRenderer f)
		{
			notification = n;
			width = 0;
			font = f;

			if (notification.getText().isEmpty())
			{
				text = Collections.emptyList();
			}
			else
			{
				text = new ArrayList<>();

				for (ITextComponent c : notification.getText())
				{
					for (String s : font.listFormattedStringToWidth(c.getFormattedText(), 250))
					{
						text.add(s);
						width = Math.max(width, font.getStringWidth(s));
					}
				}
			}

			width += 20;

			if (notification.getIcon() != ImageProvider.NULL)
			{
				if (text.isEmpty())
				{
					width = 32;
				}
				else
				{
					width += 20;
				}
			}

			if (text.size() > 2)
			{
				height = 4 + text.size() * 12;
			}
			else
			{
				height = 32;
			}
		}

		public void render(int ax, int ay)
		{
			GlStateManager.enableBlend();

			GlStateManager.color(1F, 1F, 1F, 1F);
			GuiHelper.drawBlankRect(ax, ay, width, height, notification.getColor());

			if (notification.getIcon() != ImageProvider.NULL)
			{
				notification.getIcon().draw(ax + 8, ay + (height - 16) / 2, 16, 16, Color4I.NONE);
			}

			for (int i = 0; i < text.size(); i++)
			{
				font.drawString(text.get(i), ax + (notification.getIcon() != ImageProvider.NULL ? 30 : 10), ay + i * 11 + (height - text.size() * 10) / 2, 0xFFFFFFFF);
			}
		}
	}

	private static class Temp
	{
		private static final LinkedHashMap<ResourceLocation, INotification> MAP = new LinkedHashMap<>();

		private long time;
		private NotificationWidget widget;

		private Temp(INotification n)
		{
			widget = new NotificationWidget(n, MC.fontRenderer);
			time = -1L;
		}

		public boolean render(ScaledResolution screen)
		{
			if (time == -1L)
			{
				time = System.currentTimeMillis();
			}
			else if (time <= 0L)
			{
				return true;
			}

			int timeExisted = (int) (System.currentTimeMillis() - time);
			int timer = widget.notification.getTimer() & 0xFFFF;

			if (timeExisted > timer)
			{
				time = 0L;
				return true;
			}

			double d1 = 1D;

			if (timer - timeExisted < 300)
			{
				d1 = (timer - timeExisted) / 300D;
			}

			if (timeExisted < 300)
			{
				d1 = timeExisted / 300D;
			}

			GlStateManager.pushMatrix();
			GlStateManager.disableDepth();
			GlStateManager.depthMask(false);
			GlStateManager.disableLighting();
			widget.render(screen.getScaledWidth() - widget.width - 4, (int) (d1 * widget.height) - widget.height);
			GlStateManager.depthMask(true);
			GlStateManager.color(1F, 1F, 1F, 1F);
			GlStateManager.enableLighting();
			GlStateManager.popMatrix();

			return false;
		}
	}

	static boolean shouldRenderTemp()
	{
		return current != null || !Temp.MAP.isEmpty();
	}

	static void renderTemp(ScaledResolution screen)
	{
		if (current != null)
		{
			if (current.render(screen))
			{
				current = null;
			}
		}
		else if (!Temp.MAP.isEmpty())
		{
			current = new Temp(Temp.MAP.values().iterator().next());
			Temp.MAP.remove(current.widget.notification.getId().getID());
		}
	}

	public static void add(INotification n)
	{
		ResourceLocation id = n.getId().getID();
		Temp.MAP.remove(id);

		if (current != null && current.widget.notification.getId().equals(n.getId()))
		{
			current = null;
		}

		Temp.MAP.put(id, n);
	}

	public static void init()
	{
		current = null;
		Temp.MAP.clear();
	}
}