package ftb.lib.mod.net;

import ftb.lib.api.client.FTBLibClient;
import ftb.lib.api.info.InfoPage;
import ftb.lib.api.net.*;
import ftb.lib.mod.client.gui.info.GuiInfo;
import latmod.lib.ByteCount;
import latmod.lib.json.JsonElementIO;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.*;

public class MessageDisplayGuide extends MessageLM_IO
{
	public MessageDisplayGuide() { super(ByteCount.INT); }
	
	public MessageDisplayGuide(InfoPage file)
	{
		this();
		io.writeUTF(file.getID());
		JsonElementIO.write(io, file.getSerializableElement());
	}
	
	@Override
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET_INFO; }
	
	@Override
	@SideOnly(Side.CLIENT)
	public IMessage onMessage(MessageContext ctx)
	{
		InfoPage file = new InfoPage(io.readUTF());
		file.fromJson(JsonElementIO.read(io));
		FTBLibClient.openGui(new GuiInfo(null, file));
		return null;
	}
}