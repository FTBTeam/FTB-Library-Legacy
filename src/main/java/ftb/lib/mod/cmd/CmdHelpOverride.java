package ftb.lib.mod.cmd;

import net.minecraft.command.CommandHelp;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

import java.util.Collections;
import java.util.List;

public class CmdHelpOverride extends CommandHelp
{
	public CmdHelpOverride()
	{ super(); }
	
	@Override
	public String getCommandUsage(ICommandSender ics)
	{ return '/' + getCommandName() + " [command]"; }
	
	@Override
	protected List<ICommand> getSortedPossibleCommands(ICommandSender ics)
	{
		List<ICommand> list = MinecraftServer.getServer().getCommandManager().getPossibleCommands(ics);
		
		try
		{
			Collections.sort(list);
		}
		catch(Exception e) { }
		
		return list;
	}
}