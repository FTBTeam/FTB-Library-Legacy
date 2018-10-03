package com.feed_the_beast.ftblib.lib.gui;

import com.feed_the_beast.ftblib.lib.icon.Icon;
import com.feed_the_beast.ftblib.lib.util.StringUtils;
import com.feed_the_beast.ftblib.lib.util.misc.MouseButton;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author LatvianModder
 */
public class ContextMenuItem implements Comparable<ContextMenuItem>
{
	public static final ContextMenuItem SEPARATOR = new ContextMenuItem("", Icon.EMPTY, () -> {})
	{
		@Override
		public Widget createWidget(ContextMenu panel)
		{
			return new ContextMenu.CSeperator(panel);
		}
	};

	public String title;
	public Icon icon;
	public Runnable callback;
	public boolean enabled = true;
	public String yesNoText = "";
	public boolean closeMenu = true;

	public ContextMenuItem(String t, Icon i, @Nullable Runnable c)
	{
		title = t;
		icon = i;
		callback = c;
	}

	public void addMouseOverText(List<String> list)
	{
	}

	public ContextMenuItem setEnabled(boolean v)
	{
		enabled = v;
		return this;
	}

	public ContextMenuItem setYesNo(String s)
	{
		yesNoText = s;
		return this;
	}

	public ContextMenuItem setCloseMenu(boolean v)
	{
		closeMenu = v;
		return this;
	}

	public Widget createWidget(ContextMenu panel)
	{
		return new ContextMenu.CButton(panel, this);
	}

	@Override
	public int compareTo(ContextMenuItem o)
	{
		return StringUtils.unformatted(title).compareToIgnoreCase(StringUtils.unformatted(o.title));
	}

	public void onClicked(Panel panel, MouseButton button)
	{
		if (closeMenu)
		{
			panel.getGui().closeContextMenu();
		}

		callback.run();
	}
}