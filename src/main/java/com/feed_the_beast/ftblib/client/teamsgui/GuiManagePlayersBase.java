package com.feed_the_beast.ftblib.client.teamsgui;

import com.feed_the_beast.ftblib.lib.gui.*;
import com.feed_the_beast.ftblib.lib.gui.misc.GuiButtonListBase;
import com.feed_the_beast.ftblib.lib.icon.Color4I;
import com.feed_the_beast.ftblib.lib.icon.Icon;
import com.feed_the_beast.ftblib.lib.icon.PlayerHeadIcon;
import com.feed_the_beast.ftblib.lib.util.misc.MouseButton;
import com.feed_the_beast.ftblib.net.MessageMyTeamPlayerList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;

/**
 * @author LatvianModder
 */
public class GuiManagePlayersBase extends GuiButtonListBase
{
	static class ButtonPlayerBase extends SimpleTextButton
	{
		final MessageMyTeamPlayerList.Entry entry;

		ButtonPlayerBase(Panel panel, MessageMyTeamPlayerList.Entry m)
		{
			super(panel, "", Icon.EMPTY);
			entry = m;
			updateIcon();
			setTitle(entry.name);
		}

		Color4I getPlayerColor()
		{
			return getDefaultPlayerColor();
		}

		Color4I getDefaultPlayerColor()
		{
			return Color4I.GRAY;
		}

		final void updateIcon()
		{
			//Variant 2: setIcon(gui.getTheme().getWidget(false).withColorAndBorder(getPlayerColor(), 1).combineWith(gui.getTheme().getSlot(false).withColorAndBorder(Color4I.DARK_GRAY, 3), new PlayerHeadIcon(entry.name).withBorder(4)));
			setIcon(new PlayerHeadIcon(entry.uuid).withOutline(getPlayerColor(), false).withOutline(Color4I.DARK_GRAY, true).withBorder(2));
		}

		@Override
		public void addMouseOverText(List<String> list)
		{
		}

		@Override
		public void onClicked(MouseButton button)
		{
		}
	}

	private final List<MessageMyTeamPlayerList.Entry> entries;
	private final BiFunction<Panel, MessageMyTeamPlayerList.Entry, ButtonPlayerBase> buttonFunction;
	private String usernameFilter = "";

	public GuiManagePlayersBase(String title, Collection<MessageMyTeamPlayerList.Entry> m, BiFunction<Panel, MessageMyTeamPlayerList.Entry, ButtonPlayerBase> b)
	{
		setTitle(title);
		entries = new ArrayList<>(m);
		buttonFunction = b;
	}

	public void setFilter (String username) {
		this.usernameFilter = username;
	}

	private List<MessageMyTeamPlayerList.Entry> applyFilter (Collection<MessageMyTeamPlayerList.Entry> list) {
		final ArrayList<MessageMyTeamPlayerList.Entry> toFilter = new ArrayList(list);
		toFilter.removeIf(e -> !e.name.startsWith(this.usernameFilter));
		return toFilter;
	}

	@Override
	public void addButtons(Panel panel)
	{
		final List<MessageMyTeamPlayerList.Entry> players = applyFilter(entries);
		players.sort(null);

		for (MessageMyTeamPlayerList.Entry m : players)
		{
			panel.add(buttonFunction.apply(panel, m));
		}
	}
}