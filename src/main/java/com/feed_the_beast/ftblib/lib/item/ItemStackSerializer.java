package com.feed_the_beast.ftblib.lib.item;

import com.feed_the_beast.ftblib.lib.util.JsonUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.oredict.OreDictionary;

public class ItemStackSerializer
{
	public static ItemStack parseItemThrowingException(String input) throws Exception
	{
		input = input.trim();
		if (input.isEmpty() || input.equals("-") || input.equals("minecraft:air"))
		{
			return ItemStack.EMPTY;
		}
		else if (input.startsWith("{"))
		{
			NBTTagCompound nbt = JsonToNBT.getTagFromJson(input);

			if (nbt.getByte("Count") <= 0)
			{
				nbt.setByte("Count", (byte) 1);
			}

			return new ItemStack(nbt);
		}

		String[] s1 = input.split(" ", 4);

		if (s1.length == 0)
		{
			return ItemStack.EMPTY;
		}

		Item item = Item.REGISTRY.getObject(new ResourceLocation(s1[0]));

		if (item == null)
		{
			throw new NullPointerException("Unknown item: " + s1[0]);
		}

		if (item == Items.AIR)
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
			itemstack.setTagCompound(JsonToNBT.getTagFromJson(s1[3]));
		}

		return itemstack;
	}

	public static ItemStack parseItem(String input)
	{
		try
		{
			return parseItemThrowingException(input);
		}
		catch (Exception ex)
		{
			return ItemStack.EMPTY;
		}
	}

	public static String toString(ItemStack stack)
	{
		if (stack.isEmpty())
		{
			return "minecraft:air";
		}

		StringBuilder builder = new StringBuilder(String.valueOf(Item.REGISTRY.getNameForObject(stack.getItem())));

		int count = stack.getCount();
		int meta = stack.getMetadata();
		NBTTagCompound tag = stack.getTagCompound();

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

			if (nbt1 instanceof NBTTagCompound && !nbt1.isEmpty())
			{
				nbt.setTag("tag", nbt1);
			}
		}

		if (json.has("caps"))
		{
			NBTBase nbt1 = JsonUtils.toNBT(json.get("caps"));

			if (nbt1 instanceof NBTTagCompound && !nbt1.isEmpty())
			{
				nbt.setTag("ForgeCaps", nbt1);
			}
		}

		ItemStack stack = new ItemStack(nbt);
		return stack.isEmpty() ? ItemStack.EMPTY : stack;
	}
}