package com.feed_the_beast.ftblib.lib.config;

import com.feed_the_beast.ftblib.lib.icon.Color4I;
import com.feed_the_beast.ftblib.lib.io.DataIn;
import com.feed_the_beast.ftblib.lib.io.DataOut;
import com.feed_the_beast.ftblib.lib.util.misc.BooleanConsumer;
import com.google.gson.JsonElement;
import net.minecraft.command.ICommandSender;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.function.BooleanSupplier;

/**
 * @author LatvianModder
 */
public class ConfigBoolean extends ConfigValue implements BooleanSupplier, IIteratingConfig
{
	public static final List<String> VARIANTS = Arrays.asList("true", "false");
	public static final String ID = "bool";
	public static final Color4I COLOR_TRUE = Color4I.rgb(0x33AA33);
	public static final Color4I COLOR_FALSE = Color4I.rgb(0xD52834);

	public static class SimpleBoolean extends ConfigBoolean
	{
		private final BooleanSupplier get;
		private final BooleanConsumer set;

		public SimpleBoolean(BooleanSupplier g, BooleanConsumer s)
		{
			super(false);
			get = g;
			set = s;
		}

		@Override
		public boolean getBoolean()
		{
			return get.getAsBoolean();
		}

		@Override
		public void setBoolean(boolean v)
		{
			set.accept(v);
		}
	}

	private boolean value;

	public ConfigBoolean(boolean v)
	{
		value = v;
	}

	@Override
	public String getID()
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

	@Override
	public String getString()
	{
		return getBoolean() ? "true" : "false";
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
	public void iterate(ConfigValueInstance inst, boolean next)
	{
		if (inst.getCanEdit())
		{
			setBoolean(!getBoolean());
		}
	}

	@Override
	public boolean setValueFromString(@Nullable ICommandSender sender, String string, boolean simulate)
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

	@Override
	public void setValueFromJson(JsonElement json)
	{
		setBoolean(json.getAsBoolean());
	}
}