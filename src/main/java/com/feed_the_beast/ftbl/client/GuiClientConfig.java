package com.feed_the_beast.ftbl.client;

import com.feed_the_beast.ftbl.api.events.registry.RegisterClientConfigEvent;
import com.feed_the_beast.ftbl.api.gui.IDrawableObject;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.lib.Color4I;
import com.feed_the_beast.ftbl.lib.client.ClientUtils;
import com.feed_the_beast.ftbl.lib.gui.Button;
import com.feed_the_beast.ftbl.lib.gui.GuiBase;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.feed_the_beast.ftbl.lib.gui.WidgetLayout;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.GuiMessageDialog;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LatvianModder
 */
public class GuiClientConfig extends GuiBase
{
	private class GuiCustomConfig extends GuiConfig
	{
		public GuiCustomConfig(String modid, String title)
		{
			super(ClientUtils.MC.currentScreen, modid, title);
		}

		@Override
		protected void actionPerformed(GuiButton button)
		{
			if (button.id == 2000)
			{
				boolean flag = true;
				try
				{
					if ((configID != null || parentScreen == null || !(parentScreen instanceof GuiConfig)) && (entryList.hasChangedEntry(true)))
					{
						boolean requiresMcRestart = entryList.saveConfigElements();

						ConfigChangedEvent event = new ConfigChangedEvent.OnConfigChangedEvent(modID, configID, isWorldRunning, requiresMcRestart);
						MinecraftForge.EVENT_BUS.post(event);
						if (!event.getResult().equals(Event.Result.DENY))
						{
							MinecraftForge.EVENT_BUS.post(new ConfigChangedEvent.PostConfigChangedEvent(modID, configID, isWorldRunning, requiresMcRestart));
						}

						if (requiresMcRestart)
						{
							flag = false;
							mc.displayGuiScreen(new GuiMessageDialog(parentScreen, "fml.configgui.gameRestartTitle", new TextComponentString(I18n.format("fml.configgui.gameRestartRequired")), "fml.configgui.confirmRestartMessage"));
						}

						if (parentScreen instanceof GuiConfig)
						{
							((GuiConfig) parentScreen).needsRefresh = true;
						}
					}
				}
				catch (Throwable e)
				{
					FMLLog.log.error("Error performing GuiConfig action:", e);
				}

				if (flag)
				{
					mc.displayGuiScreen(parentScreen);
				}
			}
			else
			{
				super.actionPerformed(button);
			}
		}
	}

	private class ButtonClientConfig extends Button
	{
		private final String modId;

		public ButtonClientConfig(String id, String title, IDrawableObject icon)
		{
			super(2, 0, getFont().getStringWidth(title) + (icon.isNull() ? 6 : 24), 20);
			setTitle(title);
			setIcon(icon);
			modId = id;
		}

		@Override
		public void onClicked(GuiBase gui, IMouseButton button)
		{
			GuiHelper.playClickSound();
			ClientUtils.MC.displayGuiScreen(new GuiCustomConfig(modId, getTitle(gui)));
		}

		@Override
		public void addMouseOverText(GuiBase gui, List<String> list)
		{
		}

		@Override
		public void renderWidget(GuiBase gui)
		{
			int ax = getAX();
			int ay = getAY();

			DEFAULT_BACKGROUND.draw(ax, ay, width, height, Color4I.NONE);
			IDrawableObject icon = getIcon(gui);

			if (icon.isNull())
			{
				gui.drawString(getTitle(gui), ax + 2, ay + 6);
			}
			else
			{
				icon.draw(ax + 2, ay + 2, 16, 16, Color4I.NONE);
				gui.drawString(getTitle(gui), ax + 21, ay + 6);
			}

			if (gui.isMouseOver(ax, ay, width, height))
			{
				DEFAULT_MOUSE_OVER.draw(ax, ay, width, height, Color4I.NONE);
			}
		}
	}

	private final List<ButtonClientConfig> buttons;

	public GuiClientConfig()
	{
		super(0, 0);
		buttons = new ArrayList<>();

		new RegisterClientConfigEvent((id, title, icon) ->
		{
			buttons.add(new ButtonClientConfig(id, title, icon));
			return null;
		}).post();

		buttons.sort((o1, o2) -> o1.getTitle(this).compareToIgnoreCase(o2.getTitle(this)));

		for (ButtonClientConfig b : buttons)
		{
			setWidth(Math.max(width, b.width));
		}

		for (ButtonClientConfig b : buttons)
		{
			b.setWidth(width);
		}

		setWidth(width + 4);
		setHeight(3 + buttons.size() * 21);
	}

	@Override
	public void addWidgets()
	{
		addAll(buttons);
		align(new WidgetLayout.Vertical(2, 1, 2));
	}

	@Override
	public IDrawableObject getIcon(GuiBase gui)
	{
		return Button.DEFAULT_BACKGROUND;
	}
}