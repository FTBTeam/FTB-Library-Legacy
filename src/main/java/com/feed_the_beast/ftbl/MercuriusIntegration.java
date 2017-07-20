package com.feed_the_beast.ftbl;

import com.feed_the_beast.ftbl.api.EventHandler;
import com.feed_the_beast.ftbl.api_impl.SharedServerData;
import com.feed_the_beast.ftbl.lib.internal.FTBLibFinals;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * @author LatvianModder
 */
@EventHandler(requiredMods = "mercurius")
public class MercuriusIntegration
{
	@SubscribeEvent
	public static void onAnalytics(net.minecraftforge.mercurius.binding.StatsCollectionEvent event)
	{
		Map<String, Object> map = new HashMap<>();
		map.put("FTB_PackMode", SharedServerData.INSTANCE.getPackMode().getName());
		event.addEventData(FTBLibFinals.MOD_ID, map);
	}
}