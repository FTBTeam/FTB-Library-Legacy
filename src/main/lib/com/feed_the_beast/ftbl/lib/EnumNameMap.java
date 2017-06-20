package com.feed_the_beast.ftbl.lib;

import com.feed_the_beast.ftbl.lib.tile.EnumSaveType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author LatvianModder
 */
public final class EnumNameMap<E extends Enum<E>>
{
	public static final String NULL_VALUE = "-";
	public final int size;
	private final Map<String, E> map;
	private final List<String> keys;
	private final List<E> values;
	private final E defaultValue;

	public EnumNameMap(E[] v, boolean addNull, @Nullable E def)
	{
		List<E> list = new ArrayList<>(v.length + (addNull ? 1 : 0));

		for (E e : v)
		{
			if (e != null)
			{
				list.add(e);
			}
		}

		if (addNull)
		{
			list.add(null);
		}

		values = Collections.unmodifiableList(list);
		size = values.size();

		Map<String, E> map1 = new LinkedHashMap<>(size);

		for (E e : values)
		{
			map1.put(getName(e), e);
		}

		map = Collections.unmodifiableMap(map1);
		keys = Collections.unmodifiableList(new ArrayList<>(map.keySet()));
		defaultValue = def;
	}

	public EnumNameMap(E[] v, boolean addNull)
	{
		this(v, addNull, addNull ? null : v[0]);
	}

	public static String getName(@Nullable Object o)
	{
		if (o == null)
		{
			return NULL_VALUE;
		}
		else if (o instanceof IStringSerializable)
		{
			return ((IStringSerializable) o).getName();
		}
		else
		{
			return ((o instanceof Enum<?>) ? ((Enum<?>) o).name() : o.toString()).toLowerCase();
		}
	}

	@Nullable
	public E get(@Nullable String s)
	{
		if (s == null || s.isEmpty() || s.charAt(0) == '-')
		{
			return defaultValue;
		}
		else
		{
			E e = map.get(s);
			return e == null ? defaultValue : e;
		}
	}

	@Nullable
	public E getFromIndex(int index)
	{
		return index < 0 || index >= values.size() ? defaultValue : values.get(index);
	}

	public int getIndex(@Nullable Object e)
	{
		return values.indexOf(e);
	}

	public int getStringIndex(String s)
	{
		return getIndex(map.get(s));
	}

	public List<String> getKeys()
	{
		return keys;
	}

	public List<E> getValues()
	{
		return values;
	}

	@Nullable
	public E getDefaultValue()
	{
		return defaultValue;
	}

	@Nullable
	public E readFromNBT(NBTTagCompound nbt, String name, EnumSaveType type)
	{
		return (!type.save || nbt.hasKey(name, Constants.NBT.TAG_ANY_NUMERIC)) ? getFromIndex(nbt.getInteger(name)) : get(nbt.getString(name));
	}

	public void writeToNBT(NBTTagCompound nbt, String name, EnumSaveType type, @Nullable Object value)
	{
		if (!type.save)
		{
			if (values.size() >= 128)
			{
				nbt.setShort(name, (short) getIndex(value));
			}
			else
			{
				nbt.setByte(name, (byte) getIndex(value));
			}
		}
		else
		{
			nbt.setString(name, getName(value));
		}
	}
}