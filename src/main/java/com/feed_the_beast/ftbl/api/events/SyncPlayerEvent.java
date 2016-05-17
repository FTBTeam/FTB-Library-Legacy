package com.feed_the_beast.ftbl.api.events;

import com.feed_the_beast.ftbl.api.ForgePlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Created by LatvianModder on 18.05.2016.
 */
public class SyncPlayerEvent extends Event
{
	public final ForgePlayer player;
	public final boolean self;
	public final NBTTagCompound data;
	
	public SyncPlayerEvent(ForgePlayer p, boolean b, NBTTagCompound d)
	{
		player = p;
		self = b;
		data = d;
	}
}