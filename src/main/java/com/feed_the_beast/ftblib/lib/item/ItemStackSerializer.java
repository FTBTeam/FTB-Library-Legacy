package com.feed_the_beast.ftblib.lib.item;

import com.feed_the_beast.ftblib.lib.util.JsonUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
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

	public static JsonElement serialize(ItemStack is, boolean forceNonnull, boolean string)
	{
		if (!forceNonnull && is.isEmpty())
		{
			return JsonNull.INSTANCE;
		}
		else if (string)
		{
			return new JsonPrimitive(toString(is));
		}

		JsonObject json = new JsonObject();
		ResourceLocation id = Item.REGISTRY.getNameForObject(is.getItem());
		json.addProperty("item", id == null ? "minecraft:air" : id.toString());

		if (is.getHasSubtypes())
		{
			json.addProperty("data", is.getMetadata());
		}

		if (is.getCount() > 1)
		{
			json.addProperty("count", is.getCount());
		}

		if (is.hasTagCompound())
		{
			json.add("nbt", JsonUtils.toJson(is.getTagCompound()));
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
		else
		{
			JsonObject json = e.getAsJsonObject();

			if (!json.has("item"))
			{
				return ItemStack.EMPTY;
			}

			Item item = Item.getByNameOrId(json.get("id").getAsString());

			if (item == null)
			{
				return ItemStack.EMPTY;
			}

			int meta = 0;

			if (item.getHasSubtypes() && json.has("data"))
			{
				meta = json.get("data").getAsInt();
			}

			int count = 1;

			if (json.has("count"))
			{
				count = json.get("count").getAsInt();
			}

			ItemStack stack = new ItemStack(item, count, meta);

			if (stack.isEmpty())
			{
				return ItemStack.EMPTY;
			}

			if (json.has("nbt"))
			{
				stack.setTagCompound((NBTTagCompound) JsonUtils.toNBT(json.get("nbt")));
			}

			return stack;
		}
	}
}