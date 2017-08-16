package com.feed_the_beast.ftbl.client.teamsgui;

import com.feed_the_beast.ftbl.api.gui.IDrawableObject;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.lib.Color4I;
import com.feed_the_beast.ftbl.lib.client.ClientUtils;
import com.feed_the_beast.ftbl.lib.client.PlayerHeadImage;
import com.feed_the_beast.ftbl.lib.client.TexturelessRectangle;
import com.feed_the_beast.ftbl.lib.gui.Button;
import com.feed_the_beast.ftbl.lib.gui.GuiBase;
import com.feed_the_beast.ftbl.lib.gui.GuiIcons;
import com.feed_the_beast.ftbl.lib.gui.Panel;
import com.feed_the_beast.ftbl.lib.gui.PanelScrollBar;
import com.feed_the_beast.ftbl.lib.gui.Widget;

import java.util.Collections;
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
		private final IDrawableObject background;

		private ButtonCreateTeam()
		{
			super(0, 0, 32, 32);
			setTitle("Create a New Team");
			setIcon(GuiIcons.ADD);
			background = new TexturelessRectangle(Color4I.NONE).setLineColor(Color4I.WHITE).setRoundEdges(true);
		}

		@Override
		public void onClicked(GuiBase gui, IMouseButton button)
		{
			new GuiCreateTeam().openGui();
		}

		@Override
		public void renderWidget(GuiBase gui)
		{
			int ax = getAX();
			int ay = getAY();
			background.draw(ax, ay, 32, 32, Color4I.NONE);
			getIcon(gui).draw(ax + 8, ay + 8, 16, 16, Color4I.NONE);
		}
	}

	private static class ButtonTeam extends Button
	{
		private final PublicTeamData team;
		private final IDrawableObject background;
		private static final Color4I INVITED_COLOR = Color4I.rgba(0x6620A32B);

		private ButtonTeam(PublicTeamData t)
		{
			super(0, 0, 32, 32);
			team = t;
			setTitle(team.color.getTextFormatting() + team.displayName);
			setIcon(new PlayerHeadImage(t.ownerName));
			background = new TexturelessRectangle(team.isInvited ? INVITED_COLOR : Color4I.NONE).setLineColor(team.color.getColor()).setRoundEdges(true);
		}

		@Override
		public void onClicked(GuiBase gui, IMouseButton button)
		{
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
		public void renderWidget(GuiBase gui)
		{
			int ax = getAX();
			int ay = getAY();
			background.draw(ax, ay, 32, 32, Color4I.NONE);
			getIcon(gui).draw(ax + 8, ay + 8, 16, 16, Color4I.NONE);
		}

		@Override
		public void addMouseOverText(GuiBase gui, List<String> list)
		{
			list.add(getTitle(gui));
			list.add("ID: " + team.getName());

			if (!team.description.isEmpty())
			{
				list.add("");
				list.add(team.description);
			}

			list.add("");
			list.add("Click to " + (team.isInvited ? "join the team" : "request invite to this team"));
		}
	}

	public GuiSelectTeam(List<PublicTeamData> teams)
	{
		super(192, 170);
		Collections.sort(teams);

		panelTeams = new Panel(0, 1, 168, 168)
		{
			@Override
			public void addWidgets()
			{
				add(new ButtonCreateTeam());

				for (PublicTeamData t : teams)
				{
					add(new ButtonTeam(t));
				}
			}

			@Override
			public void updateWidgetPositions()
			{
				int size = 8;
				int x = 0;

				for (Widget widget : widgets)
				{
					widget.setX(8 + x * 40);
					widget.setY(size);

					x++;

					if (x == 4)
					{
						x = 0;
						size += 40;
					}
				}

				scrollTeams.setElementSize(size);
			}
		};

		panelTeams.addFlags(Panel.FLAG_DEFAULTS);

		scrollTeams = new PanelScrollBar(168, 8, 16, 152, 10, panelTeams)
		{
			@Override
			public boolean shouldRender(GuiBase gui)
			{
				return true;
			}
		};

		scrollTeams.background = Button.DEFAULT_BACKGROUND;
	}

	@Override
	public void addWidgets()
	{
		add(panelTeams);
		add(scrollTeams);
	}

	@Override
	public IDrawableObject getIcon(GuiBase gui)
	{
		return DEFAULT_BACKGROUND;
	}
}