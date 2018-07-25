package com.feed_the_beast.ftblib.lib.config;

import com.feed_the_beast.ftblib.lib.gui.IOpenableGui;
import com.feed_the_beast.ftblib.lib.icon.Color4I;
import com.feed_the_beast.ftblib.lib.io.DataIn;
import com.feed_the_beast.ftblib.lib.io.DataOut;
import com.feed_the_beast.ftblib.lib.util.misc.MouseButton;
import com.feed_the_beast.ftblib.lib.util.misc.NameMap;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author LatvianModder
 */
public class ConfigEnum<E> extends ConfigValue
{
	public static final String ID = "enum";
	public static final Color4I COLOR = Color4I.rgb(0x0094FF);

	public static <T extends Enum<T>> ConfigEnum<T> create(NameMap<T> nm, Supplier<T> getter, Consumer<T> setter)
	{
		return new ConfigEnum<T>(nm)
		{
			@Override
			public T getValue()
			{
				return getter.get();
			}

			@Override
			public void setValue(T e)
			{
				setter.accept(e);
			}
		};
	}

	private final NameMap<E> nameMap;
	private E value;

	public ConfigEnum(NameMap<E> nm)
	{
		nameMap = nm;
		value = nm.defaultValue;
	}

	@Override
	public String getName()
	{
		return ID;
	}

	public NameMap<E> getNameMap()
	{
		return nameMap;
	}

	public E getValue()
	{
		return value;
	}

	public void setValue(E e)
	{
		value = e;
	}

	public void setValue(String value)
	{
		setValue(getNameMap().get(value));
	}

	@Override
	public String getString()
	{
		return getNameMap().getName(getValue());
	}

	@Override
	public ITextComponent getStringForGUI()
	{
		return getNameMap().getDisplayName(null, getValue());
	}

	@Override
	public boolean getBoolean()
	{
		return getValue() != getNameMap().defaultValue;
	}

	@Override
	public int getInt()
	{
		return getNameMap().getIndex(getValue());
	}

	@Override
	public ConfigEnum<E> copy()
	{
		return new ConfigEnum<>(getNameMap().withDefault(getNameMap().get(getInt())));
	}

	@Override
	public Color4I getColor()
	{
		Color4I col = getNameMap().getColor(getValue());
		return col.isEmpty() ? COLOR : col;
	}

	@Override
	public void addInfo(ConfigValueInstance inst, List<String> list)
	{
		list.add(TextFormatting.AQUA + "Default: " + TextFormatting.RESET + getNameMap().getDisplayName(null, getNameMap().get(inst.getDefaultValue().getString())).getFormattedText());
	}

	@Override
	public List<String> getVariants()
	{
		return getNameMap().keys;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt, String key)
	{
		nbt.setString(key, getString());
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt, String key)
	{
		setValue(nbt.getString(key));
	}

	@Override
	public void writeData(DataOut data)
	{
		NameMap<E> nameMap = getNameMap();

		data.writeShort(nameMap.size());

		for (Map.Entry<String, E> entry : nameMap.map.entrySet())
		{
			data.writeString(entry.getKey());
			data.writeTextComponent(nameMap.getDisplayName(null, entry.getValue()));
			data.writeIcon(nameMap.getColor(entry.getValue()));
		}

		data.writeShort(nameMap.getIndex(getValue()));
	}

	@Override
	public void readData(DataIn data)
	{
		throw new IllegalStateException("Can't read Abstract Enum Property!");
	}

	public void onClicked(MouseButton button)
	{
		setValue(button.isLeft() ? getNameMap().getNext(getValue()) : getNameMap().getPrevious(getValue()));
	}

	@Override
	public void onClicked(IOpenableGui gui, ConfigValueInstance inst, MouseButton button)
	{
		onClicked(button);
		gui.openGui();
	}

	@Override
	public void setValueFromOtherValue(ConfigValue value)
	{
		setValue(value.getString());
	}
}