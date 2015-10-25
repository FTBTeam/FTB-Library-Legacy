package ftb.lib.mod.net;

import cpw.mods.fml.common.network.simpleimpl.*;
import cpw.mods.fml.relauncher.Side;
import ftb.lib.*;
import ftb.lib.api.*;
import ftb.lib.mod.FTBLibMod;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentTranslation;

public class MessageReload extends MessageLM
{
	public MessageReload() { super(DATA_NONE); }
	
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET; }
	
	public IMessage onMessage(MessageContext ctx)
	{
		FTBWorld.reloadGameModes();
		EntityPlayer ep = FTBLibMod.proxy.getClientPlayer();
		new EventFTBReloadPre(Side.CLIENT, ep).post();
		new EventFTBReload(Side.CLIENT, ep).post();
		FTBLib.printChat(ep, new ChatComponentTranslation("ftbl:reloadedClient"));
		return null;
	}
}