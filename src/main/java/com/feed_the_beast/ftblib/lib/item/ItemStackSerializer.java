package com.feed_the_beast.ftblib.lib.item;

import com.feed_the_beast.ftblib.lib.util.JsonUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.oredict.OreDictionary;

public class ItemStackSerializer
{
	public static ItemStack parseItem(String input)
	{
		input = input.trim();
		if (input.isEmpty() || input.equals("-"))
		{
			return ItemStack.EMPTY;
		}
		else if (input.startsWith("{") && input.endsWith("}"))
		{
			try
			{
				ItemStack stack = new ItemStack(JsonToNBT.getTagFromJson(input));

				if (!stack.isEmpty())
				{
					return stack;
				}
			}
			catch (NBTException e)
			{
				e.printStackTrace();
			}
		}

		String[] s1 = input.split(" ", 4);

		if (s1.length == 0)
		{
			return ItemStack.EMPTY;
		}

		Item item = Item.REGISTRY.getObject(new ResourceLocation(s1[0]));

		if (item == null)
		{
			return ItemStack.EMPTY;
		}

		int stackSize = 1, meta = 0;

		if (s1.length >= 2)
		{
			stackSize = MathHelper.getInt(s1[1], 1);
		}

		if (s1.length >= 3)
		{
			meta = (s1[2].charAt(0) == '*') ? OreDictionary.WILDCARD_VALUE : MathHelper.getInt(s1[2], 0);
		}

		ItemStack itemstack = new ItemStack(item, stackSize, meta);

		if (s1.length >= 4)
		{
			try
			{
				itemstack.setTagCompound(JsonToNBT.getTagFromJson(s1[3]));
			}
			catch (Exception ex)
			{
			}
		}

		return itemstack;
	}

	public static String toString(ItemStack is)
	{
		if (is.isEmpty())
		{
			return "-";
		}

		StringBuilder builder = new StringBuilder(String.valueOf(Item.REGISTRY.getNameForObject(is.getItem())));

		int count = is.getCount();
		int meta = is.getMetadata();
		NBTTagCompound tag = is.getTagCompound();

		if (count > 1 || meta != 0 || tag != null)
		{
			builder.append(' ');
			builder.append(count);
		}

		if (meta != 0 || tag != null)
		{
			builder.append(' ');
			builder.append(meta);
		}

		if (tag != null)
		{
			builder.append(' ');
			builder.append(tag);
		}

		return builder.toString();
	}

	public static JsonElement serialize(ItemStack stack, boolean forceNonnull, boolean string)
	{
		if (!forceNonnull && stack.isEmpty())
		{
			return JsonNull.INSTANCE;
		}
		else if (string)
		{
			return new JsonPrimitive(toString(stack));
		}

		NBTTagCompound nbt = stack.serializeNBT();

		JsonObject json = new JsonObject();
		json.addProperty("item", nbt.getString("id"));

		if (stack.getHasSubtypes())
		{
			json.addProperty("data", nbt.getShort("Damage"));
		}

		if (stack.getCount() > 1)
		{
			json.addProperty("count", nbt.getByte("Count"));
		}

		if (stack.hasTagCompound())
		{
			json.add("nbt", JsonUtils.toJson(nbt.getCompoundTag("tag")));
		}

		if (nbt.hasKey("ForgeCaps"))
		{
			json.add("caps", JsonUtils.toJson(nbt.getCompoundTag("ForgeCaps")));
		}

		return json;
	}

	public static ItemStack deserialize(JsonElement e)
	{
		if (e.isJsonNull())
		{
			return ItemStack.EMPTY;
		}
		else if (e.isJsonPrimitive())
		{
			return parseItem(e.getAsString());
		}

		JsonObject json = e.getAsJsonObject();

		if (!json.has("item"))
		{
			return ItemStack.EMPTY;
		}

		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("id", json.get("item").getAsString());
		nbt.setShort("Damage", json.has("data") ? json.get("data").getAsShort() : (short) 0);
		nbt.setShort("Count", json.has("count") ? json.get("count").getAsByte() : (byte) 1);

		if (json.has("nbt"))
		{
			NBTBase nbt1 = JsonUtils.toNBT(json.get("nbt"));

			if (nbt1 instanceof NBTTagCompound)
			{
				nbt.setTag("tag", nbt1);
			}
		}

		if (json.has("caps"))
		{
			NBTBase nbt1 = JsonUtils.toNBT(json.get("caps"));

			if (nbt1 instanceof NBTTagCompound && !nbt1.hasNoTags())
			{
				nbt.setTag("ForgeCaps", nbt1);
			}
		}

		ItemStack stack = new ItemStack(nbt);
		return stack.isEmpty() ? ItemStack.EMPTY : stack;
	}
}