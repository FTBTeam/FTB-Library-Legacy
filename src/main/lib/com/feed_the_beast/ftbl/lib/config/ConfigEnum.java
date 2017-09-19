package com.feed_the_beast.ftbl.lib.config;

import com.feed_the_beast.ftbl.api.ICustomColor;
import com.feed_the_beast.ftbl.api.ICustomName;
import com.feed_the_beast.ftbl.lib.Color4I;
import com.feed_the_beast.ftbl.lib.MouseButton;
import com.feed_the_beast.ftbl.lib.NameMap;
import com.feed_the_beast.ftbl.lib.io.DataIn;
import com.feed_the_beast.ftbl.lib.io.DataOut;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nonnull;
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

	@Override
	@Nonnull
	public E getValue()
	{
		return value;
	}

	public void setValue(E e)
	{
		value = e;
	}

	@Override
	public String getString()
	{
		return getNameMap().getName(getValue());
	}

	public String toString()
	{
		return getString();
	}

	@Override
	public boolean getBoolean()
	{
		return !getString().equals("-");
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
		Color4I col = getValue() instanceof ICustomColor ? ((ICustomColor) getValue()).getCustomColor() : Color4I.NONE;
		return col.hasColor() ? col : COLOR;
	}

	@Override
	public void addInfo(ConfigValueInfo info, List<String> list)
	{
		ITextComponent component = info.defaultValue.getValue() instanceof ICustomName && ((ICustomName) info.defaultValue.getValue()).hasCustomName() ? ((ICustomName) info.defaultValue.getValue()).getCustomDisplayName() : null;
		list.add(TextFormatting.AQUA + "Def: " + (component == null ? info.defaultValue.getString() : component.getFormattedText()));
	}

	@Override
	public List<String> getVariants()
	{
		return getNameMap().keys;
	}

	@Override
	public void fromJson(JsonElement json)
	{
		setValue(getNameMap().get(json.getAsString()));
	}

	@Override
	public JsonElement getSerializableElement()
	{
		return new JsonPrimitive(getString());
	}

	@Override
	public void writeData(DataOut data)
	{
		data.writeShort(getNameMap().values.size());

		for (Map.Entry<String, E> entry : getNameMap().map.entrySet())
		{
			data.writeString(entry.getKey());
			data.writeTextComponent(entry.getValue() instanceof ICustomName && ((ICustomName) entry.getValue()).hasCustomName() ? ((ICustomName) entry.getValue()).getCustomDisplayName() : null);
			data.writeJson((entry.getValue() instanceof ICustomColor ? ((ICustomColor) entry.getValue()).getCustomColor() : Color4I.NONE).toJson());
		}

		data.writeShort(getNameMap().getIndex(getValue()));
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
	public void onClicked(IGuiEditConfig gui, ConfigValueInfo info, MouseButton button)
	{
		onClicked(button);
		gui.onChanged(info.id, getSerializableElement());
	}
}