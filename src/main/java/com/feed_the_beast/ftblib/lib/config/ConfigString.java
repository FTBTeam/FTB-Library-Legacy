package com.feed_the_beast.ftblib.lib.config;

import com.feed_the_beast.ftblib.lib.icon.Color4I;
import com.feed_the_beast.ftblib.lib.io.DataIn;
import com.feed_the_beast.ftblib.lib.io.DataOut;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

/**
 * @author LatvianModder
 */
public class ConfigString extends ConfigValue
{
	public static final String ID = "string";
	public static final Color4I COLOR = Color4I.rgb(0xFFAA49);

	private String value;
	private int charLimit;

	public ConfigString()
	{
		this("");
	}

	public ConfigString(String v)
	{
		this(v, 0);
	}

	public ConfigString(String v, int limit)
	{
		value = v;
		charLimit = limit;
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
	public ConfigString copy()
	{
		return new ConfigString(getString(), charLimit);
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

		if (charLimit > 0)
		{
			list.add(TextFormatting.AQUA + "Char Limit: " + charLimit);
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
		data.writeShort(charLimit);
	}

	@Override
	public void readData(DataIn data)
	{
		setString(data.readString());
		charLimit = data.readUnsignedShort();
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