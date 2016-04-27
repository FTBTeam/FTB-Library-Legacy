package ftb.lib.mod.net;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ftb.lib.api.client.FTBLibClient;
import ftb.lib.api.gui.LMGuiHandler;
import ftb.lib.api.gui.LMGuiHandlerRegistry;
import ftb.lib.api.net.LMNetworkWrapper;
import ftb.lib.api.net.MessageLM;
import ftb.lib.mod.FTBLibMod;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;

public class MessageOpenGui extends MessageLM<MessageOpenGui>
{
	public String mod;
	public int guiID;
	public NBTTagCompound tag;
	public int windowID;
	
	public MessageOpenGui() { }
	
	public MessageOpenGui(String m, int id, NBTTagCompound t, int wid)
	{
		mod = m;
		guiID = id;
		tag = t;
		windowID = wid;
	}
	
	@Override
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET; }
	
	@Override
	public void fromBytes(ByteBuf io)
	{
		mod = readString(io);
		guiID = io.readInt();
		tag = readTag(io);
		windowID = io.readInt();
	}
	
	@Override
	public void toBytes(ByteBuf io)
	{
		writeString(io, mod);
		io.writeInt(guiID);
		writeTag(io, tag);
		io.writeInt(windowID);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IMessage onMessage(MessageOpenGui m, MessageContext ctx)
	{
		LMGuiHandler h = LMGuiHandlerRegistry.get(m.mod);
		if(h != null && FTBLibMod.proxy.openClientGui(FTBLibClient.mc.thePlayer, m.mod, m.guiID, m.tag))
			FTBLibClient.mc.thePlayer.openContainer.windowId = m.windowID;
		return null;
	}
}