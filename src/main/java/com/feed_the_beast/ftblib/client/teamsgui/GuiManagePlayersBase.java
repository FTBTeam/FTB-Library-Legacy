package com.feed_the_beast.ftblib.client.teamsgui;

import com.feed_the_beast.ftblib.lib.gui.Panel;
import com.feed_the_beast.ftblib.lib.gui.SimpleTextButton;
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
			setIcon(new PlayerHeadIcon(entry.uuid).withBorder(getPlayerColor(), false).withBorder(Color4I.DARK_GRAY, true).withPadding(2));
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

	public GuiManagePlayersBase(String title, Collection<MessageMyTeamPlayerList.Entry> m, BiFunction<Panel, MessageMyTeamPlayerList.Entry, ButtonPlayerBase> b)
	{
		setTitle(title);
		setHasSearchBox(true);
		entries = new ArrayList<>(m);
		buttonFunction = b;
	}

	@Override
	public void addButtons(Panel panel)
	{
		entries.sort(null);

		for (MessageMyTeamPlayerList.Entry m : entries)
		{
			panel.add(buttonFunction.apply(panel, m));
		}
	}
}