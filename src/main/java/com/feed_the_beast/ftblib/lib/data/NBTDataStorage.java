package com.feed_the_beast.ftblib.lib.data;

import com.feed_the_beast.ftblib.lib.util.CommonUtils;
import com.feed_the_beast.ftblib.lib.util.NBTUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IStringSerializable;
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
	public static abstract class Data implements IStringSerializable, INBTSerializable<NBTTagCompound>, IHasCache
	{
		@Override
		public NBTTagCompound serializeNBT()
		{
			return new NBTTagCompound();
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt)
		{
		}

		@Override
		public void clearCache()
		{
		}
	}

	public static final NBTDataStorage EMPTY = new NBTDataStorage()
	{
		@Override
		@Nullable
		public Data getRaw(String id)
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

	private final Map<String, Data> map;

	public NBTDataStorage()
	{
		map = new HashMap<>();
	}

	public void add(Data data)
	{
		map.put(data.getName(), data);
	}

	@Nullable
	public Data getRaw(String id)
	{
		return map.get(id);
	}

	public <T extends Data> T get(String id)
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

		for (Data data : map.values())
		{
			NBTTagCompound nbt1 = data.serializeNBT();

			if (!nbt1.hasNoTags())
			{
				nbt.setTag(data.getName(), nbt1);
			}
		}

		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		NBTUtils.renameTag(nbt, "ftbl:data", "ftblib");
		NBTUtils.renameTag(nbt, "ftbl", "ftblib");
		NBTUtils.renameTag(nbt, "ftbu:data", "ftbutilities");
		NBTUtils.renameTag(nbt, "ftbu", "ftbutilities");

		for (Data data : map.values())
		{
			data.deserializeNBT(nbt.getCompoundTag(data.getName()));
		}
	}

	@Override
	public void clearCache()
	{
		for (Data data : map.values())
		{
			data.clearCache();
		}
	}
}