package com.feed_the_beast.ftbl.lib.util.misc;

import com.feed_the_beast.ftbl.lib.util.CommonUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author LatvianModder
 */
public class NBTDataStorage implements INBTSerializable<NBTTagCompound>
{
	public static final NBTDataStorage EMPTY = new NBTDataStorage()
	{
		@Override
		@Nullable
		public INBTSerializable<NBTTagCompound> getRaw(ResourceLocation id)
		{
			return null;
		}

		@Override
		public boolean isEmpty()
		{
			return true;
		}

		@Override
		public NBTTagCompound serializeNBT()
		{
			return new NBTTagCompound();
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt)
		{
		}
	};

	private final Map<ResourceLocation, INBTSerializable<NBTTagCompound>> map;

	public NBTDataStorage()
	{
		map = new HashMap<>();
	}

	public void add(ResourceLocation id, INBTSerializable<NBTTagCompound> data)
	{
		map.put(id, data);
	}

	@Nullable
	public INBTSerializable<NBTTagCompound> getRaw(ResourceLocation id)
	{
		return map.get(id);
	}

	public <T extends INBTSerializable<NBTTagCompound>> T get(ResourceLocation id)
	{
		return CommonUtils.cast(Objects.requireNonNull(getRaw(id)));
	}

	public boolean isEmpty()
	{
		return map.isEmpty();
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();

		for (Map.Entry<ResourceLocation, INBTSerializable<NBTTagCompound>> entry : map.entrySet())
		{
			NBTTagCompound nbt1 = entry.getValue().serializeNBT();

			if (!nbt1.hasNoTags())
			{
				nbt.setTag(entry.getKey().toString(), nbt1);
			}
		}

		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		for (Map.Entry<ResourceLocation, INBTSerializable<NBTTagCompound>> entry : map.entrySet())
		{
			entry.getValue().deserializeNBT(nbt.getCompoundTag(entry.getKey().toString()));
		}
	}
}