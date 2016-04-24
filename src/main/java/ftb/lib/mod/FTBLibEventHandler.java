package ftb.lib.mod;

import com.tamashenning.forgeanalytics.client.ForgeAnalyticsConstants;
import com.tamashenning.forgeanalytics.events.AnalyticsEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import ftb.lib.FTBLib;
import ftb.lib.FTBWorld;
import ftb.lib.ReloadType;
import ftb.lib.api.ServerTickCallback;
import ftb.lib.mod.net.MessageReload;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.WorldEvent;

import java.util.ArrayList;
import java.util.List;

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
			ForgeAnalyticsConstants.CustomProperties.put("FTB_PackMode", w.getMode().getID());
		}
	}
	
	@SubscribeEvent
	public void onWorldLoaded(WorldEvent.Load e)
	{
		if(e.world.provider.dimensionId == 0 && !e.world.isRemote)
		{
			FTBLib.reload(FTBLib.getServer(), ReloadType.SERVER_ONLY, false);
		}
	}
	
	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent e)
	{
		if(e.player instanceof EntityPlayerMP)
		{
			EntityPlayerMP ep = (EntityPlayerMP) e.player;
			new MessageReload(ReloadType.CLIENT_ONLY, ep, true).sendTo(ep);
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