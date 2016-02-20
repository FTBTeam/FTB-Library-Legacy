package ftb.lib.mod;

import com.tamashenning.forgeanalytics.client.ForgeAnalyticsConstants;
import com.tamashenning.forgeanalytics.events.AnalyticsEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.*;
import cpw.mods.fml.relauncher.Side;
import ftb.lib.*;
import ftb.lib.api.ServerTickCallback;
import ftb.lib.mod.net.MessageSendWorldID;
import latmod.lib.util.Phase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.WorldEvent;

import java.util.*;

public class FTBLibEventHandler
{
	public static final List<ServerTickCallback> callbacks = new ArrayList<>();
	public static final List<ServerTickCallback> pendingCallbacks = new ArrayList<>();
	
	@cpw.mods.fml.common.Optional.Method(modid = "forgeanalytics")
	@SubscribeEvent
	public void onAnalytics(AnalyticsEvent event)
	{
		FTBWorld w = FTBWorld.get(event.side);
		if(w != null)
		{
			ForgeAnalyticsConstants.CustomProperties.put("FTB_PackMode", w.getMode().ID);
			FTBLib.dev_logger.info("Sent FTBLib's analytics");
		}
	}
	
	@SubscribeEvent
	public void onWorldLoaded(WorldEvent.Load e)
	{
		if(e.world.provider.dimensionId == 0 && e.world instanceof WorldServer)
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
	
	@SubscribeEvent
	public void onRightClick(PlayerInteractEvent e)
	{
		if(FTBLib.ftbu != null) FTBLib.ftbu.onRightClick(e);
	}
}