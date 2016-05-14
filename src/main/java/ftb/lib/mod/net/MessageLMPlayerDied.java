package ftb.lib.mod.net;

import ftb.lib.api.ForgePlayerSP;
import ftb.lib.api.ForgeWorldSP;
import ftb.lib.api.client.FTBLibClient;
import ftb.lib.api.net.LMNetworkWrapper;
import ftb.lib.api.net.MessageToClient;
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