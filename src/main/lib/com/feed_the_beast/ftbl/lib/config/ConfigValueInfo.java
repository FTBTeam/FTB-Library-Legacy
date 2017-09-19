package com.feed_the_beast.ftbl.lib.config;

import com.feed_the_beast.ftbl.api.FTBLibAPI;
import com.feed_the_beast.ftbl.lib.io.Bits;
import com.feed_the_beast.ftbl.lib.io.DataIn;
import com.feed_the_beast.ftbl.lib.io.DataOut;

/**
 * @author LatvianModder
 */
public class ConfigValueInfo
{
	public static final int DISPLAY_NAME = 1;
	public static final int GROUP = 2;
	public static final int EXCLUDED = 4;
	public static final int HIDDEN = 8;
	public static final int CANT_EDIT = 16;
	public static final int USE_SCROLL_BAR = 32;

	public final String id;
	public String group;
	public ConfigValue defaultValue;
	public String displayName;

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

	public ConfigValueInfo(String _id)
	{
		id = _id;
		setDefaults();
	}

	public ConfigValueInfo(String _group, String _id, ConfigValue _defaultValue)
	{
		id = _group.isEmpty() ? _id : (_group + "." + _id);
		setDefaults();
		group = _group;
		defaultValue = _defaultValue.copy();
	}

	public void setDefaults()
	{
		group = "";
		displayName = "";
		defaultValue = ConfigNull.INSTANCE;
		excluded = false;
		hidden = false;
		cantEdit = false;
		useScrollBar = false;
	}

	public void writeData(DataOut net)
	{
		int flags = 0;

		flags = Bits.setFlag(flags, DISPLAY_NAME, !displayName.isEmpty());
		flags = Bits.setFlag(flags, GROUP, !group.isEmpty());
		flags = Bits.setFlag(flags, EXCLUDED, excluded);
		flags = Bits.setFlag(flags, HIDDEN, hidden);
		flags = Bits.setFlag(flags, CANT_EDIT, cantEdit);
		flags = Bits.setFlag(flags, USE_SCROLL_BAR, useScrollBar);

		net.writeByte(flags);

		if (!displayName.isEmpty())
		{
			net.writeString(displayName);
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
			displayName = net.readString();
		}

		if (Bits.getFlag(flags, GROUP))
		{
			group = net.readString();
		}

		defaultValue = FTBLibAPI.API.getConfigValueFromID(net.readString());
		defaultValue.readData(net);
	}

	public ConfigValueInfo copy()
	{
		ConfigValueInfo info = new ConfigValueInfo(id);
		info.defaultValue = defaultValue.copy();
		info.group = group;
		info.displayName = displayName;
		info.excluded = excluded;
		info.hidden = hidden;
		info.cantEdit = cantEdit;
		info.useScrollBar = useScrollBar;
		return info;
	}

	public ConfigValueInfo setNameLangKey(String key)
	{
		displayName = key;
		return this;
	}
}