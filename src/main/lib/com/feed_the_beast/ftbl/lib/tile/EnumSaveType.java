package com.feed_the_beast.ftbl.lib.tile;

/**
 * @author LatvianModder
 */
public enum EnumSaveType
{
	SAVE(true, true),
	NET_FULL(false, true),
	NET_UPDATE(false, false);

	public final boolean save;
	public final boolean full;

	EnumSaveType(boolean s, boolean f)
	{
		save = s;
		full = f;
	}
}