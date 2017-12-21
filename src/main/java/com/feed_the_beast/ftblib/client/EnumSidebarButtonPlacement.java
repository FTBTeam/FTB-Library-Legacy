package com.feed_the_beast.ftblib.client;

import com.feed_the_beast.ftblib.lib.util.CommonUtils;

/**
 * @author LatvianModder
 */
public enum EnumSidebarButtonPlacement
{
	DISABLED,
	TOP_LEFT,
	INVENTORY_SIDE,
	AUTO;

	public boolean top()
	{
		switch (this)
		{
			case TOP_LEFT:
				return true;
			case AUTO:
				return !CommonUtils.isNEILoaded();
			default:
				return false;
		}
	}
}