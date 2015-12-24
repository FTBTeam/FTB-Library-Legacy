package ftb.lib.mod.net;

import cpw.mods.fml.common.network.simpleimpl.*;
import cpw.mods.fml.relauncher.*;
import ftb.lib.*;
import ftb.lib.api.*;
import ftb.lib.mod.FTBLibMod;
import latmod.lib.ByteCount;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentTranslation;

public class MessageReload extends MessageLM
{
	public MessageReload() { super(ByteCount.INT); }
	
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET; }

	@SideOnly(Side.CLIENT)
	public IMessage onMessage(MessageContext ctx)
	{ reloadClient(); return null; }

	@SideOnly(Side.CLIENT)
	public static void reloadClient()
	{
		FTBWorld.reloadGameModes();
		EntityPlayer ep = FTBLibMod.proxy.getClientPlayer();
		EventFTBReload event = new EventFTBReload(Side.CLIENT, ep, true);
		if(FTBLib.ftbu != null) FTBLib.ftbu.onReloaded(event);
		event.post();
		FTBLib.printChat(ep, new ChatComponentTranslation("ftbl:reloadedClient"));
	}
}