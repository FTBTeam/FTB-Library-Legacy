package ftb.lib.mod.net;

import ftb.lib.api.gui.LMGuiHandler;
import ftb.lib.api.gui.LMGuiHandlerRegistry;
import ftb.lib.api.net.LMNetworkWrapper;
import ftb.lib.api.net.MessageToClient;
import ftb.lib.mod.FTBLibMod;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageOpenGui extends MessageToClient<MessageOpenGui>
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
	
	@Override
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET; }
	
	@Override
	public void fromBytes(ByteBuf io)
	{
		modID = readString(io);
		guiID = io.readInt();
		data = readTag(io);
		windowID = io.readUnsignedByte();
	}
	
	@Override
	public void toBytes(ByteBuf io)
	{
		writeString(io, modID);
		io.writeInt(guiID);
		writeTag(io, data);
		io.writeByte(windowID);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void onMessage(MessageOpenGui m, Minecraft mc)
	{
		LMGuiHandler h = LMGuiHandlerRegistry.get(m.modID);
		
		if(h != null && FTBLibMod.proxy.openClientGui(mc.thePlayer, m.modID, m.guiID, m.data))
		{
			mc.thePlayer.openContainer.windowId = m.windowID;
		}
	}
}