package com.feed_the_beast.ftblib.lib.config;

import com.feed_the_beast.ftblib.lib.icon.Color4I;
import com.feed_the_beast.ftblib.lib.icon.Icon;
import com.feed_the_beast.ftblib.lib.io.DataIn;
import com.feed_the_beast.ftblib.lib.io.DataOut;
import com.feed_the_beast.ftblib.lib.math.MathUtils;
import com.feed_the_beast.ftblib.lib.util.misc.MouseButton;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author LatvianModder
 */
public class ConfigStringEnum extends ConfigValue
{
	private final List<String> keys;
	private String value;
	private final Map<String, ITextComponent> customNames;
	private final Map<String, Color4I> customColors;

	public ConfigStringEnum()
	{
		this(Collections.emptyList(), "");
	}

	public ConfigStringEnum(Collection<String> k, String v)
	{
		keys = new ArrayList<>(k);
		value = v;
		customNames = new HashMap<>();
		customColors = new HashMap<>();
	}

	@Override
	public String getName()
	{
		return ConfigEnum.ID;
	}

	@Override
	public final Object getValue()
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
	public boolean hasCustomName()
	{
		return customNames.containsKey(getString());
	}

	@Override
	public ITextComponent getCustomDisplayName()
	{
		return customNames.get(getString());
	}

	@Override
	public Color4I getCustomColor()
	{
		Color4I col = customColors.get(getString());
		return col == null ? Icon.EMPTY : col;
	}

	public void setCustomName(String key, @Nullable ITextComponent component)
	{
		if (component == null)
		{
			customNames.remove(key);
		}
		else
		{
			customNames.put(key, component);
		}
	}

	public void setCustomColor(String key, Color4I col)
	{
		if (col.isEmpty())
		{
			customColors.remove(key);
		}
		else
		{
			customColors.put(key, col);
		}
	}

	@Override
	public boolean getBoolean()
	{
		return !getString().equals("-");
	}

	@Override
	public int getInt()
	{
		return keys.indexOf(getString());
	}

	@Override
	public ConfigStringEnum copy()
	{
		ConfigStringEnum config = new ConfigStringEnum(keys, getString());

		for (Map.Entry<String, ITextComponent> entry : customNames.entrySet())
		{
			config.customNames.put(entry.getKey(), entry.getValue().createCopy());
		}

		for (Map.Entry<String, Color4I> entry : customColors.entrySet())
		{
			config.customColors.put(entry.getKey(), entry.getValue().copy());
		}

		return config;
	}

	@Override
	public Color4I getColor()
	{
		return ConfigEnum.COLOR;
	}

	@Override
	public void addInfo(ConfigValueInfo info, List<String> list)
	{
		ITextComponent component = customNames.get(info.defaultValue.getString());
		list.add(TextFormatting.AQUA + "Def: " + (component == null ? info.defaultValue.getString() : component.getFormattedText()));
	}

	@Override
	public List<String> getVariants()
	{
		return Collections.unmodifiableList(keys);
	}

	@Override
	public void onClicked(IGuiEditConfig gui, ConfigValueInfo info, MouseButton button)
	{
		setString(keys.get(MathUtils.mod(getInt() + (button.isLeft() ? 1 : -1), keys.size())));
		gui.onChanged(info.id, getSerializableElement());
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
	public void writeData(DataOut data)
	{
		data.writeShort(keys.size());

		for (String s : keys)
		{
			data.writeString(s);
			data.writeTextComponent(customNames.get(s));
			data.writeIcon(customColors.get(s));
		}

		data.writeShort(getInt());
	}

	@Override
	public void readData(DataIn data)
	{
		keys.clear();
		customNames.clear();
		customColors.clear();
		int s = data.readUnsignedShort();

		while (--s >= 0)
		{
			String key = data.readString();
			keys.add(key);
			setCustomName(key, data.readTextComponent());
			Icon i = data.readIcon();

			if (i instanceof Color4I)
			{
				setCustomColor(key, (Color4I) i);
			}
		}

		setString(keys.get(data.readUnsignedShort()));
	}
}