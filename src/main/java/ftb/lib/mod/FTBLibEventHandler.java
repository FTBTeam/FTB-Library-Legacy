package ftb.lib.mod;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import ftb.lib.FTBGameModes;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.world.WorldEvent;

public class FTBLibEventHandler
{
	@SubscribeEvent
	public void onServerStarted(WorldEvent.Load e)
	{ FTBGameModes.reloadGameModes(); }
	
	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerLoggedInEvent e)
	{ if(e.player instanceof EntityPlayerMP) FTBGameModes.playerLoggedIn((EntityPlayerMP)e.player); }
}