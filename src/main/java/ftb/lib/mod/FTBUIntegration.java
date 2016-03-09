package ftb.lib.mod;

import ftb.lib.api.events.ReloadEvent;
import net.minecraftforge.event.entity.player.*;

public interface FTBUIntegration // FTBLIntegration
{
	void onReloaded(ReloadEvent e);
	void renderWorld(float pt);
	void onTooltip(ItemTooltipEvent e);
	void onRightClick(PlayerInteractEvent e);
}