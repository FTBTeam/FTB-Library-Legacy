package com.feed_the_beast.ftbl.lib.config;

import com.feed_the_beast.ftbl.lib.item.ItemStackSerializer;
import com.google.gson.JsonElement;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import javax.annotation.Nullable;

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

	public ConfigItemStack(@Nullable ItemStack is)
	{
		value = is;
	}

	@Override
	public String getName()
	{
		return ID;
	}

	public ItemStack getItem()
	{
		return value;
	}

	/**
	 * @param sizeMode 0 - ignore stack size, 1 - itemStack size must be equal, 2 - itemStack size must be equal or larger
	 * @return stack that is equal to itemStack. null if none match
	 */
	public boolean matchesItem(ItemStack itemStack, int sizeMode)
	{
		ItemStack is = getItem();
		boolean isempty = is.isEmpty();
		boolean stackempty = itemStack.isEmpty();

		if (isempty || stackempty)
		{
			return isempty == stackempty;
		}

		int issize = is.getCount();
		int stackSize = itemStack.getCount();

		Item item0 = itemStack.getItem();
		int meta = itemStack.getMetadata();

		if (is.getItem() == item0 && is.getMetadata() == meta)
		{
			switch (sizeMode)
			{
				case 1:
					if (issize == stackSize)
					{
						return true;
					}
					break;
				case 2:
					if (issize <= stackSize)
					{
						return true;
					}
					break;
				default:
					return true;
			}
		}

		return false;
	}

	public void setItem(@Nullable ItemStack is)
	{
		value = is;
	}

	@Nullable
	@Override
	public Object getValue()
	{
		return getItem();
	}

	@Override
	public String getString()
	{
		ItemStack is = getItem();
		return is.getCount() + "x " + (is.isEmpty() ? "Air" : is.getDisplayName());
	}

	@Override
	public boolean getBoolean()
	{
		return getInt() > 0;
	}

	@Override
	public int getInt()
	{
		return getItem().getCount();
	}

	@Override
	public ConfigItemStack copy()
	{
		return new ConfigItemStack(getItem());
	}

	@Override
	public void fromJson(JsonElement o)
	{
		setItem(ItemStackSerializer.deserialize(o));
	}

	@Override
	public JsonElement getSerializableElement()
	{
		return ItemStackSerializer.serialize(getItem());
	}

	@Override
	public void writeData(ByteBuf data)
	{
		ByteBufUtils.writeItemStack(data, getItem());
	}

	@Override
	public void readData(ByteBuf data)
	{
		setItem(ByteBufUtils.readItemStack(data));
	}
}