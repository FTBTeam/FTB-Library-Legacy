package com.feed_the_beast.ftblib.lib.data;

import com.feed_the_beast.ftblib.lib.util.CommonUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author LatvianModder
 */
public class NBTDataStorage implements INBTSerializable<NBTTagCompound>, IHasCache
{
	public static final NBTDataStorage EMPTY = new NBTDataStorage()
	{
		@Override
		@Nullable
		public INBTSerializable<NBTTagCompound> getRaw(String id)
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

	private final Map<String, INBTSerializable<NBTTagCompound>> map;

	public NBTDataStorage()
	{
		map = new HashMap<>();
	}

	public void add(String id, INBTSerializable<NBTTagCompound> data)
	{
		map.put(id, data);
	}

	@Nullable
	public INBTSerializable<NBTTagCompound> getRaw(String id)
	{
		return map.get(id);
	}

	public <T extends INBTSerializable<NBTTagCompound>> T get(String id)
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

		for (Map.Entry<String, INBTSerializable<NBTTagCompound>> entry : map.entrySet())
		{
			NBTTagCompound nbt1 = entry.getValue().serializeNBT();

			if (!nbt1.hasNoTags())
			{
				nbt.setTag(entry.getKey(), nbt1);
			}
		}

		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		CommonUtils.renameTag(nbt, "ftbl:data", "ftblib");
		CommonUtils.renameTag(nbt, "ftbl", "ftblib");
		CommonUtils.renameTag(nbt, "ftbu:data", "ftbutilities");
		CommonUtils.renameTag(nbt, "ftbu", "ftbutilities");

		for (Map.Entry<String, INBTSerializable<NBTTagCompound>> entry : map.entrySet())
		{
			entry.getValue().deserializeNBT(nbt.getCompoundTag(entry.getKey()));
		}
	}

	@Override
	public void clearCache()
	{
		for (Map.Entry<String, INBTSerializable<NBTTagCompound>> entry : map.entrySet())
		{
			if (entry.getValue() instanceof IHasCache)
			{
				((IHasCache) entry.getValue()).clearCache();
			}
		}
	}
}