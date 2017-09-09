package com.feed_the_beast.ftbl.lib.config;

import com.feed_the_beast.ftbl.lib.Color4I;
import com.feed_the_beast.ftbl.lib.MouseButton;
import com.feed_the_beast.ftbl.lib.gui.misc.GuiSelectors;
import com.feed_the_beast.ftbl.lib.io.IExtendedIOObject;
import com.feed_the_beast.ftbl.lib.util.JsonUtils;
import com.google.gson.JsonElement;
import net.minecraft.util.IJsonSerializable;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author LatvianModder
 */
public abstract class ConfigValue implements IStringSerializable, IExtendedIOObject, IJsonSerializable
{
	@Nullable
	public abstract Object getValue();

	public abstract String getString();

	public abstract boolean getBoolean();

	public abstract int getInt();

	public double getDouble()
	{
		return getInt();
	}

	public boolean containsInt(int val)
	{
		return getInt() == val;
	}

	public abstract ConfigValue copy();

	public boolean equalsValue(ConfigValue value)
	{
		return Objects.equals(getValue(), value.getValue());
	}

	public Color4I getColor()
	{
		return Color4I.GRAY;
	}

	public void addInfo(ConfigKey key, List<String> list)
	{
		list.add(TextFormatting.AQUA + "Def: " + key.getDefValue().getString());
	}

	public List<String> getVariants()
	{
		return Collections.emptyList();
	}

	public boolean isNull()
	{
		return false;
	}

	public void onClicked(IGuiEditConfig gui, ConfigKey key, MouseButton button)
	{
		GuiSelectors.selectJson(this, (value, set) ->
		{
			if (set)
			{
				fromJson(value.getSerializableElement());
				gui.onChanged(key, getSerializableElement());
			}

			gui.openGui();
		});
	}

	public boolean setValueFromString(String text, boolean simulate)
	{
		try
		{
			JsonElement json = JsonUtils.fromJson(text);

			if (!json.isJsonNull())
			{
				if (!simulate)
				{
					fromJson(json);
				}

				return true;
			}
		}
		catch (Exception ex)
		{
		}

		return false;
	}

	@Override
	public boolean equals(Object o)
	{
		return o instanceof ConfigValue && equalsValue((ConfigValue) o);
	}

	@Override
	public String toString()
	{
		return getString();
	}
}