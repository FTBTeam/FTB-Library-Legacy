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
		if(i == 0) return ConfigRegistry.map.getKeyStringArray();
		else if(i == 1)
		{
			ConfigGroup list = ConfigRegistry.map.get(args[0]).getGroup();
			if(list != null) return list.entryMap.getKeyStringArray();
		}
		else if(i == 2)
		{
			ConfigGroup file = ConfigRegistry.map.get(args[0]).getGroup();
			if(file != null)
			{
				ConfigGroup group = file.getGroup(args[1]);
				if(group != null) return group.entryMap.getKeyStringArray();
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

			if(!ConfigRegistry.map.containsKey(args[0]))
				return error(new ChatComponentText("Invalid file: '" + args[0] + "'!"));

			ConfigGroup group = ConfigRegistry.map.get(args[0]).getGroup();

			if(group != null && group.parentFile != null)
			{
				new MessageEditConfig(AdminToken.generate(ep), false, group).sendTo(ep);
				return null;
			}
			
			return error(new ChatComponentText("Invalid file: '" + args[0] + "'!"));
		}
		
		checkArgs(args, 3); // file, group, entry, value...

		ConfigRegistry.Provider p = ConfigRegistry.map.get(args[0]);
		if(!(p instanceof ConfigRegistry.ConfigFileProvider))
			return new ChatComponentText("Can only edit files!");

		ConfigGroup file = p.getGroup();

		boolean success = false;
		if(file != null)
		{
			ConfigGroup group = file.getGroup(args[1]);

			if(group != null)
			{
				ConfigEntry entry = group.getEntry(args[2]);
				
				if(entry != null)
				{
					success = true;
					
					if(args.length >= 4)
					{
						String json = LMStringUtils.unsplitSpaceUntilEnd(3, args);
						
						FTBLib.logger.info("Setting " + args[0] + " " + args[1] + " " + args[2] + " to " + json);
						
						try
						{
							entry.setJson(LMJsonUtils.getJsonElement(json));
							if(file.parentFile != null) file.parentFile.save();
						}
						catch(Exception ex)
						{
							ChatComponentText error = new ChatComponentText(ex.getMessage());
							error.getChatStyle().setColor(EnumChatFormatting.RED);
							return error;
						}
					}
					else return new ChatComponentText(entry.getPrettyJsonString(false));
				}
			}
		}
		
		if(!success) return new ChatComponentText("Can't find config entry '" + args[0] + " " + args[1] + " " + args[2] + "'");
		
		FTBLib.reload(ics, true, true);
		return null;
	}
}