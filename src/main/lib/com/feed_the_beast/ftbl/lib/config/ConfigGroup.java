package com.feed_the_beast.ftbl.lib.config;

import com.feed_the_beast.ftbl.api.FTBLibAPI;
import com.feed_the_beast.ftbl.lib.util.JsonUtils;
import com.feed_the_beast.ftbl.lib.util.NetUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.IJsonSerializable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author LatvianModder
 */
public final class ConfigGroup implements IJsonSerializable
{
	private ITextComponent title;
	private String supergroup = "";
	private final Map<String, ConfigValueInstance> map;
	private final Map<String, ITextComponent> groupNames;

	public ConfigGroup(@Nullable ITextComponent t, boolean linked)
	{
		title = t;
		map = linked ? new LinkedHashMap<>() : new HashMap<>();
		groupNames = new HashMap<>();
	}

	public ConfigGroup(@Nullable ITextComponent t)
	{
		this(t, false);
	}

	public ConfigGroup setTitle(ITextComponent t)
	{
		title = t;
		return this;
	}

	public ITextComponent getTitle()
	{
		return title;
	}

	public ConfigGroup setSupergroup(String s)
	{
		supergroup = s;
		return this;
	}

	public ConfigGroup setGroupName(String group, ITextComponent name)
	{
		groupNames.put(group, name);
		return this;
	}

	public ITextComponent getGroupName(String key)
	{
		ITextComponent name = groupNames.get(key);
		return name == null ? new TextComponentTranslation(supergroup.isEmpty() ? key : (supergroup + "." + key)) : name;
	}

	public String getNameKey(ConfigValueInfo info)
	{
		return info.displayName.isEmpty() ? (supergroup.isEmpty() ? info.id : (supergroup + "." + info.id)) : info.displayName;
	}

	public ConfigValueInfo add(ConfigValueInfo info, ConfigValue value)
	{
		map.put(info.id, new ConfigValueInstance(info, value));
		return info;
	}

	public ConfigValueInfo add(String group, String id, ConfigValue value)
	{
		return add(new ConfigValueInfo(group, id, value), value);
	}

	public boolean has(String key)
	{
		return map.containsKey(key);
	}

	public void remove(String key)
	{
		map.remove(key);
	}

	public ConfigValue get(String key)
	{
		ConfigValueInstance v = map.get(key);
		return (v == null) ? ConfigNull.INSTANCE : v.value;
	}

	public boolean isEmpty()
	{
		return map.isEmpty();
	}

	public final Map<String, ConfigValueInstance> getMap()
	{
		return map;
	}

	public ConfigGroup copy()
	{
		ConfigGroup g = new ConfigGroup(title.createCopy());

		for (ConfigValueInstance instance : map.values())
		{
			g.add(instance.info.copy(), instance.value.copy());
		}

		return g;
	}

	public void writeData(ByteBuf data)
	{
		NetUtils.writeTextComponent(data, title);
		ByteBufUtils.writeUTF8String(data, supergroup);

		data.writeShort(map.size());

		for (ConfigValueInstance instance : map.values())
		{
			ByteBufUtils.writeUTF8String(data, instance.info.id);
			instance.info.writeData(data);
			ByteBufUtils.writeUTF8String(data, instance.value.getName());
			instance.value.writeData(data);
		}

		data.writeShort(groupNames.size());

		for (Map.Entry<String, ITextComponent> entry : groupNames.entrySet())
		{
			ByteBufUtils.writeUTF8String(data, entry.getKey());
			ByteBufUtils.writeUTF8String(data, JsonUtils.toJson(JsonUtils.serializeTextComponent(entry.getValue())));
		}
	}

	public void readData(ByteBuf data)
	{
		title = NetUtils.readTextComponent(data);
		supergroup = ByteBufUtils.readUTF8String(data);

		int s = data.readUnsignedShort();
		map.clear();

		while (--s >= 0)
		{
			ConfigValueInfo info = new ConfigValueInfo(ByteBufUtils.readUTF8String(data));
			info.readData(data);
			ConfigValue value = FTBLibAPI.API.getConfigValueFromID(ByteBufUtils.readUTF8String(data));
			value.readData(data);
			map.put(info.id, new ConfigValueInstance(info, value));
		}

		s = data.readUnsignedShort();
		groupNames.clear();

		while (--s >= 0)
		{
			String group = ByteBufUtils.readUTF8String(data);
			groupNames.put(group, JsonUtils.deserializeTextComponent(JsonUtils.fromJson(ByteBufUtils.readUTF8String(data))));
		}
	}

	@Override
	public void fromJson(JsonElement json)
	{
		JsonObject o = json.getAsJsonObject();

		for (ConfigValueInstance instance : map.values())
		{
			if (!instance.info.excluded)
			{
				JsonElement e = o.get(instance.info.id);

				if (e != null)
				{
					instance.value.fromJson(e);
				}
			}
		}
	}

	@Override
	public JsonElement getSerializableElement()
	{
		JsonObject o = new JsonObject();

		for (ConfigValueInstance instance : map.values())
		{
			if (!instance.info.excluded)
			{
				o.add(instance.info.id, instance.value.getSerializableElement());
			}
		}

		return o;
	}
}