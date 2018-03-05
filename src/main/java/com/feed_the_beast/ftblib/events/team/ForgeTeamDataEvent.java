package com.feed_the_beast.ftblib.events.team;

import com.feed_the_beast.ftblib.lib.data.ForgeTeam;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.function.BiConsumer;

/**
 * @author LatvianModder
 */
public class ForgeTeamDataEvent extends ForgeTeamEvent
{
	private final BiConsumer<String, INBTSerializable<NBTTagCompound>> callback;

	public ForgeTeamDataEvent(ForgeTeam team, BiConsumer<String, INBTSerializable<NBTTagCompound>> c)
	{
		super(team);
		callback = c;
	}

	public void register(String id, INBTSerializable<NBTTagCompound> data)
	{
		callback.accept(id, data);
	}
}