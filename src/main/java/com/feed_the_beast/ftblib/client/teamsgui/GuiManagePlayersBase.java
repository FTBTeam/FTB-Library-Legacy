package com.feed_the_beast.ftblib.client.teamsgui;

import com.feed_the_beast.ftblib.lib.gui.GuiBase;
import com.feed_the_beast.ftblib.lib.gui.Panel;
import com.feed_the_beast.ftblib.lib.gui.SimpleTextButton;
import com.feed_the_beast.ftblib.lib.gui.misc.GuiButtonListBase;
import com.feed_the_beast.ftblib.lib.icon.Color4I;
import com.feed_the_beast.ftblib.lib.icon.Icon;
import com.feed_the_beast.ftblib.lib.icon.PlayerHeadIcon;
import com.feed_the_beast.ftblib.lib.util.misc.MouseButton;
import com.feed_the_beast.ftblib.net.MessageMyTeamPlayerList;
import com.mojang.authlib.GameProfile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;

/**
 * @author LatvianModder
 */
public class GuiManagePlayersBase extends GuiButtonListBase
{
	static final Comparator<MessageMyTeamPlayerList.Entry> COMPARATOR = (o1, o2) ->
	{
		int o1s = o1.getSortIndex();
		int o2s = o2.getSortIndex();
		return o1s == o2s ? o1.name.compareToIgnoreCase(o2.name) : o2s - o1s;
	};

	static class ButtonPlayerBase extends SimpleTextButton
	{
		final MessageMyTeamPlayerList.Entry entry;

		ButtonPlayerBase(GuiBase gui, MessageMyTeamPlayerList.Entry m)
		{
			super(gui, "", Icon.EMPTY);
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
			setIcon(new PlayerHeadIcon(new GameProfile(entry.uuid, entry.name)).withOutline(getPlayerColor(), false).withOutline(Color4I.DARK_GRAY, true).withBorder(2));
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
	private final BiFunction<GuiBase, MessageMyTeamPlayerList.Entry, ButtonPlayerBase> buttonFunction;

	public GuiManagePlayersBase(String title, Collection<MessageMyTeamPlayerList.Entry> m, BiFunction<GuiBase, MessageMyTeamPlayerList.Entry, ButtonPlayerBase> b)
	{
		setTitle(title);
		entries = new ArrayList<>(m);
		buttonFunction = b;
	}

	@Override
	public void addButtons(Panel panel)
	{
		entries.sort(COMPARATOR);

		for (MessageMyTeamPlayerList.Entry m : entries)
		{
			panel.add(buttonFunction.apply(this, m));
		}
	}
}