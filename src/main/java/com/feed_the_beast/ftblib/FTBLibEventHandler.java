package com.feed_the_beast.ftblib;

import com.feed_the_beast.ftblib.lib.data.Universe;
import com.feed_the_beast.ftblib.lib.util.ServerUtils;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

/**
 * @author LatvianModder
 */
@Mod.EventBusSubscriber(modid = FTBLib.MOD_ID)
public class FTBLibEventHandler
{
	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event)
	{
		if (event.player.ticksExisted % 5 == 2 && event.player instanceof EntityPlayerMP)
		{
			byte opState = event.player.getEntityData().getByte("FTBLibOP");
			byte newOpState = ServerUtils.isOP((EntityPlayerMP) event.player) ? (byte) 2 : (byte) 1;

			if (opState != newOpState)
			{
				event.player.getEntityData().setByte("FTBLibOP", newOpState);
				Universe.get().clearCache();
			}
		}
	}
}