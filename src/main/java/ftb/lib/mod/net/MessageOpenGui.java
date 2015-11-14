package ftb.lib.mod.net;
import cpw.mods.fml.common.network.simpleimpl.*;
import cpw.mods.fml.relauncher.*;
import ftb.lib.api.*;
import ftb.lib.api.gui.*;
import ftb.lib.client.FTBLibClient;
import ftb.lib.mod.FTBLibMod;
import net.minecraft.nbt.NBTTagCompound;

public class MessageOpenGui extends MessageLM
{
	public MessageOpenGui() { super(DATA_LONG); }
	
	public MessageOpenGui(String mod, int id, NBTTagCompound tag, int wid)
	{
		this();
		io.writeString(mod);
		io.writeInt(id);
		writeTag(tag);
		io.writeUByte(wid);
	}
	
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET_GUI; }
	
	@SideOnly(Side.CLIENT)
	public IMessage onMessage(MessageContext ctx)
	{
		String modID = io.readString();
		int guiID = io.readInt();
		NBTTagCompound data = readTag();
		int windowID = io.readUByte();
		
		LMGuiHandler h = LMGuiHandlerRegistry.get(modID);
		if(h != null && FTBLibMod.proxy.openClientGui(FTBLibClient.mc.thePlayer, modID, guiID, data))
			FTBLibClient.mc.thePlayer.openContainer.windowId = windowID;
		return null;
	}
}