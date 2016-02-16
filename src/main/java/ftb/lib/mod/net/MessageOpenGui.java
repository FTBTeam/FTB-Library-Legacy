package ftb.lib.mod.net;

import ftb.lib.api.client.FTBLibClient;
import ftb.lib.api.gui.*;
import ftb.lib.api.net.*;
import ftb.lib.mod.FTBLibMod;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.*;

public class MessageOpenGui extends MessageLM<MessageOpenGui>
{
	public String modID;
	public int guiID;
	public NBTTagCompound data;
	public int windowID;
	
	public MessageOpenGui() { }
	
	public MessageOpenGui(String mod, int id, NBTTagCompound tag, int wid)
	{
		modID = mod;
		guiID = id;
		data = tag;
		windowID = wid;
	}
	
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET_GUI; }
	
	public void fromBytes(ByteBuf io)
	{
		modID = readString(io);
		guiID = io.readInt();
		data = readTag(io);
		windowID = io.readUnsignedByte();
	}
	
	public void toBytes(ByteBuf io)
	{
		writeString(io, modID);
		io.writeInt(guiID);
		writeTag(io, data);
		io.writeByte(windowID);
	}
	
	public IMessage onMessage(MessageOpenGui m, MessageContext ctx)
	{
		LMGuiHandler h = LMGuiHandlerRegistry.get(m.modID);
		if(h != null && FTBLibMod.proxy.openClientGui(FTBLibClient.mc.thePlayer, m.modID, m.guiID, m.data))
			FTBLibClient.mc.thePlayer.openContainer.windowId = m.windowID;
		return null;
	}
}