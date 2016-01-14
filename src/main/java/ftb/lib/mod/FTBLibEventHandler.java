package ftb.lib.mod;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import ftb.lib.*;
import ftb.lib.api.ServerTickCallback;
import ftb.lib.mod.net.MessageSendWorldID;
import latmod.lib.util.Phase;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.*;

public class FTBLibEventHandler
{
	public static final List<ServerTickCallback> callbacks = new ArrayList<>();
	public static final List<ServerTickCallback> pendingCallbacks = new ArrayList<>();
	
	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerLoggedInEvent e)
	{
		if(e.player instanceof EntityPlayerMP)
		{
			EntityPlayerMP ep = (EntityPlayerMP) e.player;
			if(FTBLib.ftbu != null) FTBLib.ftbu.onPlayerJoined(ep, Phase.PRE);
			new MessageSendWorldID(FTBWorld.server, ep).sendTo(ep);
			if(FTBLib.ftbu != null) FTBLib.ftbu.onPlayerJoined(ep, Phase.POST);
		}
	}
	
	@SubscribeEvent
	public void onWorldTick(TickEvent.WorldTickEvent e)
	{
		if(!e.world.isRemote && e.side == Side.SERVER && e.phase == TickEvent.Phase.END && e.type == TickEvent.Type.WORLD)
		{
			if(e.world.provider.dimensionId == 0)
			{
				if(!pendingCallbacks.isEmpty())
				{
					callbacks.addAll(pendingCallbacks);
					pendingCallbacks.clear();
				}
				
				if(!callbacks.isEmpty())
				{
					for(int i = callbacks.size() - 1; i >= 0; i--)
						if(callbacks.get(i).incAndCheck()) callbacks.remove(i);
				}
			}
			
			if(FTBLib.ftbu != null) FTBLib.ftbu.onServerTick(e.world);
		}
	}
}