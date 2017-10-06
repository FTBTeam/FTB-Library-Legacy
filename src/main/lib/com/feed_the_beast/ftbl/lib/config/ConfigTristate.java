package com.feed_the_beast.ftbl.lib.config;

import com.feed_the_beast.ftbl.lib.io.DataIn;
import com.feed_the_beast.ftbl.lib.io.DataOut;
import com.feed_the_beast.ftbl.lib.util.misc.Color4I;
import com.feed_the_beast.ftbl.lib.util.misc.MouseButton;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author LatvianModder
 */
public class ConfigTristate extends ConfigValue
{
	public static final String ID = "tristate";

	public static ConfigTristate create(EnumTristate defValue, Supplier<EnumTristate> getter, Consumer<EnumTristate> setter)
	{
		return new ConfigTristate(defValue)
		{
			@Override
			public EnumTristate get()
			{
				return getter.get();
			}

			@Override
			public void set(EnumTristate v)
			{
				setter.accept(v);
			}
		};
	}

	private EnumTristate value;

	public ConfigTristate()
	{
		value = EnumTristate.DEFAULT;
	}

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

	@Nullable
	@Override
	public Object getValue()
	{
		return get();
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
	public void onClicked(IGuiEditConfig gui, ConfigValueInfo info, MouseButton button)
	{
		set(EnumTristate.NAME_MAP.getNext(get()));
		gui.onChanged(info.id, getSerializableElement());
	}

	@Override
	public void fromJson(JsonElement json)
	{
		set(json.getAsString().equals("toggle") ? get().getOpposite() : EnumTristate.NAME_MAP.get(json.getAsString()));
	}

	@Override
	public JsonElement getSerializableElement()
	{
		return new JsonPrimitive(get().getName());
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
}