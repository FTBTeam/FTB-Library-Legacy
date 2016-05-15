package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.api.ForgePlayerSP;
import com.feed_the_beast.ftbl.api.ForgeWorldSP;
import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.api.net.LMNetworkWrapper;
import com.feed_the_beast.ftbl.api.net.MessageToClient;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageLMPlayerDied extends MessageToClient<MessageLMPlayerDied>
{
	public MessageLMPlayerDied() { }
	
	@Override
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET; }
	
	@Override
	public void fromBytes(ByteBuf io)
	{
	}
	
	@Override
	public void toBytes(ByteBuf io)
	{
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void onMessage(MessageLMPlayerDied m, Minecraft mc)
	{
		if(ForgeWorldSP.inst != null)
		{
			ForgePlayerSP p = ForgeWorldSP.inst.getPlayer(FTBLibClient.mc.thePlayer);
			p.onDeath();
		}
	}
}