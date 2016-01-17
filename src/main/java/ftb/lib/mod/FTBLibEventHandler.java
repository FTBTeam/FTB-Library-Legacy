package ftb.lib.mod;

import ftb.lib.*;
import ftb.lib.api.ServerTickCallback;
import ftb.lib.mod.net.MessageSendWorldID;
import latmod.lib.util.Phase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraftforge.fml.relauncher.Side;

import java.util.*;

public class FTBLibEventHandler
{
	public static final List<ServerTickCallback> callbacks = new ArrayList<>();
	public static final List<ServerTickCallback> pendingCallbacks = new ArrayList<>();
	
	@SubscribeEvent
	public void onWorldLoaded(WorldEvent.Load e)
	{
		if(e.world.provider.getDimensionId() == 0 && e.world instanceof WorldServer)
			FTBLib.reload(FTBLib.getServer(), false, false);
	}
	
	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent e)
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
			if(e.world.provider.getDimensionId() == 0)
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
	
	@SubscribeEvent
	public void onRightClick(PlayerInteractEvent e)
	{
		if(FTBLib.ftbu != null) FTBLib.ftbu.onRightClick(e);
	}
}