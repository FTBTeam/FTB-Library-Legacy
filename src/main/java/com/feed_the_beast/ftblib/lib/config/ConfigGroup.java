package com.feed_the_beast.ftblib.lib.config;

import com.feed_the_beast.ftblib.lib.io.DataIn;
import com.feed_the_beast.ftblib.lib.io.DataOut;
import com.feed_the_beast.ftblib.lib.util.FinalIDObject;
import com.feed_the_beast.ftblib.lib.util.misc.BooleanConsumer;
import com.feed_the_beast.ftblib.lib.util.misc.NameMap;
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
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
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
public class ConfigGroup extends FinalIDObject implements INBTSerializable<NBTTagCompound>
{
	public static final ConfigGroup DEFAULT = newGroup("default");

	public static final DataOut.Serializer<ConfigGroup> SERIALIZER = (data, object) ->
	{
		data.writeString(object.getID());
		data.writeTextComponent(object.displayName);
		data.writeShort(object.values.size());

		for (ConfigValueInstance instance : object.getValues())
		{
			data.writeString(instance.getID());
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
			group.values.put(inst.getID(), inst);
		}

		s = data.readUnsignedShort();
		group.groups.clear();

		while (--s >= 0)
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
		instance.setOrder((byte) Math.min(values.size(), 127));

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

	public final Collection<ConfigGroup> getGroups()
	{
		return groups.values();
	}

	public ConfigGroup copy()
	{
		ConfigGroup g = new ConfigGroup(getID());

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

	@Override
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
			NBTTagCompound nbt1 = group.serializeNBT();

			if (!nbt1.isEmpty())
			{
				nbt.setTag(group.getID(), nbt1);
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
				instance.getValue().readFromNBT(nbt, instance.getID());
			}
		}

		for (ConfigGroup group : getGroups())
		{
			group.deserializeNBT(nbt.getCompoundTag(group.getID()));
		}
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