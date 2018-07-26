package com.feed_the_beast.ftblib.lib.config;

import com.feed_the_beast.ftblib.lib.icon.Color4I;
import com.feed_the_beast.ftblib.lib.io.DataIn;
import com.feed_the_beast.ftblib.lib.io.DataOut;
import com.feed_the_beast.ftblib.lib.math.Ticks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;

import java.util.List;
import java.util.function.LongSupplier;

/**
 * @author LatvianModder
 */
public class ConfigTimer extends ConfigValue implements LongSupplier
{
	public static final String ID = "timer";

	private Ticks value;
	private Ticks maxValue = Ticks.NO_TICKS;

	public ConfigTimer(Ticks v)
	{
		value = v;
	}

	public ConfigTimer(Ticks v, Ticks max)
	{
		this(v);
		maxValue = max;
	}

	@Override
	public String getName()
	{
		return ID;
	}

	public ConfigTimer setMax(Ticks v)
	{
		maxValue = v;
		return this;
	}

	public Ticks getMax()
	{
		return maxValue;
	}

	@Override
	public Ticks getTimer()
	{
		return value;
	}

	public void setTimer(Ticks v)
	{
		Ticks max = getMax();
		value = max.hasTicks() && v.ticks() >= max.ticks() ? max : v;
	}

	@Override
	public String getString()
	{
		return getTimer().toString();
	}

	@Override
	public boolean getBoolean()
	{
		return getTimer().hasTicks();
	}

	@Override
	public int getInt()
	{
		return (int) getTimer().ticks();
	}

	@Override
	public double getDouble()
	{
		return getTimer().ticks();
	}

	@Override
	public ConfigTimer copy()
	{
		return new ConfigTimer(getTimer(), getMax());
	}

	@Override
	public boolean equalsValue(ConfigValue value)
	{
		return value instanceof ConfigTimer && getTimer().equalsTimer(value.getTimer());
	}

	@Override
	public Color4I getColor()
	{
		return ConfigInt.COLOR;
	}

	@Override
	public void addInfo(ConfigValueInstance inst, List<String> list)
	{
		super.addInfo(inst, list);

		Ticks max = getMax();

		if (max.hasTicks())
		{
			list.add(TextFormatting.AQUA + "Max: " + TextFormatting.RESET + max);
		}
	}

	@Override
	public boolean setValueFromString(String string, boolean simulate)
	{
		if (string.isEmpty())
		{
			return false;
		}

		try
		{
			value = Ticks.get(string);

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
	public void writeToNBT(NBTTagCompound nbt, String key)
	{
		nbt.setString(key, getTimer().toString());
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt, String key)
	{
		setTimer(Ticks.NO_TICKS);
		setValueFromString(nbt.getString(key), false);
	}

	@Override
	public void writeData(DataOut data)
	{
		data.writeLong(getTimer().ticks());
		data.writeLong(getMax().ticks());
	}

	@Override
	public void readData(DataIn data)
	{
		setTimer(Ticks.get(data.readLong()));
		setMax(Ticks.get(data.readLong()));
	}

	@Override
	public long getAsLong()
	{
		return getTimer().ticks();
	}

	@Override
	public void setValueFromOtherValue(ConfigValue value)
	{
		setTimer(value.getTimer());
	}
}