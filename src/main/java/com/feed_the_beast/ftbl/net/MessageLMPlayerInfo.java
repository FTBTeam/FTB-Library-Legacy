package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.api.ForgePlayerMP;
import com.feed_the_beast.ftbl.api.ForgePlayerSP;
import com.feed_the_beast.ftbl.api.ForgeWorldSP;
import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.api.net.LMNetworkWrapper;
import com.feed_the_beast.ftbl.api.net.MessageToClient;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.UUID;

public class MessageLMPlayerInfo extends MessageToClient<MessageLMPlayerInfo>
{
	public UUID playerID;
	public String[] info;
	public ItemStack[] armor;
	
	MessageLMPlayerInfo() {}
	
	public MessageLMPlayerInfo(ForgePlayerMP owner, ForgePlayerMP p)
	{
		playerID = p.getProfile().getId();
		
		ArrayList<ITextComponent> info0 = new ArrayList<>();
		p.getInfo(owner, info0);
		
		info = new String[Math.min(255, info0.size())];
		
		for(int i = 0; i < info.length; i++)
		{
			info[i] = ITextComponent.Serializer.componentToJson(info0.get(i));
		}
		
		armor = new ItemStack[5];
		for(int i = 0; i < armor.length; i++)
		{
			armor[i] = p.lastArmor.get(i);
		}
	}
	
	@Override
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET; }
	
	@Override
	public void fromBytes(ByteBuf io)
	{
		playerID = readUUID(io);
		
		info = new String[io.readUnsignedByte()];
		for(int i = 0; i < info.length; i++)
		{
			info[i] = ByteBufUtils.readUTF8String(io);
		}
		
		armor = new ItemStack[5];
		for(int i = 0; i < armor.length; i++)
		{
			armor[i] = ByteBufUtils.readItemStack(io);
		}
	}
	
	@Override
	public void toBytes(ByteBuf io)
	{
		writeUUID(io, playerID);
		
		io.writeByte(info.length);
		for(String anInfo : info)
		{
			ByteBufUtils.writeUTF8String(io, anInfo);
		}
		
		for(ItemStack anArmor : armor)
		{
			ByteBufUtils.writeItemStack(io, anArmor);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void onMessage(MessageLMPlayerInfo m, Minecraft mc)
	{
		if(ForgeWorldSP.inst == null)
		{
			return;
		}
		
		ForgePlayerSP p = ForgeWorldSP.inst.getPlayer(m.playerID).toPlayerSP();
		if(p == null)
		{
			return;
		}
		
		ArrayList<ITextComponent> info = new ArrayList<>();
		for(int i = 0; i < m.info.length; i++)
		{
			info.add(ITextComponent.Serializer.jsonToComponent(m.info[i]));
		}
		
		p.receiveInfo(info);
		
		p.lastArmor.clear();
		
		for(int i = 0; i < m.armor.length; i++)
		{
			if(m.armor[i] != null) { p.lastArmor.put(i, m.armor[i].copy()); }
		}
		
		FTBLibClient.onGuiClientAction();
	}
}