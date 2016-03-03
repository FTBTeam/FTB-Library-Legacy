package ftb.lib.mod.cmd;

import ftb.lib.*;
import ftb.lib.api.item.StringIDInvLoader;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

import java.io.File;

/**
 * Created by LatvianModder on 03.03.2016.
 */
public class CmdLoadInv extends CmdSaveInv
{
	public CmdLoadInv()
	{ super("ftb_loadinv"); }
	
	protected void onInvCmd(File file, EntityPlayerMP ep) throws Exception
	{
		NBTTagCompound tag = LMNBTUtils.readTag(file);
		
		StringIDInvLoader.readInvFromNBT(ep.inventory, tag, "Inventory");
		
		if(FTBLib.isModInstalled(OtherMods.BAUBLES))
			StringIDInvLoader.readInvFromNBT(BaublesHelper.getBaubles(ep), tag, "Baubles");
	}
}
