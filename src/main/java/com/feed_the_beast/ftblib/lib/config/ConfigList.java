package com.feed_the_beast.ftblib.lib.config;

import com.feed_the_beast.ftblib.lib.data.FTBLibAPI;
import com.feed_the_beast.ftblib.lib.icon.Color4I;
import com.feed_the_beast.ftblib.lib.io.DataIn;
import com.feed_the_beast.ftblib.lib.io.DataOut;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author LatvianModder
 */
public final class ConfigList extends ConfigValue implements Iterable<ConfigValue>
{
	public static final String ID = "list";
	public static final Color4I COLOR = Color4I.rgb(0xFFAA49);

	private final List<ConfigValue> list;
	private String valueId;

	public ConfigList(String id)
	{
		list = new ArrayList<>();
		valueId = id;
	}

	public ConfigList(Collection<ConfigValue> v)
	{
		this(ConfigNull.ID);
		addAll(v);
	}

	public ConfigList(ConfigValue v0, ConfigValue... v)
	{
		this(ConfigNull.ID);
		add(v0);
		addAll(v);
	}

	@Override
	public String getName()
	{
		return ID;
	}

	@Override
	public Object getValue()
	{
		return getList();
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

	public void add(ConfigValue v)
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

	public void addAll(Collection<ConfigValue> v)
	{
		for (ConfigValue v1 : v)
		{
			add(v1);
		}
	}

	public void addAll(ConfigValue... v)
	{
		for (ConfigValue v1 : v)
		{
			add(v1);
		}
	}

	public Collection<ConfigValue> getList()
	{
		return list;
	}

	public boolean containsValue(@Nullable Object val)
	{
		if (list.isEmpty())
		{
			return false;
		}

		for (ConfigValue value : list)
		{
			if (value.getValue() == val)
			{
				return true;
			}
		}

		if (val == null)
		{
			return false;
		}

		for (ConfigValue value : list)
		{
			if (val.equals(value.getValue()))
			{
				return true;
			}
		}

		return false;
	}

	@Override
	public void writeData(DataOut data)
	{
		data.writeString(valueId);

		if (valueId.equals(ConfigNull.ID))
		{
			return;
		}

		Collection<ConfigValue> list = getList();
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
			add(v);
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
		return new ConfigList(list);
	}

	@Override
	public Color4I getColor()
	{
		return COLOR;
	}

	@Override
	public void fromJson(JsonElement json)
	{
		if (!hasValidId())
		{
			return;
		}

		list.clear();
		JsonArray a = json.getAsJsonArray();

		if (a.size() == 0)
		{
			return;
		}

		ConfigValue blank = FTBLibAPI.getConfigValueFromId(valueId);

		for (JsonElement e : a)
		{
			ConfigValue v = blank.copy();
			v.fromJson(e);
			add(v);
		}
	}

	@Override
	public JsonElement getSerializableElement()
	{
		JsonArray a = new JsonArray();

		if (hasValidId())
		{
			list.forEach(v -> a.add(v.getSerializableElement()));
		}

		return a;
	}

	@Override
	public Iterator<ConfigValue> iterator()
	{
		return list.iterator();
	}
}