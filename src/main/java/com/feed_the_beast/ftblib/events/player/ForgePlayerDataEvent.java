package com.feed_the_beast.ftblib.events.player;

import com.feed_the_beast.ftblib.lib.data.ForgePlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.function.BiConsumer;

/**
 * @author LatvianModder
 */
public class ForgePlayerDataEvent extends ForgePlayerEvent
{
	private final BiConsumer<String, INBTSerializable<NBTTagCompound>> callback;

	public ForgePlayerDataEvent(ForgePlayer player, BiConsumer<String, INBTSerializable<NBTTagCompound>> c)
	{
		super(player);
		callback = c;
	}

	public void register(String id, INBTSerializable<NBTTagCompound> data)
	{
		callback.accept(id, data);
	}
}