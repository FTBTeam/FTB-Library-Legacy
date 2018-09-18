package com.feed_the_beast.ftblib.lib.config;

import com.feed_the_beast.ftblib.lib.gui.IOpenableGui;
import com.feed_the_beast.ftblib.lib.gui.misc.GuiEditConfigValue;
import com.feed_the_beast.ftblib.lib.icon.Color4I;
import com.feed_the_beast.ftblib.lib.io.DataIn;
import com.feed_the_beast.ftblib.lib.io.DataOut;
import com.feed_the_beast.ftblib.lib.io.DataReader;
import com.feed_the_beast.ftblib.lib.math.Ticks;
import com.feed_the_beast.ftblib.lib.util.JsonUtils;
import com.feed_the_beast.ftblib.lib.util.misc.MouseButton;
import com.google.gson.JsonElement;
import net.minecraft.command.ICommandSender;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

/**
 * @author LatvianModder
 */
public abstract class ConfigValue implements IStringSerializable
{
	public abstract String getString();

	public abstract boolean getBoolean();

	public abstract int getInt();

	public double getDouble()
	{
		return getLong();
	}

	public long getLong()
	{
		return getInt();
	}

	public Ticks getTimer()
	{
		return Ticks.NO_TICKS;
	}

	public abstract ConfigValue copy();

	public boolean equalsValue(ConfigValue value)
	{
		return value == this || getString().equals(value.getString());
	}

	public Color4I getColor()
	{
		return Color4I.GRAY;
	}

	public void addInfo(ConfigValueInstance inst, List<String> list)
	{
		if (inst.getCanEdit() && !inst.getDefaultValue().isNull())
		{
			list.add(TextFormatting.AQUA + "Default: " + TextFormatting.RESET + inst.getDefaultValue().getStringForGUI().getFormattedText());
		}
	}

	public List<String> getVariants()
	{
		return Collections.emptyList();
	}

	public boolean isNull()
	{
		return false;
	}

	public void onClicked(IOpenableGui gui, ConfigValueInstance inst, MouseButton button)
	{
		if (this instanceof IIteratingConfig)
		{
			((IIteratingConfig) this).iterate(inst, button.isLeft());
			return;
		}

		gui.openContextMenu(new GuiEditConfigValue(inst, (value, set) ->
		{
			if (set)
			{
				setValueFromOtherValue(value);
			}

			gui.closeContextMenu();
		}));
	}

	public boolean setValueFromString(@Nullable ICommandSender sender, String string, boolean simulate)
	{
		JsonElement json = DataReader.get(string).safeJson();

		if (!json.isJsonNull())
		{
			if (!simulate)
			{
				NBTTagCompound nbt = new NBTTagCompound();
				NBTBase nbt1 = JsonUtils.toNBT(json);

				if (nbt1 != null)
				{
					nbt.setTag("x", nbt1);
				}

				readFromNBT(nbt, "x");
			}

			return true;
		}

		return false;
	}

	public void setValueFromOtherValue(ConfigValue value)
	{
		setValueFromString(null, value.getString(), false);
	}

	public void setValueFromJson(JsonElement json)
	{
		if (json.isJsonPrimitive())
		{
			setValueFromString(null, json.getAsString(), false);
		}
	}

	@Override
	public boolean equals(Object o)
	{
		return o instanceof ConfigValue && equalsValue((ConfigValue) o);
	}

	@Override
	public final String toString()
	{
		return getString();
	}

	public ITextComponent getStringForGUI()
	{
		return new TextComponentString(getString());
	}

	public abstract void writeToNBT(NBTTagCompound nbt, String key);

	public abstract void readFromNBT(NBTTagCompound nbt, String key);

	public abstract void writeData(DataOut data);

	public abstract void readData(DataIn data);

	public boolean isEmpty()
	{
		return !getBoolean();
	}
}