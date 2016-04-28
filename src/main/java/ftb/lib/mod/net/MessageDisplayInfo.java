package ftb.lib.mod.net;

import com.google.gson.JsonElement;
import ftb.lib.api.client.FTBLibClient;
import ftb.lib.api.info.InfoPage;
import ftb.lib.api.net.LMNetworkWrapper;
import ftb.lib.api.net.MessageLM;
import ftb.lib.mod.client.gui.info.GuiInfo;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageDisplayInfo extends MessageLM<MessageDisplayInfo>
{
	public String infoID;
	public JsonElement json;
	
	public MessageDisplayInfo() { }
	
	public MessageDisplayInfo(InfoPage page)
	{
		infoID = page.getID();
		json = page.getSerializableElement();
	}
	
	@Override
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET_INFO; }
	
	@Override
	public void fromBytes(ByteBuf io)
	{
		infoID = readString(io);
		json = readJsonElement(io);
	}
	
	@Override
	public void toBytes(ByteBuf io)
	{
		writeString(io, infoID);
		writeJsonElement(io, json);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IMessage onMessage(MessageDisplayInfo m, MessageContext ctx)
	{
		InfoPage page = new InfoPage(m.infoID);
		page.fromJson(m.json);
		FTBLibClient.openGui(new GuiInfo(null, page));
		return null;
	}
}