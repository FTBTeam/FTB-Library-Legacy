package com.feed_the_beast.ftblib.lib.config;

import com.feed_the_beast.ftblib.lib.icon.Color4I;
import com.feed_the_beast.ftblib.lib.io.DataIn;
import com.feed_the_beast.ftblib.lib.io.DataOut;
import com.feed_the_beast.ftblib.lib.util.misc.MouseButton;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

/**
 * @author LatvianModder
 */
public class ConfigBoolean extends ConfigValue implements BooleanSupplier
{
	private static final List<String> VARIANTS = Arrays.asList("true", "false");
	public static final String ID = "bool";
	public static final Color4I COLOR_TRUE = Color4I.rgb(0x33AA33);
	public static final Color4I COLOR_FALSE = Color4I.rgb(0xD52834);

	public static ConfigBoolean create(boolean defValue, BooleanSupplier getter, Consumer<Boolean> setter)
	{
		return new ConfigBoolean(defValue)
		{
			@Override
			public boolean getBoolean()
			{
				return getter.getAsBoolean();
			}

			@Override
			public void setBoolean(boolean v)
			{
				setter.accept(v);
			}
		};
	}

	private boolean value;

	public ConfigBoolean()
	{
	}

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

	@Nullable
	@Override
	public Object getValue()
	{
		return getBoolean();
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
	public void onClicked(IGuiEditConfig gui, ConfigValueInfo info, MouseButton button)
	{
		setBoolean(!getBoolean());
		gui.onChanged(info.id, getSerializableElement());
	}

	@Override
	public void fromJson(JsonElement json)
	{
		if (json.getAsString().equals("toggle"))
		{
			setBoolean(!getBoolean());
		}
		else
		{
			setBoolean(json.getAsBoolean());
		}
	}

	@Override
	public JsonElement getSerializableElement()
	{
		return new JsonPrimitive(getBoolean());
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
}