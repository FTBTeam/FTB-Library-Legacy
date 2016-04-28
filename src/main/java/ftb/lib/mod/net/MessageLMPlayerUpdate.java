package ftb.lib.mod.net;

import ftb.lib.api.ForgePlayerMP;
import ftb.lib.api.ForgePlayerSP;
import ftb.lib.api.ForgeWorldSP;
import ftb.lib.api.client.FTBLibClient;
import ftb.lib.api.net.LMNetworkWrapper;
import ftb.lib.api.net.MessageLM;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
	
	@Override
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET; }
	
	@Override
	public void fromBytes(ByteBuf io)
	{
		playerID = readUUID(io);
		isSelf = io.readBoolean();
		data = ByteBufUtils.readTag(io);
	}
	
	@Override
	public void toBytes(ByteBuf io)
	{
		writeUUID(io, playerID);
		io.writeBoolean(isSelf);
		writeTag(io, data);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IMessage onMessage(MessageLMPlayerUpdate m, MessageContext ctx)
	{
		ForgePlayerSP p = ForgeWorldSP.inst.getPlayer(m.playerID).toPlayerSP();
		p.readFromNet(m.data, m.isSelf);
		FTBLibClient.onGuiClientAction();
		return null;
	}
}