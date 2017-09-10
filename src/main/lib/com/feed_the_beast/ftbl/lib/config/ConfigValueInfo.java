package com.feed_the_beast.ftbl.lib.config;

import com.feed_the_beast.ftbl.api.FTBLibAPI;
import com.feed_the_beast.ftbl.lib.io.Bits;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;

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

	public void writeData(ByteBuf data)
	{
		int flags = 0;

		flags = Bits.setFlag(flags, DISPLAY_NAME, !displayName.isEmpty());
		flags = Bits.setFlag(flags, GROUP, !group.isEmpty());
		flags = Bits.setFlag(flags, EXCLUDED, excluded);
		flags = Bits.setFlag(flags, HIDDEN, hidden);
		flags = Bits.setFlag(flags, CANT_EDIT, cantEdit);
		flags = Bits.setFlag(flags, USE_SCROLL_BAR, useScrollBar);

		data.writeByte(flags);

		if (!displayName.isEmpty())
		{
			ByteBufUtils.writeUTF8String(data, displayName);
		}

		if (!group.isEmpty())
		{
			ByteBufUtils.writeUTF8String(data, group);
		}

		ByteBufUtils.writeUTF8String(data, defaultValue.getName());
		defaultValue.writeData(data);
	}

	public void readData(ByteBuf data)
	{
		setDefaults();
		int flags = data.readUnsignedByte();
		excluded = Bits.getFlag(flags, EXCLUDED);
		hidden = Bits.getFlag(flags, HIDDEN);
		cantEdit = Bits.getFlag(flags, CANT_EDIT);
		useScrollBar = Bits.getFlag(flags, USE_SCROLL_BAR);

		if (Bits.getFlag(flags, DISPLAY_NAME))
		{
			displayName = ByteBufUtils.readUTF8String(data);
		}

		if (Bits.getFlag(flags, GROUP))
		{
			group = ByteBufUtils.readUTF8String(data);
		}

		defaultValue = FTBLibAPI.API.getConfigValueFromID(ByteBufUtils.readUTF8String(data));
		defaultValue.readData(data);
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