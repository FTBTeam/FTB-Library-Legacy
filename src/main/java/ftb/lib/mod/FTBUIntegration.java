package ftb.lib.mod;

import ftb.lib.api.EventFTBReload;
import net.minecraftforge.event.entity.player.*;

public interface FTBUIntegration // FTBLIntegration
{
	void onReloaded(EventFTBReload e);
	void renderWorld(float pt);
	void onTooltip(ItemTooltipEvent e);
	void onRightClick(PlayerInteractEvent e);
}