package com.feed_the_beast.ftblib.lib.config;

import com.feed_the_beast.ftblib.lib.icon.Color4I;
import com.feed_the_beast.ftblib.lib.io.DataIn;
import com.feed_the_beast.ftblib.lib.io.DataOut;
import net.minecraft.command.ICommandSender;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author LatvianModder
 */
public class ConfigTristate extends ConfigValue implements IIteratingConfig
{
	public static final String ID = "tristate";

	private EnumTristate value;

	public ConfigTristate(EnumTristate v)
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
		return value.isTrue();
	}

	public EnumTristate get()
	{
		return value;
	}

	public void set(EnumTristate v)
	{
		value = v;
	}

	@Override
	public String getString()
	{
		return get().getName();
	}

	@Override
	public int getInt()
	{
		return get().ordinal();
	}

	@Override
	public ConfigTristate copy()
	{
		return new ConfigTristate(get());
	}

	@Override
	public Color4I getColor()
	{
		return get().getColor();
	}

	@Override
	public List<String> getVariants()
	{
		return EnumTristate.NAME_MAP.keys;
	}

	@Override
	public void iterate(ConfigValueInstance inst, boolean next)
	{
		if (inst.getCanEdit())
		{
			set(next ? EnumTristate.NAME_MAP.getNext(get()) : EnumTristate.NAME_MAP.getPrevious(get()));
		}
	}

	@Override
	public boolean setValueFromString(@Nullable ICommandSender sender, String string, boolean simulate)
	{
		if (string.equals("toggle"))
		{
			if (!simulate)
			{
				set(get().getOpposite());
			}

			return true;
		}

		EnumTristate tristate = EnumTristate.NAME_MAP.getNullable(string);

		if (tristate != null)
		{
			if (!simulate)
			{
				set(tristate);
			}

			return true;
		}

		return false;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt, String key)
	{
		nbt.setString(key, get().getName());
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt, String key)
	{
		set(EnumTristate.NAME_MAP.get(key));
	}

	@Override
	public void writeData(DataOut data)
	{
		data.writeByte(get().ordinal());
	}

	@Override
	public void readData(DataIn data)
	{
		set(EnumTristate.NAME_MAP.get(data.readUnsignedByte()));
	}

	@Override
	public void setValueFromOtherValue(ConfigValue value)
	{
		if (value instanceof ConfigTristate)
		{
			set(((ConfigTristate) value).get());
		}
		else
		{
			super.setValueFromOtherValue(value);
		}
	}
}