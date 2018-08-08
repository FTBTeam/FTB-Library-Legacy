package com.feed_the_beast.ftblib.lib.config;

import com.feed_the_beast.ftblib.lib.icon.Color4I;
import com.feed_the_beast.ftblib.lib.io.DataIn;
import com.feed_the_beast.ftblib.lib.io.DataOut;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
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
	private Pattern pattern;

	public ConfigString()
	{
		this("");
	}

	public ConfigString(String v)
	{
		this(v, null);
	}

	public ConfigString(String v, @Nullable Pattern p)
	{
		value = v;
		pattern = p;
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

	@Nullable
	public Pattern getPattern()
	{
		return pattern;
	}

	public void setPattern(@Nullable Pattern p)
	{
		pattern = p;
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
	public long getLong()
	{
		return Long.parseLong(getString());
	}

	@Override
	public ConfigString copy()
	{
		return new ConfigString(getString(), getPattern());
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
		if (getPattern() != null && !getPattern().matcher(string).matches())
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

		if (getPattern() != null)
		{
			list.add(TextFormatting.AQUA + "Regex: " + TextFormatting.RESET + getPattern().pattern());
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
		data.writeString(getPattern() == null ? "" : getPattern().pattern());
	}

	@Override
	public void readData(DataIn data)
	{
		setString(data.readString());
		String p = data.readString();
		setPattern(p.isEmpty() ? null : Pattern.compile(p));
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