package com.feed_the_beast.ftbl.api;

public enum EnumReloadType
{
	/**
	 * On client side - logged in, on server side - world created
	 */
	CREATED(false),

	/**
	 * On client side - mode change packet received, on server side - /packmode set [mode]
	 */
	MODE_CHANGED(true),

	/**
	 * On client side - /reload_client, on server side - /reload
	 */
	RELOAD_COMMAND(true);

	/**
	 * @author LatvianModder
	 */
	public static final EnumReloadType[] VALUES = values();

	private final boolean command;

	EnumReloadType(boolean b)
	{
		command = b;
	}

	public boolean command()
	{
		return command;
	}
}