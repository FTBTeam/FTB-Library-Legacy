package com.feed_the_beast.ftbl.lib.config;

import com.feed_the_beast.ftbl.api.config.IConfigKey;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.api.config.IGuiEditConfig;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.lib.Color4I;
import com.feed_the_beast.ftbl.lib.NameMap;
import com.feed_the_beast.ftbl.lib.math.MathUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author LatvianModder
 */
public abstract class PropertyEnumAbstract<E> extends PropertyBase
{
	public static final String ID = "enum";
	public static final Color4I COLOR = Color4I.rgb(0x0094FF);

	@Override
	public String getName()
	{
		return ID;
	}

	@Override
	@Nonnull
	public abstract E getValue();

	public abstract NameMap<E> getNameMap();

	public abstract void setValue(E e);

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
	public IConfigValue copy()
	{
		return new PropertyEnum<>(getNameMap().withDefault(getNameMap().get(getInt())));
	}

	@Override
	public Color4I getColor()
	{
		return COLOR;
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
	public void writeData(ByteBuf data)
	{
		data.writeShort(getNameMap().values.size());

		for (String s : getNameMap().keys)
		{
			ByteBufUtils.writeUTF8String(data, s);
		}

		data.writeShort(getNameMap().getIndex(getValue()));
	}

	@Override
	public void readData(ByteBuf data)
	{
		throw new IllegalStateException("Can't read Abstract Enum Property!");
	}

	@Override
	public void onClicked(IGuiEditConfig gui, IConfigKey key, IMouseButton button)
	{
		setValue(getNameMap().get(MathUtils.wrap(getInt() + (button.isLeft() ? 1 : -1), getNameMap().values.size())));
		gui.onChanged(key, getSerializableElement());
	}
}