package com.feed_the_beast.ftblib.lib.config;

import com.feed_the_beast.ftblib.lib.io.DataIn;
import com.feed_the_beast.ftblib.lib.io.DataOut;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

/**
 * @author LatvianModder
 */
public class ConfigItemStack extends ConfigValue
{
	public static final String ID = "item_stack";

	private ItemStack value;

	public ConfigItemStack()
	{
	}

	public ConfigItemStack(ItemStack is)
	{
		value = is;
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

	public void setItem(ItemStack is)
	{
		value = is.isEmpty() ? ItemStack.EMPTY : is;
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
		return new ConfigItemStack(getStack());
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
			setItem(ItemStack.EMPTY);
		}
		else
		{
			setItem(new ItemStack(nbt1));
		}
	}

	@Override
	public void writeData(DataOut data)
	{
		data.writeItemStack(getStack());
	}

	@Override
	public void readData(DataIn data)
	{
		setItem(data.readItemStack());
	}

	@Override
	public boolean isEmpty()
	{
		return getStack().isEmpty();
	}

	@Override
	public ITextComponent getStringForGUI()
	{
		ItemStack stack = getStack();

		if (stack.getCount() <= 1)
		{
			return new TextComponentString(stack.getDisplayName());
		}

		return new TextComponentString(stack.getCount() + "x " + stack.getDisplayName());
	}

	@Override
	public boolean setValueFromString(String string, boolean simulate)
	{
		if (string.isEmpty())
		{
			return false;
		}

		if (string.charAt(0) != '{')
		{
			Item item = Item.REGISTRY.getObject(new ResourceLocation(string));

			if (item != null)
			{
				if (!simulate)
				{
					setItem(new ItemStack(item));
				}

				return true;
			}
		}

		try
		{
			NBTTagCompound nbt = JsonToNBT.getTagFromJson(string);

			if (nbt.hasKey("id"))
			{
				if (!nbt.hasKey("Count"))
				{
					nbt.setByte("Count", (byte) 1);
				}

				ItemStack stack = new ItemStack(nbt);

				if (!simulate)
				{
					setItem(stack);
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
	public void setValueFromOtherValue(ConfigValue value)
	{
		if (value instanceof ConfigItemStack)
		{
			setItem(((ConfigItemStack) value).getStack().copy());
		}
		else
		{
			super.setValueFromOtherValue(value);
		}
	}
}