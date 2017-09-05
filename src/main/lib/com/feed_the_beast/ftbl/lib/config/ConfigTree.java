package com.feed_the_beast.ftbl.lib.config;

import com.feed_the_beast.ftbl.api.FTBLibAPI;
import com.feed_the_beast.ftbl.api.config.IConfigKey;
import com.feed_the_beast.ftbl.api.config.IConfigTree;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.lib.io.Bits;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author LatvianModder
 */
public class ConfigTree implements IConfigTree
{
	private static final int HAS_DISPLAY_NAME = 1;
	private static final int HAS_GROUP = 2;

	private final Map<IConfigKey, IConfigValue> tree;

	public ConfigTree(boolean linked)
	{
		tree = linked ? new LinkedHashMap<>() : new HashMap<>();
	}

	public ConfigTree()
	{
		this(false);
	}

	@Override
	public final Map<IConfigKey, IConfigValue> getTree()
	{
		return tree;
	}

	@Override
	public IConfigTree copy()
	{
		ConfigTree t = new ConfigTree();
		getTree().forEach((key, value) -> t.add(key, value.copy()));
		return t;
	}

	@Override
	public void writeData(ByteBuf data)
	{
		data.writeShort(tree.size());

		tree.forEach((key, value) ->
		{
			ByteBufUtils.writeUTF8String(data, key.getName());
			data.writeInt(key.getFlags());

			IConfigValue defValue = key.getDefValue();
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

			IConfigValue value = FTBLibAPI.API.getConfigValueFromID(ByteBufUtils.readUTF8String(data));
			value.readData(data);

			ConfigKey key = new ConfigKey(id, value);
			key.setFlags(flags);

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
		getTree().forEach((key, value) ->
		{
			if (!key.getFlag(IConfigKey.EXCLUDED))
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
			if (!key.getFlag(IConfigKey.EXCLUDED))
			{
				o.add(key.getName(), value.getSerializableElement());
			}
		});

		return o;
	}
}