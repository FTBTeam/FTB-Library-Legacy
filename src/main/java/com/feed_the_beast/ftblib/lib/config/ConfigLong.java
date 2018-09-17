package com.feed_the_beast.ftblib.lib.config;

import com.feed_the_beast.ftblib.lib.icon.Color4I;
import com.feed_the_beast.ftblib.lib.io.DataIn;
import com.feed_the_beast.ftblib.lib.io.DataOut;
import com.feed_the_beast.ftblib.lib.util.StringUtils;
import com.google.gson.JsonElement;
import net.minecraft.command.ICommandSender;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.LongConsumer;
import java.util.function.LongSupplier;

/**
 * @author LatvianModder
 */
public class ConfigLong extends ConfigValue implements LongSupplier
{
	public static final String ID = "long";

	public static class SimpleLong extends ConfigLong
	{
		private final LongSupplier get;
		private final LongConsumer set;

		public SimpleLong(long min, long max, LongSupplier g, LongConsumer s)
		{
			super(0L, min, max);
			get = g;
			set = s;
		}

		public SimpleLong(LongSupplier g, LongConsumer s)
		{
			this(Long.MIN_VALUE, Long.MAX_VALUE, g, s);
		}

		@Override
		public long getLong()
		{
			return get.getAsLong();
		}

		@Override
		public void setLong(long v)
		{
			set.accept(v);
		}
	}

	private long value;
	private long min = Long.MIN_VALUE;
	private long max = Long.MAX_VALUE;

	public ConfigLong(long v)
	{
		value = v;
	}

	public ConfigLong(long v, long mn, long mx)
	{
		this(Math.min(Math.max(v, mn), mx));
		min = mn;
		max = mx;
	}

	@Override
	public String getName()
	{
		return ID;
	}

	@Override
	public long getLong()
	{
		return value;
	}

	public void setLong(long v)
	{
		value = v;
	}

	public ConfigLong setMin(long v)
	{
		min = v;
		return this;
	}

	public ConfigLong setMax(long v)
	{
		max = v;
		return this;
	}

	public long getMin()
	{
		return min;
	}

	public long getMax()
	{
		return max;
	}

	@Override
	public ITextComponent getStringForGUI()
	{
		return new TextComponentString(StringUtils.formatDouble(getLong(), true));
	}

	@Override
	public String getString()
	{
		return Long.toString(getLong());
	}

	@Override
	public boolean getBoolean()
	{
		return getLong() != 0D;
	}

	@Override
	public int getInt()
	{
		return (int) getLong();
	}

	@Override
	public ConfigLong copy()
	{
		return new ConfigLong(getLong());
	}

	@Override
	public boolean equalsValue(ConfigValue value)
	{
		return getLong() == value.getLong();
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

		long m = getMin();

		if (m != Long.MIN_VALUE)
		{
			list.add(TextFormatting.AQUA + "Min: " + TextFormatting.RESET + StringUtils.formatDouble(m));
		}

		m = getMax();

		if (m != Long.MAX_VALUE)
		{
			list.add(TextFormatting.AQUA + "Max: " + TextFormatting.RESET + StringUtils.formatDouble(m));
		}
	}

	@Override
	public boolean setValueFromString(@Nullable ICommandSender sender, String string, boolean simulate)
	{
		if (string.isEmpty())
		{
			return false;
		}

		try
		{
			long l = Long.parseLong(string);

			if (!simulate)
			{
				setLong(l);
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
		value = getLong();

		if (value != 0L)
		{
			nbt.setLong(key, value);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt, String key)
	{
		setLong(nbt.getLong(key));
	}

	@Override
	public void writeData(DataOut data)
	{
		data.writeLong(getLong());
		data.writeLong(getMin());
		data.writeLong(getMax());
	}

	@Override
	public void readData(DataIn data)
	{
		setLong(data.readLong());
		setMin(data.readLong());
		setMax(data.readLong());
	}

	@Override
	public long getAsLong()
	{
		return getLong();
	}

	@Override
	public void setValueFromOtherValue(ConfigValue value)
	{
		setLong(value.getLong());
	}

	@Override
	public void setValueFromJson(JsonElement json)
	{
		if (json.isJsonPrimitive())
		{
			setLong(json.getAsLong());
		}
	}
}