package ftb.lib.mod.cmd;

import java.util.*;

import net.minecraft.command.*;
import net.minecraft.server.MinecraftServer;

public class CmdHelpOverride extends CommandHelp
{
	public CmdHelpOverride()
	{ super(); }
	
	public String getCommandUsage(ICommandSender ics)
	{ return '/' + getCommandName() + " [command]"; }
	
	@SuppressWarnings("all")
	protected List getSortedPossibleCommands(ICommandSender ics)
    {
        List list = MinecraftServer.getServer().getCommandManager().getPossibleCommands(ics);
        try { Collections.sort(list); } catch(Exception e) { }
        return list;
    }
}