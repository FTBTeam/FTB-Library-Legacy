package com.feed_the_beast.ftblib.lib.config;

import com.feed_the_beast.ftblib.lib.data.FTBLibAPI;
import com.feed_the_beast.ftblib.lib.io.Bits;
import com.feed_the_beast.ftblib.lib.io.DataIn;
import com.feed_the_beast.ftblib.lib.io.DataOut;
import com.feed_the_beast.ftblib.lib.util.misc.Node;
import net.minecraft.util.text.ITextComponent;

/**
 * @author LatvianModder
 */
public class ConfigValueInfo implements Comparable<ConfigValueInfo>
{
	public static final int DISPLAY_NAME = 1;
	public static final int GROUP = 2;
	public static final int EXCLUDED = 4;
	public static final int HIDDEN = 8;
	public static final int CANT_EDIT = 16;
	public static final int USE_SCROLL_BAR = 32;

	public final Node id;
	public String group;
	public ConfigValue defaultValue;
	public ITextComponent displayName;

	/**
	 * Will be excluded from writing / reading from files
	 */
	public boolean excluded;

	/**
	 * Will be hidden from config gui
	 */
	public boolean hidden;

	/**
	 * Will be visible in config gui, but uneditable
	 */
	public boolean cantEdit;

	/**
	 * Use scroll bar on numbers whenever that is available
	 */
	public boolean useScrollBar;

	public ConfigValueInfo(Node _id)
	{
		id = _id;
		setDefaults();
	}

	public ConfigValueInfo(Node id, ConfigValue _defaultValue)
	{
		this(id);
		defaultValue = _defaultValue;
	}

	public ConfigValueInfo(String _group, String _id, ConfigValue _defaultValue)
	{
		this(Node.get(_group.isEmpty() ? _id : (_group + "." + _id)), _defaultValue.copy());
		group = _group;
	}

	public void setDefaults()
	{
		group = "";
		displayName = null;
		defaultValue = ConfigNull.INSTANCE;
		excluded = false;
		hidden = false;
		cantEdit = false;
		useScrollBar = false;
	}

	public void writeData(DataOut net)
	{
		int flags = 0;

		flags = Bits.setFlag(flags, DISPLAY_NAME, displayName != null);
		flags = Bits.setFlag(flags, GROUP, !group.isEmpty());
		flags = Bits.setFlag(flags, EXCLUDED, excluded);
		flags = Bits.setFlag(flags, HIDDEN, hidden);
		flags = Bits.setFlag(flags, CANT_EDIT, cantEdit);
		flags = Bits.setFlag(flags, USE_SCROLL_BAR, useScrollBar);

		net.writeByte(flags);

		if (displayName != null)
		{
			net.writeTextComponent(displayName);
		}

		if (!group.isEmpty())
		{
			net.writeString(group);
		}

		net.writeString(defaultValue.getName());
		defaultValue.writeData(net);
	}

	public void readData(DataIn net)
	{
		setDefaults();
		int flags = net.readUnsignedByte();
		excluded = Bits.getFlag(flags, EXCLUDED);
		hidden = Bits.getFlag(flags, HIDDEN);
		cantEdit = Bits.getFlag(flags, CANT_EDIT);
		useScrollBar = Bits.getFlag(flags, USE_SCROLL_BAR);

		if (Bits.getFlag(flags, DISPLAY_NAME))
		{
			displayName = net.readTextComponent();
		}

		if (Bits.getFlag(flags, GROUP))
		{
			group = net.readString();
		}

		defaultValue = FTBLibAPI.getConfigValueFromId(net.readString());
		defaultValue.readData(net);
	}

	public ConfigValueInfo copy()
	{
		ConfigValueInfo info = new ConfigValueInfo(id);
		info.defaultValue = defaultValue.copy();
		info.group = group;
		info.displayName = displayName == null ? null : displayName.createCopy();
		info.excluded = excluded;
		info.hidden = hidden;
		info.cantEdit = cantEdit;
		info.useScrollBar = useScrollBar;
		return info;
	}

	public ConfigValueInfo setDisplayName(ITextComponent name)
	{
		displayName = name;
		return this;
	}

	public String toString()
	{
		return id.toString();
	}

	public int hashCode()
	{
		return id.hashCode();
	}

	public boolean equals(Object o)
	{
		return o == this || o instanceof ConfigValueInfo && id.equals(((ConfigValueInfo) o).id);
	}

	@Override
	public int compareTo(ConfigValueInfo o)
	{
		return id.compareTo(o.id);
	}
}