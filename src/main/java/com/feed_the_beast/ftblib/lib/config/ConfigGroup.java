package com.feed_the_beast.ftblib.lib.config;

import com.feed_the_beast.ftblib.lib.io.DataIn;
import com.feed_the_beast.ftblib.lib.io.DataOut;
import com.feed_the_beast.ftblib.lib.util.FinalIDObject;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author LatvianModder
 */
public class ConfigGroup extends FinalIDObject implements INBTSerializable<NBTTagCompound>
{
	public static final ConfigGroup DEFAULT = newGroup("default");

	public static final DataOut.Serializer<ConfigGroup> SERIALIZER = (data, object) ->
	{
		data.writeString(object.getName());
		data.writeTextComponent(object.displayName);
		data.writeShort(object.values.size());

		for (ConfigValueInstance instance : object.getValues())
		{
			data.writeString(instance.getName());
			instance.writeData(data);
		}

		data.writeShort(object.groups.size());

		for (ConfigGroup group : object.getGroups())
		{
			ConfigGroup.SERIALIZER.write(data, group);
		}
	};

	public static final DataIn.Deserializer<ConfigGroup> DESERIALIZER = data ->
	{
		ConfigGroup group = newGroup(data.readString());
		group.displayName = data.readTextComponent();

		int s = data.readUnsignedShort();
		group.values.clear();

		while (--s >= 0)
		{
			ConfigValueInstance inst = new ConfigValueInstance(group, data);
			group.values.put(inst.getName(), inst);
		}

		s = data.readUnsignedShort();
		group.groups.clear();

		while (--s >= 0)
		{
			ConfigGroup group1 = ConfigGroup.DESERIALIZER.read(data);
			group1.parent = group;
			group.groups.put(group1.getName(), group1);
		}

		return group;
	};

	public static ConfigGroup newGroup(String name)
	{
		return new ConfigGroup(name);
	}

	public ConfigGroup parent;
	private ITextComponent displayName;
	private final Map<String, ConfigValueInstance> values;
	private final Map<String, ConfigGroup> groups;

	private ConfigGroup(String id)
	{
		super(id);
		values = new HashMap<>();
		groups = new HashMap<>();
	}

	public ConfigGroup setDisplayName(ITextComponent t)
	{
		displayName = t;
		return this;
	}

	public ITextComponent getDisplayName()
	{
		return displayName == null ? new TextComponentTranslation(getPath()) : displayName;
	}

	@Nullable
	public ConfigGroup getNullableGroup(String id)
	{
		return groups.get(id);
	}

	public ConfigGroup getGroup(String id)
	{
		int index = id.indexOf('.');

		if (index == -1)
		{
			ConfigGroup g = groups.get(id);

			if (g == null)
			{
				g = new ConfigGroup(id);
				g.parent = this;
				groups.put(g.getName(), g);
			}

			return g;
		}

		return getGroup(id.substring(0, index)).getGroup(id.substring(index + 1));
	}

	public ConfigValueInstance add(ConfigValueInstance inst)
	{
		if (inst.getGroup() != this)
		{
			throw new IllegalArgumentException("Can't add to this group, parent doesn't match!");
		}

		values.put(inst.getName(), inst);
		return inst;
	}

	public <T extends ConfigValue> ConfigValueInstance add(String id, T value, @Nullable T def)
	{
		ConfigValueInstance instance = add(new ConfigValueInstance(id, this, value));
		instance.setOrder((byte) Math.min(values.size(), 127));

		if (def != null)
		{
			instance.setDefaultValue(def);
		}

		return instance;
	}

	public boolean hasValue(String key)
	{
		return values.containsKey(key);
	}

	public void removeValue(String key)
	{
		values.remove(key);
	}

	@Nullable
	public ConfigValueInstance getValueInstance(String key)
	{
		int index = key.indexOf('.');
		return index == -1 ? values.get(key) : getGroup(key.substring(0, index)).values.get(key);
	}

	public ConfigValue getValue(String key)
	{
		ConfigValueInstance v = getValueInstance(key);
		return (v == null) ? ConfigNull.INSTANCE : v.getValue();
	}

	public final Collection<ConfigValueInstance> getValues()
	{
		return values.values();
	}

	public final Collection<ConfigGroup> getGroups()
	{
		return groups.values();
	}

	public ConfigGroup copy()
	{
		ConfigGroup g = new ConfigGroup(getName());

		if (displayName != null)
		{
			g.setDisplayName(displayName.createCopy());
		}

		for (ConfigValueInstance instance : getValues())
		{
			g.add(instance.copy(g));
		}

		return g;
	}

	public String getPath()
	{
		if (parent == null)
		{
			return getName();
		}

		return parent.getPath() + "." + getName();
	}

	public ITextComponent getDisplayNameOf(ConfigValueInstance inst)
	{
		return new TextComponentTranslation(inst.getPath());
	}

	public ITextComponent getInfoOf(ConfigValueInstance inst)
	{
		ITextComponent name = inst.getDisplayName();

		if (name instanceof TextComponentTranslation)
		{
			return new TextComponentTranslation(((TextComponentTranslation) name).getKey() + ".tooltip");
		}

		return new TextComponentTranslation(inst.getPath() + ".tooltip");
	}

	public List<String> getValueKeyTree()
	{
		List<String> list = new ArrayList<>();
		getValueKeyTree0(list, "");
		return list;
	}

	private void getValueKeyTree0(List<String> list, String path)
	{
		for (ConfigValueInstance instance : getValues())
		{
			if (path.isEmpty())
			{
				list.add(instance.getName());
			}
			else
			{
				list.add(path + "." + instance.getName());
			}
		}
	}

	public String toString()
	{
		Map<String, String> map = new LinkedHashMap<>();

		for (ConfigGroup group : getGroups())
		{
			map.put(group.getName(), group.toString());
		}

		for (ConfigValueInstance instance : getValues())
		{
			map.put(instance.getName(), instance.getValue().getString());
		}

		return parent == null ? (getName() + "#" + map) : map.toString();
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();

		for (ConfigValueInstance instance : getValues())
		{
			if (!instance.getExcluded())
			{
				instance.getValue().writeToNBT(nbt, instance.getName());
			}
		}

		for (ConfigGroup group : getGroups())
		{
			NBTTagCompound nbt1 = group.serializeNBT();

			if (!nbt1.isEmpty())
			{
				nbt.setTag(group.getName(), nbt1);
			}
		}

		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		for (ConfigValueInstance instance : getValues())
		{
			if (!instance.getExcluded())
			{
				instance.getValue().readFromNBT(nbt, instance.getName());
			}
		}

		for (ConfigGroup group : getGroups())
		{
			group.deserializeNBT(nbt.getCompoundTag(group.getName()));
		}
	}
}