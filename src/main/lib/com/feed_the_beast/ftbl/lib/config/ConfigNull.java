package com.feed_the_beast.ftbl.lib.config;

import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.lib.Color4I;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import io.netty.buffer.ByteBuf;

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
	public void addInfo(ConfigKey key, List<String> list)
	{
	}

	@Override
	public void onClicked(IGuiEditConfig gui, ConfigKey key, IMouseButton button)
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
	public void writeData(ByteBuf data)
	{
	}

	@Override
	public void readData(ByteBuf data)
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