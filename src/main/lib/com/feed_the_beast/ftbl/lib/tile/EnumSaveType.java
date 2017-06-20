package com.feed_the_beast.ftbl.lib.tile;

/**
 * @author LatvianModder
 */
public enum EnumSaveType
{
	SAVE(true),
	NET_FULL(false),
	NET_UPDATE(false);

	public final boolean save;

	EnumSaveType(boolean s)
	{
		save = s;
	}
}