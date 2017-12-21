package com.feed_the_beast.ftblib.lib.config;

import com.feed_the_beast.ftblib.lib.icon.Color4I;
import com.feed_the_beast.ftblib.lib.io.DataIn;
import com.feed_the_beast.ftblib.lib.io.DataOut;
import com.feed_the_beast.ftblib.lib.math.MathUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

/**
 * @author LatvianModder
 */
public class ConfigInt extends ConfigValue implements IntSupplier
{
	public static final String ID = "int";
	public static final Color4I COLOR = Color4I.rgb(0xAA5AE8);

	public static ConfigInt create(int defValue, int min, int max, IntSupplier getter, IntConsumer setter)
	{
		return new ConfigInt(defValue, min, max)
		{
			@Override
			public int getInt()
			{
				return getter.getAsInt();
			}

			@Override
			public void setInt(int v)
			{
				setter.accept(v);
			}
		};
	}

	private int value;
	private int minValue = Integer.MIN_VALUE;
	private int maxValue = Integer.MAX_VALUE;

	public ConfigInt()
	{
	}

	public ConfigInt(int v)
	{
		value = v;
	}

	public ConfigInt(int v, int min, int max)
	{
		this(v);
		minValue = min;
		maxValue = max;
	}

	@Override
	public String getName()
	{
		return ID;
	}

	@Nullable
	@Override
	public Object getValue()
	{
		return getInt();
	}

	public ConfigInt setMin(int v)
	{
		minValue = v;
		return this;
	}

	public ConfigInt setMax(int v)
	{
		maxValue = v;
		return this;
	}

	public int getMin()
	{
		return minValue;
	}

	public int getMax()
	{
		return maxValue;
	}

	public void setInt(int v)
	{
		value = MathHelper.clamp(v, getMin(), getMax());
	}

	@Override
	public String getString()
	{
		return Integer.toString(getInt());
	}

	@Override
	public boolean getBoolean()
	{
		return getInt() != 0;
	}

	@Override
	public int getInt()
	{
		return value;
	}

	@Override
	public ConfigInt copy()
	{
		return new ConfigInt(getInt(), getMin(), getMax());
	}

	@Override
	public boolean equalsValue(ConfigValue value)
	{
		return getInt() == value.getInt();
	}

	@Override
	public Color4I getColor()
	{
		return COLOR;
	}

	@Override
	public void addInfo(ConfigValueInfo info, List<String> list)
	{
		super.addInfo(info, list);

		int m = getMin();

		if (m != Integer.MIN_VALUE)
		{
			list.add(TextFormatting.AQUA + "Min: " + m);
		}

		m = getMax();

		if (m != Integer.MAX_VALUE)
		{
			list.add(TextFormatting.AQUA + "Max: " + m);
		}
	}

	@Override
	public boolean setValueFromString(String text, boolean simulate)
	{
		if (MathUtils.canParseInt(text))
		{
			if (!simulate)
			{
				setInt(Integer.parseInt(text));
			}

			return true;
		}

		return false;
	}

	@Override
	public void fromJson(JsonElement json)
	{
		setInt(json.getAsInt());
	}

	@Override
	public JsonElement getSerializableElement()
	{
		return new JsonPrimitive(getInt());
	}

	@Override
	public void writeData(DataOut data)
	{
		data.writeInt(getInt());
		data.writeInt(getMin());
		data.writeInt(getMax());
	}

	@Override
	public void readData(DataIn data)
	{
		setInt(data.readInt());
		setMin(data.readInt());
		setMax(data.readInt());
	}

	@Override
	public int getAsInt()
	{
		return getInt();
	}
}