package com.feed_the_beast.ftblib.lib.config;

import com.feed_the_beast.ftblib.lib.icon.Color4I;
import com.feed_the_beast.ftblib.lib.io.DataIn;
import com.feed_the_beast.ftblib.lib.io.DataOut;
import com.feed_the_beast.ftblib.lib.util.misc.MouseButton;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author LatvianModder
 */
public class ConfigNull extends ConfigValue
{
	public static final String ID = "null";
	public static final ConfigNull INSTANCE = new ConfigNull();
	public static final Color4I COLOR = Color4I.rgb(0x333333);

	private ConfigNull()
	{
	}

	@Override
	public String getName()
	{
		return ID;
	}

	@Override
	@Nullable
	public Object getValue()
	{
		return null;
	}

	@Override
	public String getString()
	{
		return "null";
	}

	@Override
	public boolean getBoolean()
	{
		return false;
	}

	@Override
	public int getInt()
	{
		return 0;
	}

	@Override
	public ConfigNull copy()
	{
		return INSTANCE;
	}

	@Override
	public boolean equalsValue(ConfigValue value)
	{
		return value == this;
	}

	@Override
	public Color4I getColor()
	{
		return COLOR;
	}

	@Override
	public void addInfo(ConfigValueInfo info, List<String> list)
	{
	}

	@Override
	public void onClicked(IGuiEditConfig gui, ConfigValueInfo info, MouseButton button)
	{
	}

	@Override
	public void fromJson(JsonElement json)
	{
	}

	@Override
	public JsonElement getSerializableElement()
	{
		return JsonNull.INSTANCE;
	}

	@Override
	public void writeData(DataOut data)
	{
	}

	@Override
	public void readData(DataIn data)
	{
	}

	@Override
	public boolean isNull()
	{
		return true;
	}

	@Override
	public boolean setValueFromString(String text, boolean simulate)
	{
		return false;
	}
}