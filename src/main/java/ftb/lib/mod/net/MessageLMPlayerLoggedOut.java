package ftb.lib.mod.net;

import ftb.lib.api.ForgePlayerMP;
import ftb.lib.api.ForgePlayerSP;
import ftb.lib.api.ForgeWorldSP;
import ftb.lib.api.net.LMNetworkWrapper;
import ftb.lib.api.net.MessageToClient;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.UUID;

public class MessageLMPlayerLoggedOut extends MessageToClient<MessageLMPlayerLoggedOut>
{
	public UUID playerID;
	
	public MessageLMPlayerLoggedOut() { }
	
	public MessageLMPlayerLoggedOut(ForgePlayerMP p)
	{
		playerID = p.getProfile().getId();
	}
	
	@Override
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET; }
	
	@Override
	public void fromBytes(ByteBuf io)
	{
		playerID = readUUID(io);
	}
	
	@Override
	public void toBytes(ByteBuf io)
	{
		writeUUID(io, playerID);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void onMessage(MessageLMPlayerLoggedOut m, Minecraft mc)
	{
		ForgePlayerSP p = ForgeWorldSP.inst.getPlayer(m.playerID);
		p.onLoggedOut();
		p.isOnline = false;
	}
}