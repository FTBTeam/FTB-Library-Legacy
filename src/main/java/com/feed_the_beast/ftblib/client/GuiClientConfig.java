package com.feed_the_beast.ftblib.client;

import com.feed_the_beast.ftblib.FTBLib;
import com.feed_the_beast.ftblib.lib.client.ClientUtils;
import com.feed_the_beast.ftblib.lib.gui.Button;
import com.feed_the_beast.ftblib.lib.gui.GuiHelper;
import com.feed_the_beast.ftblib.lib.gui.GuiIcons;
import com.feed_the_beast.ftblib.lib.gui.Panel;
import com.feed_the_beast.ftblib.lib.gui.SimpleTextButton;
import com.feed_the_beast.ftblib.lib.gui.WidgetType;
import com.feed_the_beast.ftblib.lib.gui.misc.GuiButtonListBase;
import com.feed_the_beast.ftblib.lib.gui.misc.GuiLoading;
import com.feed_the_beast.ftblib.lib.icon.Icon;
import com.feed_the_beast.ftblib.lib.util.SidedUtils;
import com.feed_the_beast.ftblib.lib.util.misc.MouseButton;
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
					if ((configID != null || !(parentScreen instanceof GuiConfig)) && entryList.hasChangedEntry(true))
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

	private class ButtonClientConfig extends SimpleTextButton
	{
		private final String modId;

		public ButtonClientConfig(Panel panel, ClientConfig config)
		{
			super(panel, config.name.getFormattedText(), config.icon);
			modId = config.id;
		}

		@Override
		public void onClicked(MouseButton button)
		{
			GuiHelper.playClickSound();
			ClientUtils.MC.displayGuiScreen(new GuiCustomConfig(modId, getTitle()));
		}
	}

	public GuiClientConfig()
	{
		setTitle(I18n.format("sidebar_button.ftblib.settings"));
	}

	@Override
	public void addButtons(Panel panel)
	{
		panel.add(new SimpleTextButton(panel, I18n.format("player_config"), GuiIcons.SETTINGS_RED)
		{
			@Override
			public void onClicked(MouseButton button)
			{
				GuiHelper.playClickSound();
				new GuiLoading().openGui();
				ClientUtils.execClientCommand("/my_settings");
			}

			@Override
			public WidgetType getWidgetType()
			{
				return SidedUtils.isModLoadedOnServer(FTBLib.MOD_ID) ? super.getWidgetType() : WidgetType.DISABLED;
			}
		});

		panel.add(new SimpleTextButton(panel, I18n.format("sidebar_button"), Icon.getIcon("ftblib:textures/gui/teams.png"))
		{
			@Override
			public void onClicked(MouseButton button)
			{
				GuiHelper.playClickSound();
				new GuiSidebarButtonConfig().openGui();
			}
		});

		List<Button> buttons = new ArrayList<>();

		for (ClientConfig config : FTBLibClient.CLIENT_CONFIG_MAP.values())
		{
			buttons.add(new ButtonClientConfig(panel, config));
		}

		buttons.sort((o1, o2) -> o1.getTitle().compareToIgnoreCase(o2.getTitle()));
		panel.addAll(buttons);
	}

	@Override
	public void onClosed()
	{
		super.onClosed();
		FTBLibClient.saveSidebarButtonConfig();
	}
}