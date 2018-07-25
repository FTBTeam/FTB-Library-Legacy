package com.feed_the_beast.ftblib.lib.config;

import com.feed_the_beast.ftblib.lib.data.FTBLibAPI;
import com.feed_the_beast.ftblib.lib.gui.IOpenableGui;
import com.feed_the_beast.ftblib.lib.gui.misc.GuiEditConfigList;
import com.feed_the_beast.ftblib.lib.icon.Color4I;
import com.feed_the_beast.ftblib.lib.io.DataIn;
import com.feed_the_beast.ftblib.lib.io.DataOut;
import com.feed_the_beast.ftblib.lib.util.misc.MouseButton;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author LatvianModder
 */
public final class ConfigList<T extends ConfigValue> extends ConfigValue implements Iterable<T>
{
	public static final String ID = "list";
	public static final Color4I COLOR = Color4I.rgb(0xFFAA49);

	private final List<T> list;
	private String type;

	public ConfigList(String id)
	{
		list = new ArrayList<>();
		type = id;
	}

	@Override
	public String getName()
	{
		return ID;
	}

	public String getType()
	{
		return type;
	}

	public void clear()
	{
		list.clear();
	}

	private boolean hasValidId()
	{
		return !type.equals(ConfigNull.ID);
	}

	public boolean canAdd(ConfigValue value)
	{
		return !value.isNull() && type.equals(value.getName());
	}

	public ConfigList<T> add(T v)
	{
		if (canAdd(v))
		{
			list.add(v);
		}

		return this;
	}

	public ConfigList<T> addAll(Collection<T> v)
	{
		for (T v1 : v)
		{
			add(v1);
		}

		return this;
	}

	public List<T> getList()
	{
		return list;
	}

	@Override
	public void writeData(DataOut data)
	{
		data.writeString(type);

		if (!hasValidId())
		{
			return;
		}

		Collection<T> list = getList();
		data.writeShort(list.size());

		for (ConfigValue s : list)
		{
			s.writeData(data);
		}
	}

	@Override
	public void readData(DataIn data)
	{
		clear();
		type = data.readString();

		if (!hasValidId())
		{
			return;
		}

		int s = data.readUnsignedShort();
		ConfigValue blank = FTBLibAPI.getConfigValueFromId(type);

		while (--s >= 0)
		{
			ConfigValue v = blank.copy();
			v.readData(data);
			add((T) v);
		}
	}

	@Override
	public String getString()
	{
		StringBuilder builder = new StringBuilder("[");

		for (int i = 0; i < list.size(); i++)
		{
			builder.append(list.get(i).getString());

			if (i != list.size() - 1)
			{
				builder.append(',');
				builder.append(' ');
			}
		}

		builder.append(']');
		return builder.toString();
	}

	@Override
	public boolean getBoolean()
	{
		return !list.isEmpty();
	}

	@Override
	public int getInt()
	{
		return list.size();
	}

	@Override
	public ConfigList<T> copy()
	{
		ConfigList<T> l = new ConfigList<>(type);

		for (T value : list)
		{
			l.add((T) value.copy());
		}

		return l;
	}

	@Override
	public Color4I getColor()
	{
		return COLOR;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt, String key)
	{
		if (hasValidId() && !list.isEmpty())
		{
			NBTTagList l = new NBTTagList();

			for (T value : list)
			{
				NBTTagCompound nbt1 = new NBTTagCompound();
				value.writeToNBT(nbt1, "value");
				l.appendTag(nbt1);
			}

			nbt.setTag(key, l);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt, String key)
	{
		clear();

		NBTTagList list = nbt.getTagList(key, Constants.NBT.TAG_COMPOUND);

		if (list.isEmpty())
		{
			return;
		}

		ConfigValue blank = FTBLibAPI.getConfigValueFromId(type);

		for (int i = 0; i < list.tagCount(); i++)
		{
			ConfigValue v = blank.copy();
			v.readFromNBT(list.getCompoundTagAt(i), "value");
			add((T) v);
		}
	}

	@Override
	public void addInfo(ConfigValueInstance inst, List<String> l)
	{
		if (list.isEmpty())
		{
			l.add(TextFormatting.AQUA + "Value: []");
		}
		else
		{
			l.add(TextFormatting.AQUA + "Value: [");

			for (T value : list)
			{
				l.add("  " + value.getStringForGUI().getFormattedText());
			}

			l.add(TextFormatting.AQUA + "]");
		}

		if (inst.getDefaultValue() instanceof ConfigList)
		{
			ConfigList<T> val = (ConfigList<T>) inst.getDefaultValue();

			if (val.list.isEmpty())
			{
				l.add(TextFormatting.AQUA + "Default: []");
			}
			else
			{
				l.add(TextFormatting.AQUA + "Default: [");

				for (T value : val.list)
				{
					l.add("  " + value.getStringForGUI().getFormattedText());
				}

				l.add(TextFormatting.AQUA + "]");
			}
		}
	}

	@Override
	public void onClicked(IOpenableGui gui, ConfigValueInstance inst, MouseButton button)
	{
		new GuiEditConfigList(inst).openGui();
	}

	@Override
	public ITextComponent getStringForGUI()
	{
		return new TextComponentString("...");
	}

	@Override
	public Iterator<T> iterator()
	{
		return list.iterator();
	}

	@Override
	public boolean isEmpty()
	{
		return list.isEmpty();
	}

	@Override
	public void setValueFromOtherValue(ConfigValue value)
	{
		list.clear();

		if (value instanceof ConfigList && type.equals(((ConfigList) value).type))
		{
			for (T v : (ConfigList<T>) value)
			{
				add((T) v.copy());
			}
		}
	}
}