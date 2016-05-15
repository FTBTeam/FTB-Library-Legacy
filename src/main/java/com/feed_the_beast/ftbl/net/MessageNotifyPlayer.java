package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.api.net.LMNetworkWrapper;
import com.feed_the_beast.ftbl.api.net.MessageToClient;
import com.feed_the_beast.ftbl.api.notification.ClientNotifications;
import com.feed_the_beast.ftbl.api.notification.Notification;
import com.feed_the_beast.ftbl.client.FTBLibModClient;
import com.feed_the_beast.ftbl.util.EnumScreen;
import io.netty.buffer.ByteBuf;
import latmod.lib.LMJsonUtils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageNotifyPlayer extends MessageToClient<MessageNotifyPlayer>
{
	public String json;
	
	public MessageNotifyPlayer() { }
	
	public MessageNotifyPlayer(Notification n)
	{
		json = LMJsonUtils.toJson(n.getSerializableElement());
	}
	
	@Override
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET; }
	
	@Override
	public void fromBytes(ByteBuf io)
	{
		json = readString(io);
	}
	
	@Override
	public void toBytes(ByteBuf io)
	{
		writeString(io, json);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void onMessage(MessageNotifyPlayer m, Minecraft mc)
	{
		if(FTBLibModClient.notifications.get() != EnumScreen.OFF)
		{
			Notification n = Notification.deserialize(LMJsonUtils.fromJson(m.json));
			
			if(FTBLibModClient.notifications.get() == EnumScreen.SCREEN) { ClientNotifications.add(n); }
			else
			{
				FTBLibClient.mc.thePlayer.addChatMessage(n.title);
				if(n.desc != null) { FTBLibClient.mc.thePlayer.addChatMessage(n.desc); }
			}
		}
	}
}