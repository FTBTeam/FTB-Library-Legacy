package com.feed_the_beast.ftblib.lib.data;

import net.minecraftforge.fml.relauncher.Side;

/**
 * @author LatvianModder
 */
public class SharedClientData extends SharedData
{
	public static final SharedClientData INSTANCE = new SharedClientData();

	private SharedClientData()
	{
	}

	@Override
	public Side getSide()
	{
		return Side.CLIENT;
	}

	@Override
	public void reset()
	{
		super.reset();
		optionalServerMods.clear();
	}
}