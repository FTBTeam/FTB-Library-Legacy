package com.feed_the_beast.ftblib.lib.config;

import com.feed_the_beast.ftblib.lib.icon.Color4I;
import com.feed_the_beast.ftblib.lib.io.DataIn;
import com.feed_the_beast.ftblib.lib.io.DataOut;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import java.util.List;
import java.util.regex.Pattern;

/**
 * @author LatvianModder
 */
public class ConfigString extends ConfigValue
{
	public static final String ID = "string";
	public static final Color4I COLOR = Color4I.rgb(0xFFAA49);

	private String value;
	private String regex;

	public ConfigString()
	{
		this("");
	}

	public ConfigString(String v)
	{
		this(v, "");
	}

	public ConfigString(String v, String r)
	{
		value = v;
		regex = r;
	}

	@Override
	public String getName()
	{
		return ID;
	}

	@Override
	public String getString()
	{
		return value;
	}

	public void setString(String v)
	{
		value = v;
	}

	public String getRegex()
	{
		return regex;
	}

	public void setRegex(String v)
	{
		regex = v;
	}

	@Override
	public boolean getBoolean()
	{
		return getString().equals("true");
	}

	@Override
	public int getInt()
	{
		return Integer.parseInt(getString());
	}

	@Override
	public double getDouble()
	{
		return Double.parseDouble(getString());
	}

	@Override
	public ConfigString copy()
	{
		return new ConfigString(getString(), getRegex());
	}

	@Override
	public Color4I getColor()
	{
		return COLOR;
	}

	@Override
	public ITextComponent getStringForGUI()
	{
		return new TextComponentString('"' + getString() + '"');
	}

	@Override
	public boolean setValueFromString(String string, boolean simulate)
	{
		if (!getRegex().isEmpty() && !Pattern.compile(getRegex()).matcher(string).matches())
		{
			return false;
		}

		if (!simulate)
		{
			setString(string);
		}

		return true;
	}

	@Override
	public void addInfo(ConfigValueInstance inst, List<String> list)
	{
		super.addInfo(inst, list);

		if (!getRegex().isEmpty())
		{
			list.add(TextFormatting.AQUA + "Regex: " + TextFormatting.RESET + getRegex());
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt, String key)
	{
		value = getString();

		if (!value.isEmpty())
		{
			nbt.setString(key, value);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt, String key)
	{
		setString(nbt.getString(key));
	}

	@Override
	public void writeData(DataOut data)
	{
		data.writeString(getString());
		data.writeString(getRegex());
	}

	@Override
	public void readData(DataIn data)
	{
		setString(data.readString());
		setRegex(data.readString());
	}

	@Override
	public boolean isEmpty()
	{
		return getString().isEmpty();
	}

	@Override
	public void setValueFromOtherValue(ConfigValue value)
	{
		setString(value.getString());
	}
}