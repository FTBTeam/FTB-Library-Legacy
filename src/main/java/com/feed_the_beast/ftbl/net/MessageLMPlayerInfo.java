package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.api.ForgePlayerMP;
import com.feed_the_beast.ftbl.api.ForgePlayerSP;
import com.feed_the_beast.ftbl.api.ForgeWorldSP;
import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.api.net.LMNetworkWrapper;
import com.feed_the_beast.ftbl.api.net.MessageToClient;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MessageLMPlayerInfo extends MessageToClient<MessageLMPlayerInfo>
{
	public UUID playerID;
	public String[] info;
	public Map<EntityEquipmentSlot, ItemStack> armor;
	
	public MessageLMPlayerInfo() {}
	
	public MessageLMPlayerInfo(ForgePlayerMP owner, ForgePlayerMP p)
	{
		playerID = p.getProfile().getId();
		
		List<ITextComponent> info0 = new ArrayList<>();
		p.getInfo(owner, info0);
		
		info = new String[Math.min(255, info0.size())];
		
		for(int i = 0; i < info.length; i++)
		{
			info[i] = ITextComponent.Serializer.componentToJson(info0.get(i));
		}
		
		armor = p.lastArmor;
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
			info[i] = readString(io);
		}
		
		armor = new HashMap<>();
		int s = io.readUnsignedByte();
		
		for(int i = 0; i < s; i++)
		{
			EntityEquipmentSlot e = EntityEquipmentSlot.values()[io.readByte()];
			ItemStack is = ByteBufUtils.readItemStack(io);
			armor.put(e, is);
		}
	}
	
	@Override
	public void toBytes(ByteBuf io)
	{
		writeUUID(io, playerID);
		
		io.writeByte(info.length);
		for(String anInfo : info)
		{
			writeString(io, anInfo);
		}
		
		io.writeByte(armor.size());
		
		for(Map.Entry<EntityEquipmentSlot, ItemStack> e : armor.entrySet())
		{
			io.writeByte(e.getKey().ordinal());
			ByteBufUtils.writeItemStack(io, e.getValue());
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
		
		List<ITextComponent> info = new ArrayList<>();
		for(int i = 0; i < m.info.length; i++)
		{
			info.add(ITextComponent.Serializer.jsonToComponent(m.info[i]));
		}
		
		p.receiveInfo(info);
		
		p.lastArmor.clear();
		p.lastArmor.putAll(m.armor);
		
		FTBLibClient.onGuiClientAction();
	}
}