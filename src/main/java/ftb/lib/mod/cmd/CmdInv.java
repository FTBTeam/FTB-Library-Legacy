package ftb.lib.mod.cmd;

import ftb.lib.BaublesHelper;
import ftb.lib.FTBLib;
import ftb.lib.LMNBTUtils;
import ftb.lib.OtherMods;
import ftb.lib.api.cmd.CommandLM;
import ftb.lib.api.cmd.CommandLevel;
import ftb.lib.api.cmd.CommandSubLM;
import ftb.lib.api.item.LMInvUtils;
import ftb.lib.mod.FTBLibLang;
import latmod.lib.LMUtils;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

import java.io.File;

public class CmdInv extends CommandSubLM
{
	public CmdInv()
	{
		super("ftb_inv", CommandLevel.OP);
	}
	
	public static class CmdSave extends CommandLM
	{
		public CmdSave(String s)
		{ super(s, CommandLevel.OP); }
		
		@Override
		public String getCommandUsage(ICommandSender ics)
		{ return '/' + commandName + " <player> <file_id>"; }
		
		@Override
		public boolean isUsernameIndex(String[] args, int i)
		{ return i == 0; }
		
		@Override
		public void execute(MinecraftServer server, ICommandSender ics, String[] args) throws CommandException
		{
			checkArgs(args, 2);
			EntityPlayerMP ep = getPlayer(server, ics, args[0]);
			File file = new File(FTBLib.folderLocal, "ftbu/playerinvs/" + LMUtils.fromUUID(ep.getGameProfile().getId()) + "_" + args[1].toLowerCase() + ".dat");
			
			try
			{
				onInvCmd(file, ep);
			}
			catch(Exception e)
			{
				if(FTBLib.DEV_ENV) { e.printStackTrace(); }
				throw FTBLibLang.raw.commandError("Failed to load inventory!");
			}
		}
		
		protected void onInvCmd(File file, EntityPlayerMP ep) throws Exception
		{
			NBTTagCompound tag = new NBTTagCompound();
			LMInvUtils.writeItemsToNBT(ep.inventory, tag, "Inventory");
			
			if(FTBLib.isModInstalled(OtherMods.BAUBLES))
			{ LMInvUtils.writeItemsToNBT(BaublesHelper.getBaubles(ep), tag, "Baubles"); }
			
			LMNBTUtils.writeTag(file, tag);
		}
	}
	
	public static class CmdLoad extends CmdSave
	{
		public CmdLoad(String s)
		{ super(s); }
		
		@Override
		protected void onInvCmd(File file, EntityPlayerMP ep) throws Exception
		{
			NBTTagCompound tag = LMNBTUtils.readTag(file);
			
			LMInvUtils.readItemsFromNBT(ep.inventory, tag, "Inventory");
			
			if(FTBLib.isModInstalled(OtherMods.BAUBLES))
			{ LMInvUtils.readItemsFromNBT(BaublesHelper.getBaubles(ep), tag, "Baubles"); }
		}
	}
	
	public static class CmdList extends CommandLM
	{
		public CmdList(String s)
		{ super(s, CommandLevel.OP); }
		
		@Override
		public void execute(MinecraftServer server, ICommandSender ics, String[] args) throws CommandException
		{
		}
	}
}