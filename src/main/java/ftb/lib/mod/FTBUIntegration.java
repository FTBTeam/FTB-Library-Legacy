package ftb.lib.mod;

import ftb.lib.api.EventFTBReload;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.*;

public interface FTBUIntegration // FTBLIntegration
{
	void onReloaded(EventFTBReload e);
	void onFTBWorldServer();
	void onFTBWorldClient();
	void onServerTick(World w);
	void renderWorld(float pt);
	void onTooltip(ItemTooltipEvent e);
	void onRightClick(PlayerInteractEvent e);
}