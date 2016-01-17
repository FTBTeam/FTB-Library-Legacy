package ftb.lib.mod.cmd;

import ftb.lib.*;
import ftb.lib.api.config.ConfigRegistry;
import ftb.lib.cmd.*;
import ftb.lib.mod.config.FTBLibConfigCmd;
import ftb.lib.mod.net.MessageEditConfig;
import latmod.lib.*;
import latmod.lib.config.*;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.*;

public class CmdEditConfig extends CommandLM
{
	public CmdEditConfig()
	{ super(FTBLibConfigCmd.Name.edit_config.get(), CommandLevel.OP); }
	
	public String getCommandUsage(ICommandSender ics)
	{ return "/" + commandName + " <ID> [group] [entry] [value]"; }
	
	public String[] getTabStrings(ICommandSender ics, String args[], int i) throws CommandException
	{
		if(i == 0) return LMMapUtils.toKeyStringArray(ConfigRegistry.map);
		else if(i == 1)
		{
			IConfigFile file = ConfigRegistry.map.get(args[0]);
			if(file != null) return LMMapUtils.toKeyStringArray(file.getGroup().entryMap);
		}
		else if(i == 2)
		{
			IConfigFile file = ConfigRegistry.map.get(args[0]);
			if(file != null)
			{
				ConfigGroup group = file.getGroup().getGroup(args[1]);
				if(group != null) return LMMapUtils.toKeyStringArray(group.entryMap);
			}
		}
		
		return null;
	}
	
	public IChatComponent onCommand(ICommandSender ics, String[] args) throws CommandException
	{
		checkArgs(args, 1);
		
		if(args.length == 1 && ics instanceof EntityPlayerMP)
		{
			EntityPlayerMP ep = getCommandSenderAsPlayer(ics);
			IConfigFile file = ConfigRegistry.map.get(args[0]);
			
			if(file == null) return error(new ChatComponentText("Invalid file: '" + args[0] + "'!"));
			
			new MessageEditConfig(LMAccessToken.generate(ep), file.getGroup()).sendTo(ep);
			return null;
		}
		
		checkArgs(args, 3); // file, group, entry, value...
		
		IConfigFile file = ConfigRegistry.map.get(args[0]);
		if(file == null) return error(new ChatComponentText("Can only edit files!"));
		
		boolean success = false;
		ConfigGroup group = file.getGroup().getGroup(args[1]);
		ConfigEntry entry = (group == null) ? null : group.getEntry(args[2]);
		
		if(entry == null)
			return error(new ChatComponentText("Can't find config entry '" + args[0] + " " + args[1] + " " + args[2] + "'"));
		
		if(args.length >= 4)
		{
			String json = LMStringUtils.unsplitSpaceUntilEnd(3, args);
			
			FTBLib.logger.info("Setting " + args[0] + " " + args[1] + " " + args[2] + " to " + json);
			
			try
			{
				entry.setJson(LMJsonUtils.getJsonElement(json));
				if(group.parentFile != null) group.parentFile.save();
				FTBLib.reload(ics, true, false);
				return new ChatComponentText(args[2] + " set to " + entry.getPrettyJsonString(false));
			}
			catch(Exception ex)
			{
				ChatComponentText error = new ChatComponentText(ex.toString());
				error.getChatStyle().setColor(EnumChatFormatting.RED);
				return error;
			}
		}
		
		return new ChatComponentText(entry.getPrettyJsonString(false));
	}
}