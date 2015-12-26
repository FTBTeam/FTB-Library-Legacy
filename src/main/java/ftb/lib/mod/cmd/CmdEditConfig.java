package ftb.lib.mod.cmd;

import ftb.lib.*;
import ftb.lib.api.config.ConfigRegistry;
import ftb.lib.cmd.*;
import ftb.lib.mod.config.FTBLibConfigCmd;
import ftb.lib.mod.net.MessageEditConfig;
import latmod.lib.*;
import latmod.lib.config.*;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.*;

public class CmdEditConfig extends CommandLM
{
	public CmdEditConfig()
	{ super(FTBLibConfigCmd.Name.edit_config.get(), CommandLevel.OP); }
	
	public String getCommandUsage(ICommandSender ics)
	{ return "/" + commandName + " <ID> [group] [entry] [value]"; }
	
	public String[] getTabStrings(ICommandSender ics, String args[], int i)
	{
		if(i == 0) return ConfigRegistry.list.toStringArray();
		else if(i == 1)
		{
			ConfigGroup list = ConfigRegistry.list.getObj(args[0]);
			if(list != null) return list.entryMap().getKeyStringArray();
		}
		else if(i == 2)
		{
			ConfigGroup file = ConfigRegistry.list.getObj(args[0]);
			if(file != null)
			{
				ConfigGroup group = file.getGroup(args[1]);
				if(group != null) return group.entryMap().getKeyStringArray();
			}
		}
		
		return null;
	}
	
	public IChatComponent onCommand(ICommandSender ics, String[] args)
	{
		checkArgs(args, 1);
		
		if(args.length == 1 && ics instanceof EntityPlayerMP)
		{
			EntityPlayerMP ep = getCommandSenderAsPlayer(ics);
			
			if(args[0].startsWith("Forge:") && args[0].length() > 6 && args[0].indexOf('.') > 0)
			{
				/*
				String filename = args[0].substring(6, args[0].length());
				File file = new File(FTBLib.folderConfig, filename);
				
				if(file.exists() && file.canRead() && file.canWrite())
				{
					Configuration c = new Configuration(file);
					new MessageEditConfig(AdminToken.generate(ep), new ForgeConfigFile(filename, c).configGroup).sendTo(ep);
					return null;
				}
				*/
			}
			else
			{
				ConfigGroup group = ConfigRegistry.list.getObj(args[0]);
				
				if(group != null)
				{
					new MessageEditConfig(AdminToken.generate(ep), false, group).sendTo(ep);
					return null;
				}
			}
			
			return error(new ChatComponentText("Invalid config: " + args[0] + "!"));
		}
		
		checkArgs(args, 3); // file, group, entry, value...
		
		ConfigGroup file = ConfigRegistry.list.getObj(args[0]);
		
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
					else return new ChatComponentText(entry.getJson().toString());
				}
			}
		}
		
		if(!success) return new ChatComponentText("Can't find config entry '" + args[0] + " " + args[1] + " " + args[2] + "'");
		
		FTBLib.reload(ics, true, true);
		return null;
	}
}