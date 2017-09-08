package com.feed_the_beast.ftbl.lib.config;

import com.feed_the_beast.ftbl.api.FTBLibAPI;
import com.feed_the_beast.ftbl.lib.io.Bits;
import com.feed_the_beast.ftbl.lib.io.IExtendedIOObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.IJsonSerializable;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author LatvianModder
 */
public final class ConfigTree implements IExtendedIOObject, IJsonSerializable
{
	private static final int HAS_DISPLAY_NAME = 1;
	private static final int HAS_GROUP = 2;

	private final Map<ConfigKey, ConfigValue> tree;

	public ConfigTree(boolean linked)
	{
		tree = linked ? new LinkedHashMap<>() : new HashMap<>();
	}

	public ConfigTree()
	{
		this(false);
	}

	public void add(ConfigKey key, ConfigValue value)
	{
		tree.put(key, value);
	}

	public boolean has(ConfigKey key)
	{
		return tree.containsKey(key);
	}

	public void remove(ConfigKey key)
	{
		tree.remove(key);
	}

	public ConfigValue get(ConfigKey key)
	{
		ConfigValue v = tree.get(key);
		return (v == null) ? ConfigNull.INSTANCE : v;
	}

	public boolean isEmpty()
	{
		return tree.isEmpty();
	}

	@Nullable
	public ConfigKey getKey(String id)
	{
		for (ConfigKey key : tree.keySet())
		{
			if (key.getName().equalsIgnoreCase(id))
			{
				return key;
			}
		}

		return null;
	}

	public final Map<ConfigKey, ConfigValue> getTree()
	{
		return tree;
	}

	public ConfigTree copy()
	{
		ConfigTree t = new ConfigTree();
		tree.forEach((key, value) -> t.add(key, value.copy()));
		return t;
	}

	@Override
	public void writeData(ByteBuf data)
	{
		data.writeShort(tree.size());

		tree.forEach((key, value) ->
		{
			ByteBufUtils.writeUTF8String(data, key.getName());

			int flags = 0;

			if (key.isExcluded())
			{
				flags |= AdvancedConfigKey.EXCLUDED;
			}

			if (key.isHidden())
			{
				flags |= AdvancedConfigKey.HIDDEN;
			}

			if (key.cantEdit())
			{
				flags |= AdvancedConfigKey.CANT_EDIT;
			}

			if (key.useScrollBar())
			{
				flags |= AdvancedConfigKey.USE_SCROLL_BAR;
			}

			data.writeInt(flags);

			ConfigValue defValue = key.getDefValue();
			ByteBufUtils.writeUTF8String(data, defValue.getName());
			defValue.writeData(data);

			int extraFlags = 0;

			String displayName = key.getNameLangKey();

			if (!displayName.isEmpty())
			{
				extraFlags |= HAS_DISPLAY_NAME;
			}

			if (!key.getGroup().isEmpty())
			{
				extraFlags |= HAS_GROUP;
			}

			data.writeByte(extraFlags);

			if (!displayName.isEmpty())
			{
				ByteBufUtils.writeUTF8String(data, displayName);
			}

			if (!key.getGroup().isEmpty())
			{
				ByteBufUtils.writeUTF8String(data, key.getGroup());
			}

			ByteBufUtils.writeUTF8String(data, value.getName());
			value.writeData(data);
		});
	}

	@Override
	public void readData(ByteBuf data)
	{
		int s = data.readUnsignedShort();
		tree.clear();

		while (--s >= 0)
		{
			String id = ByteBufUtils.readUTF8String(data);
			int flags = data.readInt();

			ConfigValue value = FTBLibAPI.API.getConfigValueFromID(ByteBufUtils.readUTF8String(data));
			value.readData(data);

			AdvancedConfigKey key = new AdvancedConfigKey(id, value);

			if (Bits.getFlag(flags, AdvancedConfigKey.EXCLUDED))
			{
				key.setExcluded(true);
			}

			if (Bits.getFlag(flags, AdvancedConfigKey.HIDDEN))
			{
				key.setHidden(true);
			}

			if (Bits.getFlag(flags, AdvancedConfigKey.CANT_EDIT))
			{
				key.setCanEdit(false);
			}

			if (Bits.getFlag(flags, AdvancedConfigKey.USE_SCROLL_BAR))
			{
				key.setUseScrollBar(true);
			}

			int extraFlags = data.readUnsignedByte();

			if (Bits.getFlag(extraFlags, HAS_DISPLAY_NAME))
			{
				key.setNameLangKey(ByteBufUtils.readUTF8String(data));
			}

			if (Bits.getFlag(extraFlags, HAS_GROUP))
			{
				key.setGroup(ByteBufUtils.readUTF8String(data));
			}

			value = FTBLibAPI.API.getConfigValueFromID(ByteBufUtils.readUTF8String(data));
			value.readData(data);
			tree.put(key, value);
		}
	}

	@Override
	public void fromJson(JsonElement json)
	{
		JsonObject o = json.getAsJsonObject();
		tree.forEach((key, value) ->
		{
			if (!key.isExcluded())
			{
				JsonElement e = o.get(key.getName());

				if (e != null)
				{
					value.fromJson(e);
				}
			}
		});
	}

	@Override
	public JsonElement getSerializableElement()
	{
		JsonObject o = new JsonObject();
		tree.forEach((key, value) ->
		{
			if (!key.isExcluded())
			{
				o.add(key.getName(), value.getSerializableElement());
			}
		});

		return o;
	}
}