package ftb.lib.mod.net;

import com.google.gson.JsonElement;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ftb.lib.api.client.FTBLibClient;
import ftb.lib.api.info.InfoPage;
import ftb.lib.api.net.LMNetworkWrapper;
import ftb.lib.api.net.MessageLM;
import ftb.lib.mod.client.gui.info.GuiInfo;
import io.netty.buffer.ByteBuf;

public class MessageDisplayInfo extends MessageLM<MessageDisplayInfo>
{
	public String pageID;
	public JsonElement json;
	
	public MessageDisplayInfo() { }
	
	public MessageDisplayInfo(InfoPage page)
	{
		page.cleanup();
		pageID = page.getID();
		json = page.getSerializableElement();
	}
	
	@Override
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET; }
	
	@Override
	public void fromBytes(ByteBuf io)
	{
		pageID = readString(io);
		json = readJsonElement(io);
	}
	
	@Override
	public void toBytes(ByteBuf io)
	{
		writeString(io, pageID);
		writeJsonElement(io, json);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IMessage onMessage(MessageDisplayInfo m, MessageContext ctx)
	{
		InfoPage page = new InfoPage(m.pageID);
		page.func_152753_a(m.json);
		FTBLibClient.openGui(new GuiInfo(null, page));
		return null;
	}
}