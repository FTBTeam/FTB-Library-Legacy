package ftb.lib.mod.cmd;

import ftb.lib.*;
import ftb.lib.api.cmd.*;
import ftb.lib.api.config.*;
import ftb.lib.mod.FTBLibMod;
import ftb.lib.mod.config.FTBLibConfigCmdNames;
import ftb.lib.mod.net.MessageEditConfig;
import latmod.lib.*;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.*;

import java.util.List;

public class CmdEditConfig extends CommandLM
{
	public CmdEditConfig()
	{ super(FTBLibConfigCmdNames.edit_config.getAsString(), CommandLevel.OP); }
	
	public String getCommandUsage(ICommandSender ics)
	{ return "/" + commandName + " <ID> [group] [entry] [value]"; }
	
	public List<String> addTabCompletionOptions(ICommandSender ics, String[] args)
	{
		if(args.length == 1) return getListOfStringsFromIterableMatchingLastWord(args, ConfigRegistry.map.keySet());
		else if(args.length == 2)
		{
			ConfigFile file = ConfigRegistry.map.get(args[0]);
			if(file != null) return getListOfStringsFromIterableMatchingLastWord(args, file.entryMap.keySet());
		}
		else if(args.length == 3)
		{
			ConfigFile file = ConfigRegistry.map.get(args[0]);
			if(file != null)
			{
				ConfigGroup group = file.getGroup(args[1]);
				if(group != null) return getListOfStringsFromIterableMatchingLastWord(args, group.entryMap.keySet());
			}
		}
		
		return null;
	}
	
	public void processCommand(ICommandSender ics, String[] args) throws CommandException
	{
		checkArgs(args, 1);
		
		if(args.length == 1 && ics instanceof EntityPlayerMP)
		{
			EntityPlayerMP ep = getCommandSenderAsPlayer(ics);
			ConfigFile file = ConfigRegistry.map.get(args[0]);
			
			if(file == null)
			{
				error("Invalid file: '" + args[0] + "'!");
				return;
			}
			
			new MessageEditConfig(LMAccessToken.generate(ep), true, file).sendTo(ep);
			return;
		}
		
		checkArgs(args, 3); // file, group, entry, value...
		
		ConfigFile file = ConfigRegistry.map.get(args[0]);
		if(file == null)
		{
			error("Can only edit files!");
			return;
		}
		
		boolean success = false;
		ConfigGroup group = file.getGroup(args[1]);
		ConfigEntry entry = (group == null) ? null : group.getEntry(args[2]);
		
		if(entry == null)
		{
			error("Can't find config entry '" + args[0] + " " + args[1] + " " + args[2] + "'");
		}
		
		if(args.length >= 4)
		{
			String json = LMStringUtils.unsplitSpaceUntilEnd(3, args);
			
			FTBLibMod.logger.info("Setting " + args[0] + " " + args[1] + " " + args[2] + " to " + json);
			
			try
			{
				entry.func_152753_a(LMJsonUtils.fromJson(json));
				file.save();
				FTBLib.reload(ics, true, false);
				ics.addChatMessage(new ChatComponentText(args[2] + " set to " + entry.getAsString()));
			}
			catch(Exception ex)
			{
				ChatComponentText c = new ChatComponentText(ex.toString());
				c.getChatStyle().setColor(EnumChatFormatting.RED);
				ics.addChatMessage(c);
			}
			
			return;
		}
		
		ics.addChatMessage(new ChatComponentText(entry.getAsString()));
	}
}