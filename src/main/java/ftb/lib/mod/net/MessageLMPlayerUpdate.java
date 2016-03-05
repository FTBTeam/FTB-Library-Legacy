package ftb.lib.mod.net;

import ftb.lib.api.client.FTBLibClient;
import ftb.lib.api.net.*;
import ftb.lib.api.players.*;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.*;

import java.util.UUID;

public class MessageLMPlayerUpdate extends MessageLM<MessageLMPlayerUpdate>
{
	public UUID playerID;
	public boolean isSelf;
	public NBTTagCompound data;
	
	public MessageLMPlayerUpdate() { }
	
	public MessageLMPlayerUpdate(ForgePlayerMP p, boolean self)
	{
		this();
		playerID = p.getProfile().getId();
		isSelf = self;
		data = new NBTTagCompound();
		p.writeToNet(data, self);
	}
	
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET; }
	
	public void fromBytes(ByteBuf io)
	{
		playerID = readUUID(io);
		isSelf = io.readBoolean();
		data = ByteBufUtils.readTag(io);
	}
	
	public void toBytes(ByteBuf io)
	{
		writeUUID(io, playerID);
		io.writeBoolean(isSelf);
		writeTag(io, data);
	}
	
	@SideOnly(Side.CLIENT)
	public IMessage onMessage(MessageLMPlayerUpdate m, MessageContext ctx)
	{
		ForgePlayerSP p = ForgeWorldSP.inst.getPlayer(m.playerID).toPlayerSP();
		p.readFromNet(m.data, m.isSelf);
		FTBLibClient.onGuiClientAction();
		return null;
	}
}