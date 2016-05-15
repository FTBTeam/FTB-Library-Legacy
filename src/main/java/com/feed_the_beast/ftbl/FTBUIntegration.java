package com.feed_the_beast.ftbl;

import com.feed_the_beast.ftbl.api.ForgePlayerMP;
import com.feed_the_beast.ftbl.api.events.ReloadEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

public interface FTBUIntegration // FTBLIntegration
{
	void onReloaded(ReloadEvent e);
	void renderWorld(float pt);
	void onTooltip(ItemTooltipEvent e);
	boolean canPlayerInteract(ForgePlayerMP player, BlockPos pos, boolean leftClick);
}