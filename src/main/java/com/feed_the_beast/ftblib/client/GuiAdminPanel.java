package com.feed_the_beast.ftblib.client;

import com.feed_the_beast.ftblib.FTBLib;
import com.feed_the_beast.ftblib.lib.client.ClientUtils;
import com.feed_the_beast.ftblib.lib.data.Action;
import com.feed_the_beast.ftblib.lib.gui.Button;
import com.feed_the_beast.ftblib.lib.gui.GuiHelper;
import com.feed_the_beast.ftblib.lib.gui.GuiIcons;
import com.feed_the_beast.ftblib.lib.gui.Panel;
import com.feed_the_beast.ftblib.lib.gui.SimpleTextButton;
import com.feed_the_beast.ftblib.lib.gui.misc.GuiButtonListBase;
import com.feed_the_beast.ftblib.lib.gui.misc.GuiLoading;
import com.feed_the_beast.ftblib.lib.icon.Icon;
import com.feed_the_beast.ftblib.lib.util.misc.MouseButton;
import net.minecraft.client.resources.I18n;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author LatvianModder
 */
public class GuiAdminPanel extends GuiButtonListBase
{
	private List<Action> actions;

	public GuiAdminPanel(Collection<Action> a)
	{
		setTitle(I18n.format("sidebar_button.ftblib.admin_panel"));
		actions = new ArrayList<>(a);
	}

	@Override
	public void addButtons(Panel panel)
	{
		List<Button> buttons = new ArrayList<>();

		for (ClientConfig config : FTBLibClient.CLIENT_CONFIG_MAP.values())
		{
			//buttons.add(new ButtonClientConfig(panel, config));
		}

		buttons.sort((o1, o2) -> o1.getTitle().compareToIgnoreCase(o2.getTitle()));

		buttons.add(0, new SimpleTextButton(panel, I18n.format("sidebar_button"), Icon.getIcon(FTBLib.MOD_ID + ":textures/gui/teams.png"))
		{
			@Override
			public void onClicked(MouseButton button)
			{
				GuiHelper.playClickSound();
				new GuiSidebarButtonConfig().openGui();
			}
		});

		if (FTBLibClient.isModLoadedOnServer(FTBLib.MOD_ID))
		{
			buttons.add(0, new SimpleTextButton(panel, I18n.format("player_config"), GuiIcons.SETTINGS_RED)
			{
				@Override
				public void onClicked(MouseButton button)
				{
					GuiHelper.playClickSound();
					new GuiLoading().openGui();
					ClientUtils.execClientCommand("/ftb my_settings");
				}
			});
		}

		panel.addAll(buttons);
	}

	@Override
	public void onClosed()
	{
		FTBLibClient.saveSidebarButtonConfig();
	}
}