package com.feed_the_beast.ftbl.client;

import com.feed_the_beast.ftbl.api.FTBLibAPI;
import com.feed_the_beast.ftbl.lib.Color4I;
import com.feed_the_beast.ftbl.lib.MouseButton;
import com.feed_the_beast.ftbl.lib.client.ClientUtils;
import com.feed_the_beast.ftbl.lib.gui.Button;
import com.feed_the_beast.ftbl.lib.gui.GuiBase;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.feed_the_beast.ftbl.lib.gui.GuiIcons;
import com.feed_the_beast.ftbl.lib.gui.WidgetLayout;
import com.feed_the_beast.ftbl.lib.gui.misc.GuiLoading;
import com.feed_the_beast.ftbl.lib.icon.Icon;
import com.feed_the_beast.ftbl.lib.internal.FTBLibFinals;
import com.feed_the_beast.ftbl.lib.util.StringUtils;
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

	private abstract class ButtonConfigBase extends Button
	{
		public ButtonConfigBase(String title, Icon icon)
		{
			super(2, 0, getFont().getStringWidth(title) + (icon.isEmpty() ? 6 : 24), 20);
			setTitle(title);
			setIcon(icon);
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
			Icon icon = getIcon(gui);

			if (icon.isEmpty())
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

	private class ButtonClientConfig extends ButtonConfigBase
	{
		private final String modId;

		public ButtonClientConfig(ClientConfig config)
		{
			super(config.name.getFormattedText(), config.icon);
			modId = config.id;
		}

		@Override
		public void onClicked(GuiBase gui, MouseButton button)
		{
			GuiHelper.playClickSound();
			ClientUtils.MC.displayGuiScreen(new GuiCustomConfig(modId, getTitle(gui)));
		}
	}

	private final List<Button> buttons;

	public GuiClientConfig()
	{
		super(0, 0);
		buttons = new ArrayList<>();

		for (ClientConfig config : FTBLibModClient.CLIENT_CONFIG_MAP.values())
		{
			buttons.add(new ButtonClientConfig(config));
		}

		buttons.sort((o1, o2) -> o1.getTitle(this).compareToIgnoreCase(o2.getTitle(this)));

		if (FTBLibAPI.API.getClientData().optionalServerMods().contains(FTBLibFinals.MOD_ID))
		{
			buttons.add(0, new ButtonConfigBase(StringUtils.translate("player_config"), GuiIcons.SETTINGS_RED)
			{
				@Override
				public void onClicked(GuiBase gui, MouseButton button)
				{
					GuiHelper.playClickSound();
					new GuiLoading().openGui();
					ClientUtils.execClientCommand("/ftb my_settings");
				}
			});


			buttons.add(1, new ButtonConfigBase(StringUtils.translate("team_config"), GuiIcons.FRIENDS)
			{
				@Override
				public void onClicked(GuiBase gui, MouseButton button)
				{
					GuiHelper.playClickSound();
					new GuiLoading().openGui();
					ClientUtils.execClientCommand("/ftb team config");
				}
			});
		}

		buttons.add(2, new ButtonConfigBase(StringUtils.translate("sidebar_button"), Icon.getIcon(FTBLibFinals.MOD_ID + ":textures/gui/teams.png"))
		{
			@Override
			public void onClicked(GuiBase gui, MouseButton button)
			{
				GuiHelper.playClickSound();
				new GuiSidebarButtonConfig().openGui();
			}
		});

		for (Button b : buttons)
		{
			setWidth(Math.max(width, b.width));
		}

		for (Button b : buttons)
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
	public Icon getIcon(GuiBase gui)
	{
		return Button.DEFAULT_BACKGROUND;
	}
}