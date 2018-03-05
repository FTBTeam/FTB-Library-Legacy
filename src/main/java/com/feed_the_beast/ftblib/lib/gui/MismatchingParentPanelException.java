package com.feed_the_beast.ftblib.lib.gui;

/**
 * @author LatvianModder
 */
public class MismatchingParentPanelException extends IllegalArgumentException
{
	public final Panel panel;
	public final Widget widget;

	public MismatchingParentPanelException(Panel p, Widget w)
	{
		super("Widget's parent panel [" + w.parent + "] doesn't match the panel it was added to! [" + p + "]");
		panel = p;
		widget = w;
	}
}