package com.feed_the_beast.ftblib.lib.gui;

import com.feed_the_beast.ftblib.lib.icon.Icon;

/**
 * @author LatvianModder
 */
public class ContextMenuItem
{
	public static final ContextMenuItem SEPARATOR = new ContextMenuItem("", Icon.EMPTY, () -> {})
	{
		@Override
		public Widget createWidget(Panel panel)
		{
			return new ContextMenu.CSeperator(panel);
		}
	};

	public String title;
	public Icon icon;
	public Runnable callback;
	public boolean enabled = true;
	public String yesNoText = "";

	public ContextMenuItem(String t, Icon i, Runnable c)
	{
		title = t;
		icon = i;
		callback = c;
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

	public Widget createWidget(Panel panel)
	{
		return new ContextMenu.CButton(panel, this);
	}
}