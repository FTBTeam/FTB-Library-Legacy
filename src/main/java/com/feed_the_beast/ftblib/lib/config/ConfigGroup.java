package com.feed_the_beast.ftblib.lib.config;

import com.feed_the_beast.ftblib.lib.data.FTBLibAPI;
import com.feed_the_beast.ftblib.lib.io.DataIn;
import com.feed_the_beast.ftblib.lib.io.DataOut;
import com.feed_the_beast.ftblib.lib.util.misc.Node;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.util.IJsonSerializable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author LatvianModder
 */
public final class ConfigGroup implements IJsonSerializable
{
	private ITextComponent title;
	private String supergroup = "";
	private final Map<Node, ConfigValueInstance> map;
	private final Map<String, ITextComponent> groupNames;
	public Consumer<ConfigGroup> readCallback = null;

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
		return info.displayName.isEmpty() ? (supergroup.isEmpty() ? info.id.toString() : (supergroup + "." + info.id)) : info.displayName;
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

	public final Map<Node, ConfigValueInstance> getMap()
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

	public void writeData(DataOut net)
	{
		net.writeTextComponent(title);
		net.writeString(supergroup);

		net.writeShort(map.size());

		for (ConfigValueInstance instance : map.values())
		{
			net.writeString(instance.info.id.toString());
			instance.info.writeData(net);
			net.writeString(instance.value.getName());
			instance.value.writeData(net);
		}

		net.writeShort(groupNames.size());

		for (Map.Entry<String, ITextComponent> entry : groupNames.entrySet())
		{
			net.writeString(entry.getKey());
			net.writeTextComponent(entry.getValue());
		}
	}

	public void readData(DataIn net)
	{
		title = net.readTextComponent();
		supergroup = net.readString();

		int s = net.readUnsignedShort();
		map.clear();

		while (--s >= 0)
		{
			ConfigValueInfo info = new ConfigValueInfo(Node.get(net.readString()));
			info.readData(net);
			ConfigValue value = FTBLibAPI.getConfigValueFromId(net.readString());
			value.readData(net);
			map.put(info.id, new ConfigValueInstance(info, value));
		}

		s = net.readUnsignedShort();
		groupNames.clear();

		while (--s >= 0)
		{
			String group = net.readString();
			groupNames.put(group, net.readTextComponent());
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
				JsonElement e = o.get(instance.info.id.toString());

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
				o.add(instance.info.id.toString(), instance.value.getSerializableElement());
			}
		}

		return o;
	}
}