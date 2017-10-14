package com.feed_the_beast.ftbl.client.teamsgui;

import com.feed_the_beast.ftbl.lib.client.ClientUtils;
import com.feed_the_beast.ftbl.lib.gui.GuiBase;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.feed_the_beast.ftbl.lib.gui.Panel;
import com.feed_the_beast.ftbl.lib.gui.SimpleTextButton;
import com.feed_the_beast.ftbl.lib.gui.misc.GuiButtonListBase;
import com.feed_the_beast.ftbl.lib.util.misc.MouseButton;
import com.feed_the_beast.ftbl.net.MessageMyTeamAction;
import com.feed_the_beast.ftbl.net.MessageMyTeamGui;
import net.minecraft.client.gui.GuiYesNo;

import java.util.Collection;

/**
 * @author LatvianModder
 */
public class GuiMyTeam extends GuiButtonListBase
{
	private static class TeamActionButton extends SimpleTextButton
	{
		private final MessageMyTeamGui.ActionContainer action;

		private TeamActionButton(GuiBase gui, MessageMyTeamGui.ActionContainer a)
		{
			super(gui, 0, 0, a.title.getFormattedText(), a.icon);
			action = a;
		}

		@Override
		public void onClicked(MouseButton button)
		{
			GuiHelper.playClickSound();

			if (action.requiresConfirm)
			{
				ClientUtils.MC.displayGuiScreen(new GuiYesNo((result, id) ->
				{
					if (result)
					{
						new MessageMyTeamAction(action.id).sendToServer();
					}

					gui.openGui();
				}, action.title.getFormattedText() + "?", "", 0)); //LANG
			}
			else
			{
				new MessageMyTeamAction(action.id).sendToServer();
			}
		}

		@Override
		public boolean renderTitleInCenter()
		{
			return false;
		}
	}

	private Collection<MessageMyTeamGui.ActionContainer> actions;

	public GuiMyTeam(String t, Collection<MessageMyTeamGui.ActionContainer> l)
	{
		setTitle(t);
		actions = l;
	}

	@Override
	public void addButtons(Panel panel)
	{
		for (MessageMyTeamGui.ActionContainer action : actions)
		{
			panel.add(new TeamActionButton(this, action));
		}
	}
}