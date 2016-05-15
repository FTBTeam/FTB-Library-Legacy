package com.feed_the_beast.ftbl.api.tile;

import com.feed_the_beast.ftbl.api.MouseButton;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by LatvianModder on 14.05.2016.
 */
public interface ITileButtonPressed
{
	void handleButton(EntityPlayerMP player, int ID, MouseButton button, NBTTagCompound data);
}