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
	public static final ItemEntry EMPTY = new ItemEntry(Items.AIR, 0, null, null);

	public static ItemEntry get(ItemStack stack)
	{
		if (stack.isEmpty())
		{
			return EMPTY;
		}

		Item item = stack.getItem();
		int metadata = stack.getMetadata();
		NBTTagCompound nbt0 = stack.serializeNBT();
		NBTTagCompound nbt = (NBTTagCompound) nbt0.getTag("tag");
		NBTTagCompound caps = (NBTTagCompound) nbt0.getTag("ForgeCaps");
		return new ItemEntry(item, metadata, nbt, caps);
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

			NBTTagCompound caps = null;

			if (json.has("caps"))
			{
				caps = (NBTTagCompound) JsonUtils.toNBT(json.get("caps"));
			}

			return new ItemEntry(item, meta, nbt, caps);
		}

		return EMPTY;
	}

	public final Item item;
	public final int metadata;
	public final NBTTagCompound nbt;
	public final NBTTagCompound caps;
	private int hashCode;
	private ItemStack stack = null;

	private ItemEntry(Item i, int m, @Nullable NBTTagCompound n, @Nullable NBTTagCompound c)
	{
		item = i;
		metadata = m;
		nbt = n;
		caps = c;
		hashCode = 0;
	}

	public boolean isEmpty()
	{
		return this == EMPTY;
	}

	public int hashCode()
	{
		if (isEmpty())
		{
			return 0;
		}
		else if (hashCode == 0)
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

		return item == entry.item && metadata == entry.metadata && Objects.equals(nbt, entry.nbt) && Objects.equals(caps, entry.caps);
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

		if (nbt != null || caps != null)
		{
			builder.append(' ');
			builder.append(nbt);
		}

		if (caps != null)
		{
			builder.append(' ');
			builder.append(caps);
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
			stack = new ItemStack(item, 1, metadata, caps);
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