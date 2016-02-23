package ftb.lib.mod.net;

import ftb.lib.api.client.FTBLibClient;
import ftb.lib.api.net.*;
import ftb.lib.api.players.*;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.*;

import java.util.*;

public class MessageLMPlayerInfo extends MessageLM<MessageLMPlayerInfo>
{
	public UUID playerID;
	public String[] info;
	public ItemStack[] armor;
	
	public MessageLMPlayerInfo() { }
	
	public MessageLMPlayerInfo(LMPlayerMP owner, LMPlayerMP p)
	{
		playerID = p.getProfile().getId();
		
		ArrayList<IChatComponent> info0 = new ArrayList<>();
		p.getInfo(owner, info0);
		
		info = new String[Math.min(255, info0.size())];
		
		for(int i = 0; i < info.length; i++)
		{
			info[i] = IChatComponent.Serializer.componentToJson(info0.get(i));
		}
		
		armor = new ItemStack[5];
		for(int i = 0; i < armor.length; i++)
		{
			armor[i] = p.lastArmor.get(i);
		}
	}
	
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET_INFO; }
	
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
	
	@SideOnly(Side.CLIENT)
	public IMessage onMessage(MessageLMPlayerInfo m, MessageContext ctx)
	{
		if(LMWorldSP.inst == null) return null;
		LMPlayerSP p = LMWorldSP.inst.getPlayer(m.playerID).toPlayerSP();
		if(p == null) return null;
		
		ArrayList<IChatComponent> info = new ArrayList<>();
		for(int i = 0; i < m.info.length; i++)
		{
			info.add(IChatComponent.Serializer.jsonToComponent(m.info[i]));
		}
		
		p.receiveInfo(info);
		
		p.lastArmor.clear();
		
		for(int i = 0; i < m.armor.length; i++)
		{
			if(m.armor[i] != null) p.lastArmor.put(i, m.armor[i].copy());
		}
		
		FTBLibClient.onGuiClientAction();
		return null;
	}
}