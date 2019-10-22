package com.feed_the_beast.ftblib.client.teamsgui;

import com.feed_the_beast.ftblib.lib.EnumTeamStatus;
import com.feed_the_beast.ftblib.lib.client.ClientUtils;
import com.feed_the_beast.ftblib.lib.gui.GuiHelper;
import com.feed_the_beast.ftblib.lib.gui.GuiIcons;
import com.feed_the_beast.ftblib.lib.gui.Panel;
import com.feed_the_beast.ftblib.lib.gui.SimpleTextButton;
import com.feed_the_beast.ftblib.lib.gui.WidgetType;
import com.feed_the_beast.ftblib.lib.gui.misc.GuiButtonListBase;
import com.feed_the_beast.ftblib.lib.util.misc.MouseButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;

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
			super(panel, I18n.format("team_action.ftblib.create_team"), GuiIcons.ADD);
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
			super(panel, t.displayName.getUnformattedText(), t.icon.withBorder(t.color.getColor(), false));
			team = t;

			if (team.type == PublicTeamData.Type.REQUESTING_INVITE)
			{
				setTitle(TextFormatting.AQUA + getTitle());
			}
			else if (team.type == PublicTeamData.Type.ENEMY)
			{
				setTitle(TextFormatting.RED + getTitle());
			}
			else if (team.type == PublicTeamData.Type.CAN_JOIN)
			{
				setTitle(TextFormatting.GREEN + getTitle());
			}
		}

		@Override
		public void onClicked(MouseButton button)
		{
			GuiHelper.playClickSound();

			if (team.type == PublicTeamData.Type.CAN_JOIN)
			{
				ClientUtils.execClientCommand("/team join " + team.getId());
				getGui().closeGui();
			}
			else if (team.type != PublicTeamData.Type.ENEMY && team.type != PublicTeamData.Type.REQUESTING_INVITE)
			{
				ClientUtils.execClientCommand("/team request_invite " + team.getId());
				team.type = PublicTeamData.Type.REQUESTING_INVITE;
				setTitle(TextFormatting.AQUA + getTitle());
				parent.alignWidgets();
			}
		}

		@Override
		public void addMouseOverText(List<String> list)
		{
			if (!team.description.isEmpty())
			{
				list.add(TextFormatting.ITALIC + team.description);
			}

			if (team.type == PublicTeamData.Type.REQUESTING_INVITE)
			{
				list.add(TextFormatting.GRAY + I18n.format("ftblib.lang.team_status.requesting_invite"));
			}
			else if (team.type == PublicTeamData.Type.ENEMY)
			{
				list.add(TextFormatting.GRAY + I18n.format(EnumTeamStatus.ENEMY.getLangKey()));
			}
			else
			{
				list.add(TextFormatting.GRAY + I18n.format(team.type == PublicTeamData.Type.CAN_JOIN ? "ftblib.lang.team.gui.join_team" : "ftblib.lang.team.gui.request_invite", team.color.getTextFormatting() + team.getId() + TextFormatting.GRAY));
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
		setTitle(I18n.format("team_action.ftblib.select_team"));
		setHasSearchBox(true);
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