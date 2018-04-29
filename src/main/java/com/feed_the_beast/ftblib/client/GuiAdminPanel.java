package com.feed_the_beast.ftblib.client;

import com.feed_the_beast.ftblib.lib.data.Action;
import com.feed_the_beast.ftblib.lib.gui.GuiHelper;
import com.feed_the_beast.ftblib.lib.gui.Panel;
import com.feed_the_beast.ftblib.lib.gui.SimpleTextButton;
import com.feed_the_beast.ftblib.lib.gui.misc.GuiButtonListBase;
import com.feed_the_beast.ftblib.lib.util.misc.MouseButton;
import net.minecraft.client.resources.I18n;

import java.util.Collection;

/**
 * @author LatvianModder
 */
public class GuiAdminPanel extends GuiButtonListBase
{
	private Collection<Action.Inst> actions;

	public GuiAdminPanel(Collection<Action.Inst> a)
	{
		setTitle(I18n.format("sidebar_button.ftblib.admin_panel"));
		actions = a;
	}

	@Override
	public void addButtons(Panel panel)
	{
		for (Action.Inst a : actions)
		{
			panel.add(new SimpleTextButton(panel, a.title.getFormattedText(), a.icon)
			{
				@Override
				public void onClicked(MouseButton button)
				{
					GuiHelper.playClickSound();
				}
			});
		}
	}

	@Override
	public void onClosed()
	{
		FTBLibClient.saveSidebarButtonConfig();
	}
}