package com.feed_the_beast.ftbl.cmd;

import com.feed_the_beast.ftbl.api.cmd.CommandLM;
import com.feed_the_beast.ftbl.api.cmd.CommandLevel;
import com.feed_the_beast.ftbl.client.FTBLibModClient;
import com.feed_the_beast.ftbl.net.MessageReload;
import com.feed_the_beast.ftbl.util.ReloadType;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CmdReloadClient extends CommandLM
{
	public CmdReloadClient()
	{ super(FTBLibModClient.reload_client_cmd.getAsString(), CommandLevel.OP); }
	
	@Override
	public void execute(MinecraftServer server, ICommandSender ics, String[] args) throws CommandException
	{
		MessageReload.reloadClient(0L, ReloadType.CLIENT_ONLY, false);
	}
}