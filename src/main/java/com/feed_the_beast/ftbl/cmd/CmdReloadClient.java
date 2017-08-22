package com.feed_the_beast.ftbl.cmd;

import net.minecraftforge.fml.relauncher.Side;

/**
 * @author LatvianModder
 */
public class CmdReloadClient extends CmdReload
{
	public CmdReloadClient()
	{
		super("reload_client", Level.ALL);
	}

	@Override
	protected Side getSide()
	{
		return Side.CLIENT;
	}
}