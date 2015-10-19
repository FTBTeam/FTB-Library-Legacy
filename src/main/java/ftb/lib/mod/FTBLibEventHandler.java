package ftb.lib.mod;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.world.WorldEvent;

public class FTBLibEventHandler
{
	@SubscribeEvent
	public void onServerStarted(WorldEvent.Load e)
	{ FTBLib.reloadGameModes(); }
	
	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerLoggedInEvent e)
	{ if(e.player instanceof EntityPlayerMP) FTBLib.playerLoggedIn((EntityPlayerMP)e.player); }
}