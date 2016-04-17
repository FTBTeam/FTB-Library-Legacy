package ftb.lib.mod;

import ftb.lib.api.events.ReloadEvent;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

public interface FTBUIntegration // FTBLIntegration
{
	void onReloaded(ReloadEvent e);
	void renderWorld(float pt);
	void onTooltip(ItemTooltipEvent e);
	boolean canPlayerInteract(EntityPlayerMP ep, BlockPos pos, boolean leftClick);
}