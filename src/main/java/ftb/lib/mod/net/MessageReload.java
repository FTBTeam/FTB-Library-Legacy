package ftb.lib.mod.net;

import cpw.mods.fml.common.network.simpleimpl.*;
import cpw.mods.fml.relauncher.Side;
import ftb.lib.*;
import ftb.lib.api.*;
import ftb.lib.mod.FTBLibMod;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentTranslation;

public class MessageReload implements IMessage, IMessageHandler<MessageReload, IMessage>
{
	public MessageReload() { }
	
	public void fromBytes(ByteBuf io)
	{
	}
	
	public void toBytes(ByteBuf io)
	{
	}
	
	public IMessage onMessage(MessageReload m, MessageContext ctx)
	{
		FTBWorld.reloadGameModes();
		EntityPlayer ep = FTBLibMod.proxy.getClientPlayer();
		new EventFTBReloadPre(Side.CLIENT, ep).post();
		new EventFTBReload(Side.CLIENT, ep).post();
		FTBLib.printChat(ep, new ChatComponentTranslation("ftbl:reloadedClient"));
		return null;
	}
}