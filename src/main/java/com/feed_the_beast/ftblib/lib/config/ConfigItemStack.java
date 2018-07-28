package com.feed_the_beast.ftblib.lib.config;

import com.feed_the_beast.ftblib.lib.gui.IOpenableGui;
import com.feed_the_beast.ftblib.lib.gui.misc.GuiSelectItemStack;
import com.feed_the_beast.ftblib.lib.io.DataIn;
import com.feed_the_beast.ftblib.lib.io.DataOut;
import com.feed_the_beast.ftblib.lib.item.ItemStackSerializer;
import com.feed_the_beast.ftblib.lib.util.misc.MouseButton;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

/**
 * @author LatvianModder
 */
public class ConfigItemStack extends ConfigValue
{
	public static final String ID = "item_stack";

	private ItemStack value;
	private boolean singleItemOnly;

	public ConfigItemStack(ItemStack is, boolean b)
	{
		value = is;
		singleItemOnly = b;

		if (singleItemOnly && value.getCount() > 1)
		{
			value.setCount(1);
		}
	}

	public ConfigItemStack(ItemStack is)
	{
		this(is, false);
	}

	@Override
	public String getName()
	{
		return ID;
	}

	public ItemStack getStack()
	{
		return value;
	}

	public void setStack(ItemStack is)
	{
		value = is.isEmpty() ? ItemStack.EMPTY : is;

		if (getSingleItemOnly() && value.getCount() > 1)
		{
			value.setCount(1);
		}
	}

	public boolean getSingleItemOnly()
	{
		return singleItemOnly;
	}

	public void setSingleItemOnly(boolean v)
	{
		singleItemOnly = v;
	}

	@Override
	public String getString()
	{
		return getStack().serializeNBT().toString();
	}

	@Override
	public boolean getBoolean()
	{
		return getInt() > 0;
	}

	@Override
	public int getInt()
	{
		return getStack().getCount();
	}

	@Override
	public ConfigItemStack copy()
	{
		return new ConfigItemStack(getStack(), getSingleItemOnly());
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt, String key)
	{
		value = getStack();

		if (!value.isEmpty())
		{
			nbt.setTag(key, value.serializeNBT());
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt, String key)
	{
		NBTTagCompound nbt1 = nbt.getCompoundTag(key);

		if (nbt1.isEmpty())
		{
			setStack(ItemStack.EMPTY);
		}
		else
		{
			setStack(new ItemStack(nbt1));
		}
	}

	@Override
	public void writeData(DataOut data)
	{
		data.writeItemStack(getStack());
		data.writeBoolean(getSingleItemOnly());
	}

	@Override
	public void readData(DataIn data)
	{
		setStack(data.readItemStack());
		setSingleItemOnly(data.readBoolean());
	}

	@Override
	public boolean isEmpty()
	{
		return getStack().isEmpty();
	}

	@Override
	public ITextComponent getStringForGUI()
	{
		value = getStack();

		if (value.getCount() <= 1)
		{
			return new TextComponentString(value.getDisplayName());
		}

		return new TextComponentString(value.getCount() + "x " + value.getDisplayName());
	}

	@Override
	public void onClicked(IOpenableGui gui, ConfigValueInstance inst, MouseButton button)
	{
		if (inst.getCanEdit())
		{
			new GuiSelectItemStack(inst, gui).openGui();
		}
	}

	@Override
	public boolean setValueFromString(String string, boolean simulate)
	{
		if (string.isEmpty())
		{
			return false;
		}

		try
		{
			ItemStack stack = ItemStackSerializer.parseItemThrowingException(string);

			if (stack.getCount() > 1 && getSingleItemOnly())
			{
				return false;
			}

			if (!simulate)
			{
				setStack(stack);
			}

			return true;
		}
		catch (Exception ex)
		{
			return false;
		}
	}

	@Override
	public void setValueFromOtherValue(ConfigValue value)
	{
		if (value instanceof ConfigItemStack)
		{
			setStack(((ConfigItemStack) value).getStack().copy());
		}
		else
		{
			super.setValueFromOtherValue(value);
		}
	}
}