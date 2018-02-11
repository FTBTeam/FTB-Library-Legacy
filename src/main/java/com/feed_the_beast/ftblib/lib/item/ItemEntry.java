package com.feed_the_beast.ftblib.lib.item;

import com.feed_the_beast.ftblib.lib.util.JsonUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Objects;

/**
 * @author LatvianModder
 */
public final class ItemEntry
{
	public static final ItemEntry EMPTY = new ItemEntry(ItemStack.EMPTY);

	public static ItemEntry get(ItemStack stack)
	{
		return stack.isEmpty() ? EMPTY : new ItemEntry(stack);
	}

	public static ItemEntry fromJson(@Nullable JsonElement json0)
	{
		if (!JsonUtils.isNull(json0) && json0.isJsonObject())
		{
			JsonObject json = json0.getAsJsonObject();

			Item item = null;

			if (json.has("item"))
			{
				item = Item.REGISTRY.getObject(new ResourceLocation(json.get("item").getAsString()));
			}

			if (item == null || item == Items.AIR)
			{
				return EMPTY;
			}

			int meta = 0;

			if (json.has("data") && item.getHasSubtypes())
			{
				meta = json.get("data").getAsInt();
			}

			NBTTagCompound nbt = null;

			if (json.has("nbt"))
			{
				nbt = (NBTTagCompound) JsonUtils.toNBT(json.get("nbt"));
			}

			return new ItemEntry(item, meta, nbt);
		}

		return EMPTY;
	}

	public final Item item;
	public final int metadata;
	public final NBTTagCompound nbt;
	private int hashCode;
	private ItemStack stack = null;

	private ItemEntry(Item i, int m, @Nullable NBTTagCompound n)
	{
		item = i;
		metadata = m;
		nbt = n;
	}

	private ItemEntry(ItemStack stack)
	{
		item = stack.getItem();
		metadata = stack.getMetadata();
		NBTTagCompound nbt0 = stack.getTagCompound();
		nbt = (nbt0 == null || nbt0.hasNoTags()) ? null : nbt0;
		hashCode = 0;
	}

	public boolean isEmpty()
	{
		return this == EMPTY;
	}

	public int hashCode()
	{
		if (hashCode == 0)
		{
			hashCode = Objects.hash(item, metadata, nbt);

			if (hashCode == 0)
			{
				hashCode = 1;
			}
		}

		return hashCode;
	}

	public boolean equalsEntry(ItemEntry entry)
	{
		if (entry == this)
		{
			return true;
		}

		return item == entry.item && metadata == entry.metadata && Objects.equals(nbt, entry.nbt);
	}

	public boolean equals(Object o)
	{
		return o == this || o != null && o.getClass() == ItemEntry.class && equalsEntry((ItemEntry) o);
	}

	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		toString(builder);
		return builder.toString();
	}

	public void toString(StringBuilder builder)
	{
		builder.append(item.getRegistryName());
		builder.append(' ');

		if (metadata == 32767)
		{
			builder.append('*');
		}
		else
		{
			builder.append(metadata);
		}

		if (nbt != null)
		{
			builder.append(' ');
			builder.append(nbt);
		}
	}

	public ItemStack getStack(int count, boolean copy)
	{
		if (count <= 0 || isEmpty())
		{
			return ItemStack.EMPTY;
		}

		if (stack == null)
		{
			stack = new ItemStack(item, 1, metadata);
			stack.setTagCompound(nbt);
		}

		ItemStack stack1;

		if (copy)
		{
			stack1 = stack.copy();
		}
		else
		{
			stack1 = stack;
		}

		stack1.setCount(count);
		return stack1;
	}

	public JsonObject toJson()
	{
		JsonObject json = new JsonObject();

		ResourceLocation id = Item.REGISTRY.getNameForObject(item);
		json.addProperty("item", id == null ? "minecraft:air" : id.toString());

		if (item.getHasSubtypes())
		{
			json.addProperty("data", metadata);
		}

		if (nbt != null)
		{
			json.add("nbt", JsonUtils.toJson(nbt));
		}

		return json;
	}
}