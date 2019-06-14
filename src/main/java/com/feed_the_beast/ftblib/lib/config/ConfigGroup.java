package com.feed_the_beast.ftblib.lib.config;

import com.feed_the_beast.ftblib.lib.io.DataIn;
import com.feed_the_beast.ftblib.lib.io.DataOut;
import com.feed_the_beast.ftblib.lib.util.FinalIDObject;
import com.feed_the_beast.ftblib.lib.util.misc.BooleanConsumer;
import com.feed_the_beast.ftblib.lib.util.misc.NameMap;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;
import java.util.function.LongConsumer;
import java.util.function.LongSupplier;
import java.util.function.Supplier;
import java.util.regex.Pattern;

/**
 * @author LatvianModder
 */
public class ConfigGroup extends FinalIDObject
{
	public static final ConfigGroup DEFAULT = newGroup("default");

	public static final DataOut.Serializer<ConfigGroup> SERIALIZER = (data, object) ->
	{
		data.writeString(object.getID());
		data.writeTextComponent(object.displayName);
		data.writeVarInt(object.values.size());

		for (ConfigValueInstance instance : object.getValues())
		{
			data.writeString(instance.getID());
			instance.writeData(data);
		}

		data.writeVarInt(object.groups.size());

		for (ConfigGroup group : object.getGroups())
		{
			ConfigGroup.SERIALIZER.write(data, group);
		}
	};

	public static final DataIn.Deserializer<ConfigGroup> DESERIALIZER = data ->
	{
		ConfigGroup group = newGroup(data.readString());
		group.displayName = data.readTextComponent();

		int s = data.readVarInt();
		group.values.clear();

		for (int i = 0; i < s; i++)
		{
			ConfigValueInstance inst = new ConfigValueInstance(group, data);
			group.values.put(inst.getID(), inst);
		}

		s = data.readVarInt();
		group.groups.clear();

		for (int i = 0; i < s; i++)
		{
			ConfigGroup group1 = ConfigGroup.DESERIALIZER.read(data);
			group1.parent = group;
			group.groups.put(group1.getID(), group1);
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
		values = new LinkedHashMap<>();
		groups = new LinkedHashMap<>();
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
				groups.put(g.getID(), g);
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

		values.put(inst.getID(), inst);
		return inst;
	}

	public <T extends ConfigValue> ConfigValueInstance add(String id, T value, @Nullable T def)
	{
		ConfigValueInstance instance = add(new ConfigValueInstance(id, this, value));
		instance.setOrder(values.size());

		if (def != null)
		{
			instance.setDefaultValue(def);
		}

		return instance;
	}

	public ConfigValueInstance addBool(String id, BooleanSupplier getter, BooleanConsumer setter, boolean def)
	{
		return add(id, new ConfigBoolean.SimpleBoolean(getter, setter), new ConfigBoolean(def));
	}

	public ConfigValueInstance addInt(String id, IntSupplier getter, IntConsumer setter, int def, int min, int max)
	{
		return add(id, new ConfigInt.SimpleInt(min, max, getter, setter), new ConfigInt(def));
	}

	public ConfigValueInstance addLong(String id, LongSupplier getter, LongConsumer setter, long def, long min, long max)
	{
		return add(id, new ConfigLong.SimpleLong(min, max, getter, setter), new ConfigLong(def));
	}

	public ConfigValueInstance addDouble(String id, DoubleSupplier getter, DoubleConsumer setter, double def, double min, double max)
	{
		return add(id, new ConfigDouble.SimpleDouble(min, max, getter, setter), new ConfigDouble(def));
	}

	public ConfigValueInstance addString(String id, Supplier<String> getter, Consumer<String> setter, String def, @Nullable Pattern pattern)
	{
		return add(id, new ConfigString.SimpleString(getter, setter, pattern), new ConfigString(def));
	}

	public ConfigValueInstance addString(String id, Supplier<String> getter, Consumer<String> setter, String def)
	{
		return addString(id, getter, setter, def, null);
	}

	public <T> ConfigValueInstance addEnum(String id, Supplier<T> getter, Consumer<T> setter, NameMap<T> nameMap)
	{
		return add(id, new ConfigEnum.SimpleEnum<>(nameMap, getter, setter), new ConfigEnum<>(nameMap));
	}

	public <C extends ConfigValue, V> ConfigValueInstance addList(String id, Collection<V> c, C type, Function<V, C> toConfig, Function<C, V> fromConfig)
	{
		return add(id, new ConfigList.SimpleList<>(c, type, toConfig, fromConfig), new ConfigList<>(type));
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

	public final List<ConfigValueInstance> getValueTree()
	{
		List<ConfigValueInstance> list = new ArrayList<>(getValues());

		for (ConfigGroup group : getGroups())
		{
			list.addAll(group.getValueTree());
		}

		return list;
	}

	public final Collection<ConfigGroup> getGroups()
	{
		return groups.values();
	}

	public ConfigGroup copy()
	{
		ConfigGroup g = new ConfigGroup(getID());
		g.displayName = displayName == null ? null : displayName.createCopy();

		for (ConfigGroup group : getGroups())
		{
			ConfigGroup gr = group.copy();
			gr.parent = g;
			g.groups.put(gr.getID(), gr);
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
			return getID();
		}

		return parent.getPath() + "." + getID();
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
				list.add(instance.getID());
			}
			else
			{
				list.add(path + "." + instance.getID());
			}
		}
	}

	public String toString()
	{
		Map<String, String> map = new LinkedHashMap<>();

		for (ConfigGroup group : getGroups())
		{
			map.put(group.getID(), group.toString());
		}

		for (ConfigValueInstance instance : getValues())
		{
			map.put(instance.getID(), instance.getValue().getString());
		}

		return parent == null ? (getID() + "#" + map) : map.toString();
	}

	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();

		for (ConfigValueInstance instance : getValues())
		{
			if (!instance.getExcluded())
			{
				instance.getValue().writeToNBT(nbt, instance.getID());
			}
		}

		for (ConfigGroup group : getGroups())
		{
			nbt.setTag(group.getID(), group.serializeNBT());
		}

		return nbt;
	}

	public void deserializeEditedNBT(NBTTagCompound nbt)
	{
		for (ConfigValueInstance instance : getValues())
		{
			if (!instance.getExcluded() && instance.getCanEdit())
			{
				instance.getValue().readFromNBT(nbt, instance.getID());
			}
		}

		for (ConfigGroup group : getGroups())
		{
			group.deserializeEditedNBT(nbt.getCompoundTag(group.getID()));
		}
	}
}