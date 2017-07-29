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
public class CmdReload extends CmdBase
{
	public CmdReload()
	{
		super("reload", Level.OP);
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		FTBLibAPI.API.reload(Side.SERVER, sender, EnumReloadType.RELOAD_COMMAND);
	}
}