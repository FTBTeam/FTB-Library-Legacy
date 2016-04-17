package ftb.lib.mod.net;

import com.mojang.authlib.GameProfile;
import ftb.lib.api.*;
import ftb.lib.api.net.*;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.*;

import java.util.UUID;

public class MessageLMPlayerLoggedIn extends MessageLM<MessageLMPlayerLoggedIn>
{
	public UUID playerID;
	public String playerName;
	public boolean isFirst;
	public NBTTagCompound data;
	
	public MessageLMPlayerLoggedIn() { }
	
	public MessageLMPlayerLoggedIn(ForgePlayerMP p, boolean first, boolean self)
	{
		playerID = p.getProfile().getId();
		playerName = p.getProfile().getName();
		isFirst = first;
		data = new NBTTagCompound();
		p.writeToNet(data, self);
	}
	
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET; }
	
	public void fromBytes(ByteBuf io)
	{
		playerID = readUUID(io);
		playerName = readString(io);
		isFirst = io.readBoolean();
		data = readTag(io);
	}
	
	public void toBytes(ByteBuf io)
	{
		writeUUID(io, playerID);
		writeString(io, playerName);
		io.writeBoolean(isFirst);
		writeTag(io, data);
	}
	
	@SideOnly(Side.CLIENT)
	public IMessage onMessage(MessageLMPlayerLoggedIn m, MessageContext ctx)
	{
		if(ForgeWorldSP.inst == null) return null;
		
		ForgePlayerSP p = ForgeWorldSP.inst.getPlayer(m.playerID);
		if(p == null) p = new ForgePlayerSP(new GameProfile(m.playerID, m.playerName));
		p.init();
		p.readFromNet(m.data, p.isMCPlayer());
		ForgeWorldSP.inst.playerMap.put(p.getProfile().getId(), p);
		p.onLoggedIn(m.isFirst);
		return null;
	}
}