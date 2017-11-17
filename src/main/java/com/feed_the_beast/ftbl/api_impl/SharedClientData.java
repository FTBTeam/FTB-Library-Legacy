package com.feed_the_beast.ftbl.api_impl;

import com.feed_the_beast.ftbl.api.ISharedClientData;

/**
 * @author LatvianModder
 */
public class SharedClientData extends SharedData implements ISharedClientData
{
	public static final SharedClientData INSTANCE = new SharedClientData();

	private SharedClientData()
	{
	}

	@Override
	public void reset()
	{
		super.reset();
		optionalServerMods.clear();
	}
}