package com.feed_the_beast.ftblib.lib.config;

import com.feed_the_beast.ftblib.lib.gui.IOpenableGui;
import com.feed_the_beast.ftblib.lib.icon.Color4I;
import com.feed_the_beast.ftblib.lib.io.DataIn;
import com.feed_the_beast.ftblib.lib.io.DataOut;
import com.feed_the_beast.ftblib.lib.util.misc.MouseButton;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Arrays;
import java.util.List;
import java.util.function.BooleanSupplier;

/**
 * @author LatvianModder
 */
public class ConfigBoolean extends ConfigValue implements BooleanSupplier
{
	public static final List<String> VARIANTS = Arrays.asList("true", "false");
	public static final String ID = "bool";
	public static final Color4I COLOR_TRUE = Color4I.rgb(0x33AA33);
	public static final Color4I COLOR_FALSE = Color4I.rgb(0xD52834);

	private boolean value;

	public ConfigBoolean(boolean v)
	{
		value = v;
	}

	@Override
	public String getName()
	{
		return ID;
	}

	@Override
	public boolean getBoolean()
	{
		return value;
	}

	public void setBoolean(boolean v)
	{
		value = v;
	}

	public final boolean toggle()
	{
		boolean value = !getBoolean();
		setBoolean(value);
		return value;
	}

	@Override
	public String getString()
	{
		return value ? "true" : "false";
	}

	@Override
	public int getInt()
	{
		return getBoolean() ? 1 : 0;
	}

	@Override
	public ConfigBoolean copy()
	{
		return new ConfigBoolean(getBoolean());
	}

	@Override
	public boolean equalsValue(ConfigValue value)
	{
		return getBoolean() == value.getBoolean();
	}

	@Override
	public Color4I getColor()
	{
		return getBoolean() ? COLOR_TRUE : COLOR_FALSE;
	}

	@Override
	public List<String> getVariants()
	{
		return VARIANTS;
	}

	@Override
	public void onClicked(IOpenableGui gui, ConfigValueInstance inst, MouseButton button)
	{
		setBoolean(!getBoolean());
	}

	@Override
	public boolean setValueFromString(String string, boolean simulate)
	{
		if (simulate)
		{
			return string.equals("true") || string.equals("false") || string.equals("toggle");
		}

		if (string.equals("toggle"))
		{
			setBoolean(!getBoolean());
		}
		else
		{
			setBoolean(string.equals("true"));
		}

		return true;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt, String key)
	{
		if (getBoolean())
		{
			nbt.setBoolean(key, true);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt, String key)
	{
		setBoolean(nbt.getBoolean(key));
	}

	@Override
	public void writeData(DataOut data)
	{
		data.writeBoolean(getBoolean());
	}

	@Override
	public void readData(DataIn data)
	{
		setBoolean(data.readBoolean());
	}

	@Override
	public boolean getAsBoolean()
	{
		return getBoolean();
	}

	@Override
	public void setValueFromOtherValue(ConfigValue value)
	{
		setBoolean(value.getBoolean());
	}
}