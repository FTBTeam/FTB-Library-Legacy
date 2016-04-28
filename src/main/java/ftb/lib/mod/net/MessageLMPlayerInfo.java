package ftb.lib.mod.net;

import ftb.lib.api.ForgePlayerMP;
import ftb.lib.api.ForgePlayerSP;
import ftb.lib.api.ForgeWorldSP;
import ftb.lib.api.client.FTBLibClient;
import ftb.lib.api.net.LMNetworkWrapper;
import ftb.lib.api.net.MessageLM;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.UUID;

public class MessageLMPlayerInfo extends MessageLM<MessageLMPlayerInfo>
{
	public UUID playerID;
	public String[] info;
	public ItemStack[] armor;
	
	public MessageLMPlayerInfo() { }
	
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
	{ return FTBLibNetHandler.NET_INFO; }
	
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
	public IMessage onMessage(MessageLMPlayerInfo m, MessageContext ctx)
	{
		if(ForgeWorldSP.inst == null) return null;
		ForgePlayerSP p = ForgeWorldSP.inst.getPlayer(m.playerID).toPlayerSP();
		if(p == null) return null;
		
		ArrayList<ITextComponent> info = new ArrayList<>();
		for(int i = 0; i < m.info.length; i++)
		{
			info.add(ITextComponent.Serializer.jsonToComponent(m.info[i]));
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