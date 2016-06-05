package com.feed_the_beast.ftbl;

import com.feed_the_beast.ftbl.api.ForgePlayerMP;
import com.feed_the_beast.ftbl.api.events.ReloadEvent;
import net.minecraft.util.math.BlockPos;

public interface FTBUIntegration // FTBLIntegration
{
    void onReloaded(ReloadEvent e);

    boolean canPlayerInteract(ForgePlayerMP player, boolean leftClick, BlockPos pos);
}