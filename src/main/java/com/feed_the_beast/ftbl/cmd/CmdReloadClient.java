package com.feed_the_beast.ftbl.cmd;

import com.feed_the_beast.ftbl.api.EnumReloadType;
import com.feed_the_beast.ftbl.api.FTBLibAPI;
import com.feed_the_beast.ftbl.api.events.ReloadEvent;
import com.feed_the_beast.ftbl.lib.cmd.CmdBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
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
		ResourceLocation id = ReloadEvent.ALL;

		if (args.length >= 1)
		{
			if (args[0].indexOf(':') != -1)
			{
				id = new ResourceLocation(args[0]);
			}
			else
			{
				id = new ResourceLocation(args[0] + ":*");
			}
		}

		FTBLibAPI.API.reload(Side.CLIENT, sender, EnumReloadType.RELOAD_COMMAND, id);
	}
}