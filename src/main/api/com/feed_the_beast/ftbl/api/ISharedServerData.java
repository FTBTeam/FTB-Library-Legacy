package com.feed_the_beast.ftbl.api;

import net.minecraftforge.fml.relauncher.Side;

/**
 * @author LatvianModder
 */
public interface ISharedServerData extends ISharedData
{
	@Override
	default Side getSide()
	{
		return Side.SERVER;
	}
}