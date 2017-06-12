package com.feed_the_beast.ftbl.api.gui;

public interface IClickable
{
	IClickable NO_ACTION = button ->
	{
	};

	void onClicked(IMouseButton button);
}