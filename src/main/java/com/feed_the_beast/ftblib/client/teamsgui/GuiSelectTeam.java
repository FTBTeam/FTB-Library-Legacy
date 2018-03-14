package com.feed_the_beast.ftblib.client.teamsgui;

import com.feed_the_beast.ftblib.FTBLibLang;
import com.feed_the_beast.ftblib.lib.EnumTeamStatus;
import com.feed_the_beast.ftblib.lib.client.ClientUtils;
import com.feed_the_beast.ftblib.lib.gui.GuiHelper;
import com.feed_the_beast.ftblib.lib.gui.GuiIcons;
import com.feed_the_beast.ftblib.lib.gui.Panel;
import com.feed_the_beast.ftblib.lib.gui.SimpleTextButton;
import com.feed_the_beast.ftblib.lib.gui.WidgetType;
import com.feed_the_beast.ftblib.lib.gui.misc.GuiButtonListBase;
import com.feed_the_beast.ftblib.lib.util.StringUtils;
import com.feed_the_beast.ftblib.lib.util.misc.MouseButton;
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
			super(panel, t.displayName.getUnformattedText(), t.icon.withOutline(t.color.getColor(), false));
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
				ClientUtils.execClientCommand("/ftb team join " + team.getName());
				getGui().closeGui();
				ClientUtils.execClientCommand("/ftb team gui");
			}
			else if (team.type != PublicTeamData.Type.ENEMY && team.type != PublicTeamData.Type.REQUESTING_INVITE)
			{
				ClientUtils.execClientCommand("/ftb team request_invite " + team.getName());
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
				list.add(TextFormatting.GRAY + StringUtils.translate("ftblib.lang.team_status.requesting_invite"));
			}
			else if (team.type == PublicTeamData.Type.ENEMY)
			{
				list.add(TextFormatting.GRAY + EnumTeamStatus.ENEMY.getLangKey().translate());
			}
			else
			{
				list.add(TextFormatting.GRAY + (team.type == PublicTeamData.Type.CAN_JOIN ? FTBLibLang.TEAM_GUI_JOIN_TEAM : FTBLibLang.TEAM_GUI_REQUEST_INVITE).translate(team.color.getTextFormatting() + team.getName() + TextFormatting.GRAY));
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