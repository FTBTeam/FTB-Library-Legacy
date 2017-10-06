package com.feed_the_beast.ftbl.lib.config;

import com.feed_the_beast.ftbl.lib.io.DataIn;
import com.feed_the_beast.ftbl.lib.io.DataOut;
import com.feed_the_beast.ftbl.lib.util.misc.Color4I;
import com.feed_the_beast.ftbl.lib.util.misc.MutableColor4I;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public class ConfigColor extends ConfigValue
{
	public static final String ID = "color";

	private final MutableColor4I value = Color4I.WHITE.mutable();

	public ConfigColor()
	{
	}

	public ConfigColor(Color4I v)
	{
		value.set(v, 255);
	}

	public ConfigColor(int col)
	{
		value.set(0xFF000000 | col);
	}

	@Override
	public String getName()
	{
		return ID;
	}

	@Override
	public MutableColor4I getColor()
	{
		return value;
	}

	@Nullable
	@Override
	public Object getValue()
	{
		return getColor();
	}

	@Override
	public String getString()
	{
		return getColor().toString();
	}

	@Override
	public boolean getBoolean()
	{
		return getColor().hasColor();
	}

	@Override
	public int getInt()
	{
		return getColor().rgba();
	}

	@Override
	public ConfigColor copy()
	{
		return new ConfigColor(getColor());
	}

	@Override
	public boolean setValueFromString(String text, boolean simulate)
	{
		try
		{
			if (text.indexOf(',') != -1)
			{
				if (text.length() < 5)
				{
					return false;
				}

				String[] s = text.split(",");

				if (s.length == 3 || s.length == 4)
				{
					int c[] = new int[4];
					c[3] = 255;

					for (int i = 0; i < s.length; i++)
					{
						c[i] = Integer.parseInt(s[i]);
					}

					if (!simulate)
					{
						getColor().set(c[0], c[1], c[2], c[3]);
					}

					return true;
				}
			}
			else
			{
				if (text.length() < 6)
				{
					return false;
				}
				else if (text.startsWith("#"))
				{
					text = text.substring(1);
				}

				int hex = Integer.parseInt(text, 16);

				if (!simulate)
				{
					getColor().set(0xFF000000 | hex);
				}

				return true;
			}
		}
		catch (Exception ex)
		{
		}

		return false;
	}

	@Override
	public void fromJson(JsonElement json)
	{
		getColor().set(0xFF000000 | json.getAsInt());
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
	}

	@Override
	public void readData(DataIn data)
	{
		getColor().set(data.readInt());
	}
}