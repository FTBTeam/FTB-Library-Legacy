package com.feed_the_beast.ftbl.lib.config;

import com.feed_the_beast.ftbl.lib.Color4I;
import com.feed_the_beast.ftbl.lib.MouseButton;
import com.feed_the_beast.ftbl.lib.math.MathUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author LatvianModder
 */
public class ConfigStringEnum extends ConfigValue
{
	private List<String> keys;
	private String value;

	public ConfigStringEnum()
	{
		this(Collections.emptyList(), "");
	}

	public ConfigStringEnum(Collection<String> k, String v)
	{
		keys = new ArrayList<>(k);
		value = v;
	}

	@Override
	public String getName()
	{
		return ConfigEnumAbstract.ID;
	}

	@Nullable
	@Override
	public Object getValue()
	{
		return getString();
	}

	public void setString(String v)
	{
		value = v;
	}

	@Override
	public String getString()
	{
		return value;
	}

	@Override
	public boolean getBoolean()
	{
		return !value.equals("-");
	}

	@Override
	public int getInt()
	{
		return keys.indexOf(value);
	}

	@Override
	public ConfigStringEnum copy()
	{
		return new ConfigStringEnum(keys, getString());
	}

	@Override
	public Color4I getColor()
	{
		return ConfigEnumAbstract.COLOR;
	}

	@Override
	public List<String> getVariants()
	{
		return Collections.unmodifiableList(keys);
	}

	@Override
	public void onClicked(IGuiEditConfig gui, ConfigKey key, MouseButton button)
	{
		setString(keys.get(MathUtils.wrap(getInt() + (button.isLeft() ? 1 : -1), keys.size())));
		gui.onChanged(key, getSerializableElement());
	}

	@Override
	public void fromJson(JsonElement json)
	{
		setString(json.getAsString());
	}

	@Override
	public JsonElement getSerializableElement()
	{
		return new JsonPrimitive(getString());
	}

	@Override
	public void writeData(ByteBuf data)
	{
		data.writeShort(keys.size());

		for (String s : keys)
		{
			ByteBufUtils.writeUTF8String(data, s);
		}

		data.writeShort(getInt());
	}

	@Override
	public void readData(ByteBuf data)
	{
		keys.clear();
		int s = data.readUnsignedShort();

		while (--s >= 0)
		{
			keys.add(ByteBufUtils.readUTF8String(data));
		}

		setString(keys.get(data.readUnsignedShort()));
	}
}