package com.feed_the_beast.ftblib.lib.config;

import com.feed_the_beast.ftblib.lib.data.FTBLibAPI;
import com.feed_the_beast.ftblib.lib.io.Bits;
import com.feed_the_beast.ftblib.lib.io.DataIn;
import com.feed_the_beast.ftblib.lib.io.DataOut;
import com.feed_the_beast.ftblib.lib.util.FinalIDObject;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public final class ConfigValueInstance extends FinalIDObject
{
	private static final int HAS_NAME = 1;
	private static final int HIDDEN = 2;
	private static final int CANT_EDIT = 3;
	private static final int USE_SCROLL_BAR = 4;
	private static final int EXCLUDED = 5;
	private static final int HAS_INFO = 6;

	private final ConfigGroup group;
	private final ConfigValue value;
	private ConfigValue defaultValue;
	private int flags;
	private ITextComponent displayName, info;
	private byte order;

	public ConfigValueInstance(String id, ConfigGroup g, ConfigValue v)
	{
		super(id);
		group = g;
		value = v;
		defaultValue = value.copy();
		flags = 0;
		displayName = null;
		info = null;
		order = 0;
	}

	public ConfigValueInstance(ConfigGroup g, DataIn data)
	{
		super(data.readString());
		group = g;
		value = FTBLibAPI.getConfigValueFromId(data.readString());
		value.readData(data);
		defaultValue = FTBLibAPI.getConfigValueFromId(data.readString());
		defaultValue.readData(data);
		flags = data.readUnsignedShort();
		order = data.readByte();
		displayName = Bits.getFlag(flags, HAS_NAME) ? data.readTextComponent() : null;
		info = Bits.getFlag(flags, HAS_INFO) ? data.readTextComponent() : null;
	}

	public ConfigGroup getGroup()
	{
		return group;
	}

	public ConfigValue getValue()
	{
		return value;
	}

	public ConfigValueInstance setDefaultValue(ConfigValue def)
	{
		defaultValue = value.copy();
		defaultValue.setValueFromOtherValue(def);
		return this;
	}

	public ConfigValue getDefaultValue()
	{
		return defaultValue;
	}

	public ConfigValueInstance setDisplayName(@Nullable ITextComponent name)
	{
		displayName = name;
		flags = Bits.setFlag(flags, HAS_NAME, displayName != null);
		return this;
	}

	public ITextComponent getDisplayName()
	{
		return displayName == null ? group.getDisplayNameOf(this) : displayName;
	}

	public ConfigValueInstance setInfo(@Nullable ITextComponent component)
	{
		info = component;
		flags = Bits.setFlag(flags, HAS_INFO, info != null);
		return this;
	}

	public ITextComponent getInfo()
	{
		return info == null ? group.getInfoOf(this) : info;
	}

	public ConfigValueInstance setHidden(boolean v)
	{
		flags = Bits.setFlag(flags, HIDDEN, v);
		return this;
	}

	public boolean getHidden()
	{
		return Bits.getFlag(flags, HIDDEN);
	}

	public ConfigValueInstance setCanEdit(boolean v)
	{
		flags = Bits.setFlag(flags, CANT_EDIT, !v);
		return this;
	}

	public boolean getCanEdit()
	{
		return !Bits.getFlag(flags, CANT_EDIT);
	}

	public ConfigValueInstance setUseScrollBar(boolean v)
	{
		flags = Bits.setFlag(flags, USE_SCROLL_BAR, v);
		return this;
	}

	public boolean getUseScrollBar()
	{
		return Bits.getFlag(flags, USE_SCROLL_BAR);
	}

	public ConfigValueInstance setExcluded(boolean v)
	{
		flags = Bits.setFlag(flags, EXCLUDED, v);
		return this;
	}

	public boolean getExcluded()
	{
		return Bits.getFlag(flags, EXCLUDED);
	}

	public ConfigValueInstance setOrder(byte o)
	{
		order = o;
		return this;
	}

	public byte getOrder()
	{
		return order;
	}

	public void writeData(DataOut data)
	{
		data.writeString(value.getName());
		value.writeData(data);
		data.writeString(defaultValue.getName());
		defaultValue.writeData(data);
		data.writeShort(flags);
		data.writeByte(order);

		if (displayName != null)
		{
			data.writeTextComponent(displayName);
		}

		if (info != null)
		{
			data.writeTextComponent(info);
		}
	}

	public ConfigValueInstance copy(ConfigGroup g)
	{
		ConfigValueInstance inst = new ConfigValueInstance(getName(), g, value.copy());
		inst.defaultValue = defaultValue.copy();
		inst.displayName = displayName == null ? null : displayName.createCopy();
		inst.info = info == null ? null : info.createCopy();
		inst.flags = flags;
		inst.order = order;
		return inst;
	}

	public String getPath()
	{
		return group.getPath() + "." + getName();
	}
}