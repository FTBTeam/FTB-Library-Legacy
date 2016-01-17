package ftb.lib.mod.net;

import ftb.lib.FTBLibClient;
import ftb.lib.api.*;
import ftb.lib.api.gui.*;
import ftb.lib.mod.FTBLibMod;
import latmod.lib.ByteCount;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.*;

public class MessageOpenGui extends MessageLM
{
	public MessageOpenGui() { super(ByteCount.INT); }
	
	public MessageOpenGui(String mod, int id, NBTTagCompound tag, int wid)
	{
		this();
		io.writeUTF(mod);
		io.writeInt(id);
		writeTag(tag);
		io.writeByte(wid);
	}
	
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET_GUI; }
	
	@SideOnly(Side.CLIENT)
	public IMessage onMessage(MessageContext ctx)
	{
		String modID = io.readUTF();
		int guiID = io.readInt();
		NBTTagCompound data = readTag();
		int windowID = io.readUnsignedByte();
		
		LMGuiHandler h = LMGuiHandlerRegistry.get(modID);
		if(h != null && FTBLibMod.proxy.openClientGui(FTBLibClient.mc.thePlayer, modID, guiID, data))
			FTBLibClient.mc.thePlayer.openContainer.windowId = windowID;
		return null;
	}
}