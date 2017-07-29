package com.feed_the_beast.ftbl.lib;

import com.feed_the_beast.ftbl.lib.math.MathUtils;
import com.feed_the_beast.ftbl.lib.tile.EnumSaveType;
import com.feed_the_beast.ftbl.lib.util.StringUtils;
import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author LatvianModder
 */
public final class NameMap<E> implements Iterable<E>
{
	@SafeVarargs
	public static <T> NameMap<T> create(T defaultValue, T... values)
	{
		List<T> list = new ArrayList<>(values.length);

		for (T e : values)
		{
			if (e != null)
			{
				list.add(e);
			}
		}

		Preconditions.checkState(!list.isEmpty());
		return new NameMap<>(defaultValue, list);
	}

	public final E defaultValue;
	public final Map<String, E> map;
	public final List<String> keys;
	public final List<E> values;

	private NameMap(E def, List<E> v)
	{
		values = v;

		Map<String, E> map0 = new LinkedHashMap<>(values.size());

		for (E e : values)
		{
			map0.put(getName(e), e);
		}

		map = Collections.unmodifiableMap(map0);
		keys = Collections.unmodifiableList(new ArrayList<>(map.keySet()));
		defaultValue = get(getName(def));
	}

	private NameMap(E def, NameMap<E> n)
	{
		map = n.map;
		keys = n.keys;
		values = n.values;
		defaultValue = get(getName(def));
	}

	public static String getName(Object value)
	{
		return StringUtils.getId(value, StringUtils.FLAG_ID_ONLY_LOWERCASE | StringUtils.FLAG_ID_FIX);
	}

	public NameMap<E> withDefault(E def)
	{
		if (def == defaultValue)
		{
			return this;
		}

		return new NameMap<>(def, this);
	}

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

	public E get(int index)
	{
		return index < 0 || index >= values.size() ? defaultValue : values.get(index);
	}

	public E offset(E value, int index)
	{
		return get(MathUtils.wrap(getIndex(value) + index, values.size()));
	}

	public E getNext(E value)
	{
		return offset(value, 1);
	}

	public E getPrevious(E value)
	{
		return offset(value, -1);
	}

	public int getIndex(Object e)
	{
		return values.indexOf(e);
	}

	public int getStringIndex(String s)
	{
		return getIndex(map.get(s));
	}

	public void writeToNBT(NBTTagCompound nbt, String name, EnumSaveType type, Object value)
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

	public E readFromNBT(NBTTagCompound nbt, String name, EnumSaveType type)
	{
		return (!type.save || nbt.hasKey(name, Constants.NBT.TAG_ANY_NUMERIC)) ? get(nbt.getInteger(name)) : get(nbt.getString(name));
	}

	public void write(ByteBuf io, E value)
	{
		int index = getIndex(value);

		if (values.size() >= 256)
		{
			io.writeShort(index);
		}
		else
		{
			io.writeByte(index);
		}
	}

	public E read(ByteBuf io)
	{
		return get(values.size() >= 256 ? io.readUnsignedShort() : io.readUnsignedByte());
	}

	public E getRandom(Random rand)
	{
		return values.get(rand.nextInt(values.size()));
	}

	@Override
	public Iterator<E> iterator()
	{
		return values.iterator();
	}
}