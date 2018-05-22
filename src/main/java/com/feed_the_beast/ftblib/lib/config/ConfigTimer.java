package com.feed_the_beast.ftblib.lib.config;

import com.feed_the_beast.ftblib.lib.icon.Color4I;
import com.feed_the_beast.ftblib.lib.io.DataIn;
import com.feed_the_beast.ftblib.lib.io.DataOut;
import com.feed_the_beast.ftblib.lib.math.Ticks;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.util.text.TextFormatting;

import java.util.List;
import java.util.function.LongSupplier;

/**
 * @author LatvianModder
 */
public class ConfigTimer extends ConfigValue implements LongSupplier
{
	public static final String ID = "timer";

	private long value;
	private long maxValue = Long.MAX_VALUE;

	public ConfigTimer()
	{
	}

	public ConfigTimer(long v)
	{
		value = v;
	}

	public ConfigTimer(long v, long max)
	{
		this(v);
		maxValue = max;
	}

	@Override
	public String getName()
	{
		return ID;
	}

	@Override
	public Object getValue()
	{
		return getInt();
	}

	public ConfigTimer setMax(long v)
	{
		maxValue = v;
		return this;
	}

	public long getMax()
	{
		return maxValue;
	}

	@Override
	public long getLong()
	{
		return value;
	}

	public void setTimer(long v)
	{
		if (v < 0L)
		{
			value = 0L;
		}
		else
		{
			value = v > getMax() ? getMax() : v;
		}
	}

	@Override
	public String getString()
	{
		return Ticks.toString(getLong());
	}

	@Override
	public boolean getBoolean()
	{
		return getLong() > 0L;
	}

	@Override
	public int getInt()
	{
		return (int) getLong();
	}

	@Override
	public double getDouble()
	{
		return getLong();
	}

	@Override
	public ConfigTimer copy()
	{
		return new ConfigTimer(getInt(), getMax());
	}

	@Override
	public boolean equalsValue(ConfigValue value)
	{
		return value instanceof ConfigTimer && getLong() == value.getLong();
	}

	@Override
	public Color4I getColor()
	{
		return ConfigInt.COLOR;
	}

	@Override
	public void addInfo(ConfigValueInfo info, List<String> list)
	{
		super.addInfo(info, list);

		long m = getMax();

		if (m != Long.MAX_VALUE)
		{
			list.add(TextFormatting.AQUA + "Max: " + Ticks.toString(m));
		}
	}

	@Override
	public boolean setValueFromString(String text, boolean simulate)
	{
		if (text.isEmpty())
		{
			return false;
		}

		try
		{
			value = Ticks.fromString(text);

			if (!simulate)
			{
				setTimer(value);
			}

			return true;
		}
		catch (Exception ex)
		{
			return false;
		}
	}

	@Override
	public void fromJson(JsonElement json)
	{
		if (json.isJsonPrimitive() && json.getAsJsonPrimitive().isNumber())
		{
			setTimer(json.getAsLong());
		}
		else
		{
			setTimer(0L);
			setValueFromString(json.getAsString(), false);
		}
	}

	@Override
	public JsonElement getSerializableElement()
	{
		long timer = getLong();
		return timer == 0L ? new JsonPrimitive(0) : new JsonPrimitive(Ticks.toString(timer));
	}

	@Override
	public void writeData(DataOut data)
	{
		data.writeLong(getLong());
		data.writeLong(getMax());
	}

	@Override
	public void readData(DataIn data)
	{
		setTimer(data.readLong());
		setMax(data.readLong());
	}

	@Override
	public long getAsLong()
	{
		return getLong();
	}
}