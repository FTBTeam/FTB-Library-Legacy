package ftb.lib.mod.cmd;

import ftb.lib.*;
import ftb.lib.api.cmd.*;
import ftb.lib.api.item.StringIDInvLoader;
import latmod.lib.json.UUIDTypeAdapterLM;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;

import java.io.File;

/**
 * Created by LatvianModder on 03.03.2016.
 */
public class CmdSaveInv extends CommandLM
{
	public CmdSaveInv()
	{ this("ftb_saveinv"); }
	
	protected CmdSaveInv(String id)
	{ super(id, CommandLevel.OP); }
	
	public String getCommandUsage(ICommandSender ics)
	{ return '/' + commandName + " <player> <file_id>"; }
	
	public Boolean getUsername(String[] args, int i)
	{ return (i == 0) ? Boolean.FALSE : null; }
	
	public IChatComponent onCommand(ICommandSender ics, String[] args) throws CommandException
	{
		checkArgs(args, 2);
		EntityPlayerMP ep = getPlayer(ics, args[0]);
		File file = new File(FTBLib.folderLocal, "ftbu/playerinvs/" + UUIDTypeAdapterLM.getString(ep.getGameProfile().getId()) + "_" + args[1].toLowerCase() + ".dat");
		
		try
		{
			onInvCmd(file, ep);
		}
		catch(Exception e)
		{
			if(FTBLib.DEV_ENV) e.printStackTrace();
			return error(new ChatComponentText("Failed to load inventory!"));
		}
		
		return null;
	}
	
	protected void onInvCmd(File file, EntityPlayerMP ep) throws Exception
	{
		NBTTagCompound tag = new NBTTagCompound();
		StringIDInvLoader.writeInvToNBT(ep.inventory, tag, "Inventory");
		
		if(FTBLib.isModInstalled(OtherMods.BAUBLES))
			StringIDInvLoader.writeInvToNBT(BaublesHelper.getBaubles(ep), tag, "Baubles");
		
		LMNBTUtils.writeTag(file, tag);
	}
}
