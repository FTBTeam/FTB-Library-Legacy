package com.feed_the_beast.ftbl.cmd;

import com.feed_the_beast.ftbl.api.EnumReloadType;
import com.feed_the_beast.ftbl.api.FTBLibAPI;
import com.feed_the_beast.ftbl.lib.cmd.CmdBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.relauncher.Side;

/**
 * @author LatvianModder
 */
public class CmdReloadClient extends CmdBase
{
	public CmdReloadClient()
	{
		super("reload_client", Level.ALL);
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		FTBLibAPI.API.reload(Side.CLIENT, sender, EnumReloadType.RELOAD_COMMAND);
	}
}