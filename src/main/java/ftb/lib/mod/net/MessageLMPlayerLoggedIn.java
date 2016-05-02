package ftb.lib.mod.net;

import com.mojang.authlib.GameProfile;
import ftb.lib.api.ForgePlayerMP;
import ftb.lib.api.ForgePlayerSP;
import ftb.lib.api.ForgeWorldSP;
import ftb.lib.api.net.LMNetworkWrapper;
import ftb.lib.api.net.MessageLM;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
	
	@Override
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET; }
	
	@Override
	public void fromBytes(ByteBuf io)
	{
		playerID = readUUID(io);
		playerName = readString(io);
		isFirst = io.readBoolean();
		data = readTag(io);
	}
	
	@Override
	public void toBytes(ByteBuf io)
	{
		writeUUID(io, playerID);
		writeString(io, playerName);
		io.writeBoolean(isFirst);
		writeTag(io, data);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IMessage onMessage(MessageLMPlayerLoggedIn m, MessageContext ctx)
	{
		if(ForgeWorldSP.inst == null) { return null; }
		
		ForgePlayerSP p = ForgeWorldSP.inst.getPlayer(m.playerID);
		if(p == null) { p = new ForgePlayerSP(new GameProfile(m.playerID, m.playerName)); }
		p.init();
		p.readFromNet(m.data, p.isMCPlayer());
		ForgeWorldSP.inst.playerMap.put(p.getProfile().getId(), p);
		p.onLoggedIn(m.isFirst);
		return null;
	}
}