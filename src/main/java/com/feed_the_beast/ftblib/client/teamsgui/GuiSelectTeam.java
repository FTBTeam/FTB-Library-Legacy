package com.feed_the_beast.ftblib.client.teamsgui;

import com.feed_the_beast.ftblib.FTBLibLang;
import com.feed_the_beast.ftblib.lib.client.ClientUtils;
import com.feed_the_beast.ftblib.lib.gui.GuiHelper;
import com.feed_the_beast.ftblib.lib.gui.GuiIcons;
import com.feed_the_beast.ftblib.lib.gui.Panel;
import com.feed_the_beast.ftblib.lib.gui.SimpleTextButton;
import com.feed_the_beast.ftblib.lib.gui.WidgetType;
import com.feed_the_beast.ftblib.lib.gui.misc.GuiButtonListBase;
import com.feed_the_beast.ftblib.lib.util.misc.MouseButton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author LatvianModder
 */
public class GuiSelectTeam extends GuiButtonListBase
{
	private static class ButtonCreateTeam extends SimpleTextButton
	{
		private final boolean canCreate;

		private ButtonCreateTeam(Panel panel, boolean c)
		{
			super(panel, FTBLibLang.TEAM_GUI_CREATE_TEAM.translate(), GuiIcons.ADD);
			canCreate = c;
		}

		@Override
		public void onClicked(MouseButton button)
		{
			GuiHelper.playClickSound();
			new GuiCreateTeam().openGui();
		}

		@Override
		public WidgetType getWidgetType()
		{
			return canCreate ? WidgetType.mouseOver(isMouseOver()) : WidgetType.DISABLED;
		}
	}

	private static class ButtonTeam extends SimpleTextButton
	{
		private final PublicTeamData team;

		private ButtonTeam(Panel panel, PublicTeamData t)
		{
			super(panel, t.displayName.getFormattedText(), t.icon.withOutline(t.color.getColor(), false));
			team = t;
		}

		@Override
		public void onClicked(MouseButton button)
		{
			GuiHelper.playClickSound();

			if (team.type == PublicTeamData.Type.CAN_JOIN)
			{
				ClientUtils.execClientCommand("/ftb team join " + team.getName());
			}
			else
			{
				ClientUtils.execClientCommand("/ftb team request_invite " + team.getName());
			}

			getGui().closeGui();
		}

		@Override
		public void addMouseOverText(List<String> list)
		{
			list.add("ID: " + team.getName());

			if (!team.description.isEmpty())
			{
				list.add("");
				list.add(team.description);
			}

			if (team.type != PublicTeamData.Type.ENEMY)
			{
				list.add("");
				list.add((team.type == PublicTeamData.Type.CAN_JOIN ? FTBLibLang.TEAM_GUI_JOIN_TEAM : FTBLibLang.TEAM_GUI_REQUEST_INVITE).translate());
			}
		}

		@Override
		public WidgetType getWidgetType()
		{
			return team.type == PublicTeamData.Type.ENEMY ? WidgetType.DISABLED : WidgetType.mouseOver(isMouseOver());
		}
	}

	private final boolean canCreate;
	private final List<PublicTeamData> teams;

	public GuiSelectTeam(Collection<PublicTeamData> teams0, boolean c)
	{
		setTitle(FTBLibLang.TEAM_GUI_SELECT_TEAM.translate());
		teams = new ArrayList<>(teams0);
		teams.sort(null);
		canCreate = c;
	}

	@Override
	public void addButtons(Panel panel)
	{
		panel.add(new ButtonCreateTeam(panel, canCreate));

		for (PublicTeamData t : teams)
		{
			panel.add(new ButtonTeam(panel, t));
		}
	}
}