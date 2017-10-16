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
import net.minecraft.nbt.NBTTagCompound;

import java.util.Collection;

/**
 * @author LatvianModder
 */
public class GuiMyTeam extends GuiButtonListBase
{
	private static class TeamActionButton extends SimpleTextButton
	{
		private final MessageMyTeamGui.Action action;

		private TeamActionButton(GuiBase gui, MessageMyTeamGui.Action a)
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
					gui.openGui();

					if (result)
					{
						new MessageMyTeamAction(action.id, new NBTTagCompound()).sendToServer();
					}
				}, action.title.getFormattedText() + "?", "", 0)); //LANG
			}
			else
			{
				new MessageMyTeamAction(action.id, new NBTTagCompound()).sendToServer();
			}
		}

		@Override
		public boolean renderTitleInCenter()
		{
			return false;
		}
	}

	private Collection<MessageMyTeamGui.Action> actions;

	public GuiMyTeam(String t, Collection<MessageMyTeamGui.Action> l)
	{
		setTitle(t);
		actions = l;
	}

	@Override
	public void addButtons(Panel panel)
	{
		for (MessageMyTeamGui.Action action : actions)
		{
			panel.add(new TeamActionButton(this, action));
		}
	}
}