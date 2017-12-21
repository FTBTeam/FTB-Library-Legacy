package com.feed_the_beast.ftblib.lib.config;

import com.feed_the_beast.ftblib.lib.icon.Color4I;
import com.feed_the_beast.ftblib.lib.io.DataIn;
import com.feed_the_beast.ftblib.lib.io.DataOut;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
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

	@Nullable
	@Override
	public Object getValue()
	{
		return getString();
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
	public boolean setValueFromString(String text, boolean simulate)
	{
		setString(text);
		return true;
	}

	@Override
	public void addInfo(ConfigValueInfo info, List<String> list)
	{
		super.addInfo(info, list);

		if (charLimit > 0)
		{
			list.add(TextFormatting.AQUA + "Char Limit: " + charLimit);
		}
	}

	@Override
	public void fromJson(JsonElement json)
	{
		setString(json.getAsString());
	}

	@Override
	public JsonElement getSerializableElement()
	{
		return new JsonPrimitive(getString());
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

	public boolean isEmpty()
	{
		return getString().isEmpty();
	}
}