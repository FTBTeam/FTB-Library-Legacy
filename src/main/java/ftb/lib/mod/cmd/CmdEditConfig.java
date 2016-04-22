package ftb.lib.mod.cmd;

import ftb.lib.*;
import ftb.lib.api.cmd.*;
import ftb.lib.api.config.*;
import ftb.lib.mod.FTBLibMod;
import ftb.lib.mod.net.MessageEditConfig;
import latmod.lib.*;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import java.util.List;

public class CmdEditConfig extends CommandLM
{
	public CmdEditConfig()
	{ super("edit_config", CommandLevel.OP); }
	
	@Override
	public String getCommandUsage(ICommandSender ics)
	{ return "/" + commandName + " <ID> [group] [entry] [value]"; }
	
	@Override
	public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender ics, String[] args, BlockPos pos)
	{
		if(args.length == 1)
		{
			return getListOfStringsMatchingLastWord(args, ConfigRegistry.map.keySet());
		}
		else if(args.length == 2)
		{
			ConfigFile file = ConfigRegistry.map.get(args[0]);
			if(file != null) return getListOfStringsMatchingLastWord(args, file.entryMap.keySet());
		}
		else if(args.length == 3)
		{
			ConfigFile file = ConfigRegistry.map.get(args[0]);
			if(file != null)
			{
				ConfigGroup group = file.getGroup(args[1]);
				if(group != null) return getListOfStringsMatchingLastWord(args, group.entryMap.keySet());
			}
		}
		
		return getTabCompletionOptions(server, ics, args, pos);
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender ics, String[] args) throws CommandException
	{
		checkArgs(args, 1);
		
		if(args.length == 1 && ics instanceof EntityPlayerMP)
		{
			EntityPlayerMP ep = getCommandSenderAsPlayer(ics);
			ConfigFile file = ConfigRegistry.map.get(args[0]);
			
			if(file == null)
			{
				throw new RawCommandException("Invalid file: '" + args[0] + "'!");
			}
			
			new MessageEditConfig(LMAccessToken.generate(ep), true, file).sendTo(ep);
			return;
		}
		
		checkArgs(args, 3); // file, group, entry, value...
		
		ConfigFile file = ConfigRegistry.map.get(args[0]);
		if(file == null)
		{
			throw new RawCommandException("Can only edit files!");
		}
		
		boolean success = false;
		ConfigGroup group = file.getGroup(args[1]);
		ConfigEntry entry = (group == null) ? null : group.entryMap.get(args[2]);
		
		if(entry == null)
		{
			throw new RawCommandException("Can't find config entry '" + args[0] + " " + args[1] + " " + args[2] + "'");
		}
		
		if(args.length >= 4)
		{
			String json = LMStringUtils.unsplitSpaceUntilEnd(3, args);
			
			FTBLibMod.logger.info("Setting " + args[0] + " " + args[1] + " " + args[2] + " to " + json);
			
			try
			{
				entry.fromJson(LMJsonUtils.fromJson(json));
				file.save();
				FTBLib.reload(ics, true, false);
				ics.addChatMessage(new TextComponentString(args[2] + " set to " + entry.getSerializableElement()));
				return;
			}
			catch(Exception ex)
			{
				throw new RawCommandException(ex.toString());
			}
		}
		
		ics.addChatMessage(new TextComponentString(String.valueOf(entry.getSerializableElement())));
	}
}