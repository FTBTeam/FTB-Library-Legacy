package ftb.lib.mod.net;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ftb.lib.FTBWorld;
import ftb.lib.api.EventFTBSync;
import ftb.lib.api.EventFTBWorldClient;
import ftb.lib.api.net.LMNetworkWrapper;
import ftb.lib.api.net.MessageLM;
import latmod.lib.ByteCount;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

public class MessageSyncData extends MessageLM
{
	public MessageSyncData() { super(ByteCount.INT); }
	
	public MessageSyncData(EntityPlayerMP ep, boolean login)
	{
		this();
		NBTTagCompound tag = EventFTBSync.generateData(ep, login);
		tag.setString("Mode", FTBWorld.server.getMode().getID());
		if(login) tag.setBoolean("Login", true);
		writeTag(tag);
	}
	
	@Override
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET; }
	
	@Override
	@SideOnly(Side.CLIENT)
	public IMessage onMessage(MessageContext ctx)
	{
		boolean first = FTBWorld.client == null;
		if(first) FTBWorld.client = new FTBWorld(Side.CLIENT);
		new EventFTBWorldClient(FTBWorld.client).post();
		NBTTagCompound tag = readTag();
		FTBWorld.client.setModeRaw(tag.getString("Mode"));
		EventFTBSync.readData(tag, tag.getBoolean("Login"));
		return null;
	}
}