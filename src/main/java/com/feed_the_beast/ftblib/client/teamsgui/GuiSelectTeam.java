package com.feed_the_beast.ftblib.client.teamsgui;

import com.feed_the_beast.ftblib.lib.client.ClientUtils;
import com.feed_the_beast.ftblib.lib.gui.Button;
import com.feed_the_beast.ftblib.lib.gui.GuiBase;
import com.feed_the_beast.ftblib.lib.gui.GuiHelper;
import com.feed_the_beast.ftblib.lib.gui.GuiIcons;
import com.feed_the_beast.ftblib.lib.gui.Panel;
import com.feed_the_beast.ftblib.lib.gui.PanelScrollBar;
import com.feed_the_beast.ftblib.lib.icon.Color4I;
import com.feed_the_beast.ftblib.lib.icon.Icon;
import com.feed_the_beast.ftblib.lib.icon.PlayerHeadIcon;
import com.feed_the_beast.ftblib.lib.util.misc.MouseButton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author LatvianModder
 */
public class GuiSelectTeam extends GuiBase
{
	private final Panel panelTeams;
	private final PanelScrollBar scrollTeams;

	private static class ButtonCreateTeam extends Button
	{
		private ButtonCreateTeam(GuiBase gui)
		{
			super(gui, 0, 0, 32, 32);
			setTitle("Create a New Team");
			setIcon(GuiIcons.ADD);
		}

		@Override
		public void onClicked(MouseButton button)
		{
			GuiHelper.playClickSound();
			new GuiCreateTeam().openGui();
		}

		@Override
		public void draw()
		{
			int ax = getAX();
			int ay = getAY();
			getButtonBackground().draw(ax, ay, 32, 32);
			getIcon().draw(ax + 8, ay + 8, 16, 16);
		}
	}

	private static class ButtonTeam extends Button
	{
		private final PublicTeamData team;
		private final Icon background;
		private static final Color4I INVITED_COLOR = Color4I.rgba(0x6620A32B);

		private ButtonTeam(GuiBase gui, PublicTeamData t)
		{
			super(gui, 0, 0, 32, 32);
			team = t;
			setTitle(team.color.getTextFormatting() + team.displayName);
			setIcon(new PlayerHeadIcon(t.ownerName));
			background = team.isInvited ? INVITED_COLOR : Icon.EMPTY.withOutline(team.color.getColor(), true);
		}

		@Override
		public void onClicked(MouseButton button)
		{
			GuiHelper.playClickSound();

			if (team.isInvited)
			{
				ClientUtils.execClientCommand("/ftb team join " + team.getName());
			}
			else
			{
				ClientUtils.execClientCommand("/ftb team request_invite " + team.getName());
			}

			gui.closeGui();
		}

		@Override
		public void addMouseOverText(List<String> list)
		{
			list.add(getTitle());
			list.add("ID: " + team.getName());

			if (!team.description.isEmpty())
			{
				list.add("");
				list.add(team.description);
			}

			list.add("");
			list.add("Click to " + (team.isInvited ? "join the team" : "request invite to this team")); //LANG
		}

		@Override
		public void draw()
		{
			int ax = getAX();
			int ay = getAY();
			getButtonBackground().draw(ax, ay, width, height);
			background.draw(ax + 6, ay + 6, 20, 20);
			getIcon().draw(ax + 8, ay + 8, 16, 16);
		}
	}

	public GuiSelectTeam(Collection<PublicTeamData> teams0)
	{
		super(192, 170);
		List<PublicTeamData> teams = new ArrayList<>(teams0);
		teams.sort(null);

		panelTeams = new Panel(this, 0, 1, 168, 168)
		{
			@Override
			public void addWidgets()
			{
				Button b = new ButtonCreateTeam(gui);
				b.setX(8);
				b.setY(8);
				add(b);

				int x = 1;
				int y = 8;

				for (PublicTeamData t : teams)
				{
					b = new ButtonTeam(gui, t);
					b.setX(8 + x * 40);
					b.setY(y);
					add(b);

					x++;

					if (x == 4)
					{
						x = 0;
						y += 40;
					}
				}

				scrollTeams.setElementSize(y);
			}
		};

		panelTeams.addFlags(Panel.DEFAULTS);

		scrollTeams = new PanelScrollBar(this, 168, 8, 16, 152, 0, panelTeams)
		{
			@Override
			public boolean shouldDraw()
			{
				return true;
			}
		};
	}

	@Override
	public void addWidgets()
	{
		add(panelTeams);
		add(scrollTeams);
	}
}