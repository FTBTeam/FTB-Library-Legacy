package com.feed_the_beast.ftbl.lib;

import com.feed_the_beast.ftbl.lib.util.LMUtils;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.HashMap;
import java.util.Map;

/**
 * @author LatvianModder
 */
public class NBTDataStorage implements INBTSerializable<NBTTagCompound>
{
	private final Map<ResourceLocation, INBTSerializable<?>> map;

	public NBTDataStorage()
	{
		map = new HashMap<>();
	}

	public void add(ResourceLocation id, INBTSerializable<?> data)
	{
		map.put(id, data);
	}

	public INBTSerializable<?> get(ResourceLocation id)
	{
		return map.get(id);
	}

	public boolean isEmpty()
	{
		return map.isEmpty();
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();

		map.forEach((key, value) ->
		{
			NBTBase nbt1 = value.serializeNBT();

			if (nbt1 != null && !nbt1.hasNoTags())
			{
				nbt.setTag(key.toString(), nbt1);
			}
		});

		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		map.forEach((key, value) -> value.deserializeNBT(LMUtils.cast(nbt.getTag(key.toString()))));
	}
}