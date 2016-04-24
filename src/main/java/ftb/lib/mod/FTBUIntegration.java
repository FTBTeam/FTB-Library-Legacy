package ftb.lib.mod;

import ftb.lib.api.EventFTBReload;
import ftb.lib.api.EventFTBWorldClient;
import ftb.lib.api.EventFTBWorldServer;
import ftb.lib.api.friends.ILMPlayer;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public interface FTBUIntegration // FTBLIntegration
{
	void onReloaded(EventFTBReload e);
	void onFTBWorldServer(EventFTBWorldServer e);
	void onFTBWorldClient(EventFTBWorldClient e);
	void onFTBWorldServerClosed();
	void onServerTick(World w);
	ILMPlayer getLMPlayer(Object player);
	boolean hasClientWorld();
	void renderWorld(float pt);
	void onRightClick(PlayerInteractEvent e);
}