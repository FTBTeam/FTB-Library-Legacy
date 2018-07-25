package com.feed_the_beast.ftblib.lib.config;

import com.feed_the_beast.ftblib.lib.data.FTBLibAPI;
import com.feed_the_beast.ftblib.lib.icon.Color4I;
import com.feed_the_beast.ftblib.lib.io.DataIn;
import com.feed_the_beast.ftblib.lib.io.DataOut;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author LatvianModder
 */
public final class ConfigList<T extends ConfigValue> extends ConfigValue implements Iterable<T>
{
	public static final String ID = "list";
	public static final Color4I COLOR = Color4I.rgb(0xFFAA49);

	private final List<T> list;
	private String valueId;

	public ConfigList(String id)
	{
		list = new ArrayList<>();
		valueId = id;
	}

	public ConfigList(Collection<T> v)
	{
		this(ConfigNull.ID);
		addAll(v);
	}

	@Override
	public String getName()
	{
		return ID;
	}

	public void clear()
	{
		list.clear();
		valueId = ConfigNull.ID;
	}

	private boolean hasValidId()
	{
		return !valueId.equals(ConfigNull.ID);
	}

	public void add(T v)
	{
		if (v.isNull())
		{
			return;
		}

		if (valueId.equals(ConfigNull.ID))
		{
			valueId = v.getName();
			list.add(v);
		}
		else if (v.getName().equals(valueId))
		{
			list.add(v);
		}
	}

	public void addAll(Collection<T> v)
	{
		for (T v1 : v)
		{
			add(v1);
		}
	}

	public void addAll(T... v)
	{
		for (T v1 : v)
		{
			add(v1);
		}
	}

	public List<T> getList()
	{
		return list;
	}

	@Override
	public void writeData(DataOut data)
	{
		data.writeString(valueId);

		if (valueId.equals(ConfigNull.ID))
		{
			return;
		}

		Collection<T> list = getList();
		data.writeShort(list.size());

		for (ConfigValue s : list)
		{
			s.writeData(data);
		}
	}

	@Override
	public void readData(DataIn data)
	{
		clear();
		valueId = data.readString();

		if (valueId.equals(ConfigNull.ID))
		{
			return;
		}

		int s = data.readUnsignedShort();
		ConfigValue blank = FTBLibAPI.getConfigValueFromId(valueId);

		while (--s >= 0)
		{
			ConfigValue v = blank.copy();
			v.readData(data);
			add((T) v);
		}
	}

	@Override
	public String getString()
	{
		StringBuilder builder = new StringBuilder("[");

		for (int i = 0; i < list.size(); i++)
		{
			builder.append(list.get(i).getString());

			if (i != list.size() - 1)
			{
				builder.append(',');
				builder.append(' ');
			}
		}

		builder.append(']');
		return builder.toString();
	}

	@Override
	public boolean getBoolean()
	{
		return !list.isEmpty();
	}

	@Override
	public int getInt()
	{
		return list.size();
	}

	@Override
	public ConfigValue copy()
	{
		return new ConfigList<>(list);
	}

	@Override
	public Color4I getColor()
	{
		return COLOR;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt, String key)
	{
		if (hasValidId() && !list.isEmpty())
		{
			NBTTagList l = new NBTTagList();

			for (T value : list)
			{
				NBTTagCompound nbt1 = new NBTTagCompound();
				value.writeToNBT(nbt1, "value");
				l.appendTag(nbt1);
			}

			nbt.setTag(key, l);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt, String key)
	{
		NBTTagList list = nbt.getTagList(key, Constants.NBT.TAG_COMPOUND);

		if (list.isEmpty())
		{
			return;
		}

		ConfigValue blank = FTBLibAPI.getConfigValueFromId(valueId);

		for (int i = 0; i < list.tagCount(); i++)
		{
			ConfigValue v = blank.copy();
			v.readFromNBT(list.getCompoundTagAt(i), "value");
			add((T) v);
		}
	}

	@Override
	public ITextComponent getStringForGUI()
	{
		return new TextComponentString("...");
	}

	@Override
	public Iterator<T> iterator()
	{
		return list.iterator();
	}

	@Override
	public boolean isEmpty()
	{
		return list.isEmpty();
	}
}