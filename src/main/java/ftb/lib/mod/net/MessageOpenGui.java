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
import latmod.lib.ByteCount;
import net.minecraft.nbt.NBTTagCompound;

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
	
	@Override
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET; }
	
	@Override
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