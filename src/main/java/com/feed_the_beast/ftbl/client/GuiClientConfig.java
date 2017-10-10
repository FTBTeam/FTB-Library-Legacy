package com.feed_the_beast.ftbl.client;

import com.feed_the_beast.ftbl.api.FTBLibAPI;
import com.feed_the_beast.ftbl.lib.client.ClientUtils;
import com.feed_the_beast.ftbl.lib.gui.Button;
import com.feed_the_beast.ftbl.lib.gui.GuiBase;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.feed_the_beast.ftbl.lib.gui.GuiIcons;
import com.feed_the_beast.ftbl.lib.gui.Panel;
import com.feed_the_beast.ftbl.lib.gui.misc.GuiButtonListBase;
import com.feed_the_beast.ftbl.lib.gui.misc.GuiLoading;
import com.feed_the_beast.ftbl.lib.icon.Icon;
import com.feed_the_beast.ftbl.lib.internal.FTBLibFinals;
import com.feed_the_beast.ftbl.lib.util.StringUtils;
import com.feed_the_beast.ftbl.lib.util.misc.MouseButton;
import net.minecraft.client.gui.GuiButton;
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
public class GuiClientConfig extends GuiButtonListBase
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
							mc.displayGuiScreen(new GuiMessageDialog(parentScreen, "fml.configgui.gameRestartTitle", new TextComponentString(StringUtils.translate("fml.configgui.gameRestartRequired")), "fml.configgui.confirmRestartMessage"));
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
		public ButtonConfigBase(GuiBase gui, String title, Icon icon)
		{
			super(gui, 0, 0, gui.getStringWidth(title) + 28, 20, title);
			setIcon(icon);
		}

		@Override
		public void addMouseOverText(List<String> list)
		{
		}

		@Override
		public void renderWidget()
		{
			int ax = getAX();
			int ay = getAY();

			gui.getTheme().getButton(gui.isMouseOver(this)).draw(ax, ay, width, height);
			Icon icon = getIcon();

			if (icon.isEmpty())
			{
				gui.drawString(getTitle(), ax + 2, ay + 6, SHADOW);
			}
			else
			{
				icon.draw(ax + 2, ay + 2, 16, 16);
				gui.drawString(getTitle(), ax + 21, ay + 6, SHADOW);
			}
		}
	}

	private class ButtonClientConfig extends ButtonConfigBase
	{
		private final String modId;

		public ButtonClientConfig(GuiBase gui, ClientConfig config)
		{
			super(gui, config.name.getFormattedText(), config.icon);
			modId = config.id;
		}

		@Override
		public void onClicked(MouseButton button)
		{
			GuiHelper.playClickSound();
			ClientUtils.MC.displayGuiScreen(new GuiCustomConfig(modId, getTitle()));
		}
	}

	@Override
	public void addButtons(Panel panel)
	{
		List<Button> buttons = new ArrayList<>();

		for (ClientConfig config : FTBLibModClient.CLIENT_CONFIG_MAP.values())
		{
			buttons.add(new ButtonClientConfig(gui, config));
		}

		buttons.sort((o1, o2) -> o1.getTitle().compareToIgnoreCase(o2.getTitle()));

		if (FTBLibAPI.API.getClientData().optionalServerMods().contains(FTBLibFinals.MOD_ID))
		{
			buttons.add(0, new ButtonConfigBase(gui, StringUtils.translate("player_config"), GuiIcons.SETTINGS_RED)
			{
				@Override
				public void onClicked(MouseButton button)
				{
					GuiHelper.playClickSound();
					new GuiLoading().openGui();
					ClientUtils.execClientCommand("/ftb my_settings");
				}
			});


			buttons.add(1, new ButtonConfigBase(gui, StringUtils.translate("team_config"), GuiIcons.FRIENDS)
			{
				@Override
				public void onClicked(MouseButton button)
				{
					GuiHelper.playClickSound();
					new GuiLoading().openGui();
					ClientUtils.execClientCommand("/ftb team config");
				}
			});
		}

		buttons.add(2, new ButtonConfigBase(gui, StringUtils.translate("sidebar_button"), Icon.getIcon(FTBLibFinals.MOD_ID + ":textures/gui/teams.png"))
		{
			@Override
			public void onClicked(MouseButton button)
			{
				GuiHelper.playClickSound();
				new GuiSidebarButtonConfig().openGui();
			}
		});

		panel.addAll(buttons);
	}

	@Override
	public void onClosed()
	{
		FTBLibModClient.saveSidebarButtonConfig();
	}
}