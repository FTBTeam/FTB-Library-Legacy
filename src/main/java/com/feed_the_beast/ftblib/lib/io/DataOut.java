package com.feed_the_beast.ftblib.lib.io;

import com.feed_the_beast.ftblib.lib.icon.Icon;
import com.feed_the_beast.ftblib.lib.math.BlockDimPos;
import com.feed_the_beast.ftblib.lib.util.BlockUtils;
import com.feed_the_beast.ftblib.lib.util.JsonUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.handler.codec.EncoderException;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * @author LatvianModder
 */
public class DataOut
{
	public interface Serializer<T>
	{
		void write(DataOut data, T object);
	}

	public static final Serializer<String> STRING = DataOut::writeString;
	public static final Serializer<Integer> INT = DataOut::writeInt;
	public static final Serializer<Boolean> BOOLEAN = DataOut::writeBoolean;
	public static final Serializer<UUID> UUID = DataOut::writeUUID;
	public static final Serializer<BlockPos> BLOCK_POS = DataOut::writePos;
	public static final Serializer<BlockDimPos> BLOCK_DIM_POS = DataOut::writeDimPos;
	public static final Serializer<JsonElement> JSON = DataOut::writeJson;
	public static final Serializer<ITextComponent> TEXT_COMPONENT = DataOut::writeTextComponent;
	public static final Serializer<ResourceLocation> RESOURCE_LOCATION = DataOut::writeResourceLocation;
	public static final Serializer<ItemStack> ITEM_STACK = DataOut::writeItemStack;

	public static final DataOut.Serializer<ChunkPos> CHUNK_POS = (data, pos) ->
	{
		data.writeInt(pos.x);
		data.writeInt(pos.z);
	};

	private final ByteBuf byteBuf;

	public DataOut(ByteBuf io)
	{
		byteBuf = io;
	}

	public void writeBoolean(boolean value)
	{
		byteBuf.writeBoolean(value);
	}

	public void writeByte(int value)
	{
		byteBuf.writeByte(value);
	}

	public void writeBytes(byte[] bytes, int off, int len)
	{
		byteBuf.writeBytes(bytes, off, len);
	}

	public void writeBytes(byte[] bytes)
	{
		writeBytes(bytes, 0, bytes.length);
	}

	public void writeShort(int value)
	{
		byteBuf.writeShort(value);
	}

	public void writeInt(int value)
	{
		byteBuf.writeInt(value);
	}

	public void writeLong(long value)
	{
		byteBuf.writeLong(value);
	}

	public void writeFloat(float value)
	{
		byteBuf.writeFloat(value);
	}

	public void writeDouble(double value)
	{
		byteBuf.writeDouble(value);
	}

	public void writePos(Vec3i pos)
	{
		writeInt(pos.getX());
		writeInt(pos.getY());
		writeInt(pos.getZ());
	}

	public void writeDimPos(BlockDimPos pos)
	{
		writeInt(pos.dim);
		writeInt(pos.posX);
		writeInt(pos.posY);
		writeInt(pos.posZ);
	}

	public void writeUUID(UUID id)
	{
		writeLong(id.getMostSignificantBits());
		writeLong(id.getLeastSignificantBits());
	}

	public void writeString(String string)
	{
		ByteBufUtils.writeUTF8String(byteBuf, string);
	}

	public <T> void writeCollection(Collection<T> collection, Serializer<T> serializer)
	{
		int size = collection.size();

		if (size == 0)
		{
			writeByte(6);
			return;
		}

		boolean set = collection instanceof Set;

		if (size >= 65536)
		{
			writeByte(set ? 5 : 2);
			writeInt(size);
		}
		else if (size >= 256)
		{
			writeByte(set ? 4 : 1);
			writeShort(size);
		}
		else
		{
			writeByte(set ? 3 : 0);
			writeByte(size);
		}

		for (T object : collection)
		{
			serializer.write(this, object);
		}
	}

	public <K, V> void writeMap(Map<K, V> map, Serializer<K> keySerializer, Serializer<V> valueSerializer)
	{
		int size = map.size();

		if (size == 0)
		{
			writeByte(6);
			return;
		}

		boolean linked = map instanceof LinkedHashMap || map instanceof Int2ObjectLinkedOpenHashMap;

		if (size >= 65536)
		{
			writeByte(linked ? 5 : 2);
			writeInt(size);
		}
		else if (size >= 256)
		{
			writeByte(linked ? 4 : 1);
			writeShort(size);
		}
		else
		{
			writeByte(linked ? 3 : 0);
			writeByte(size);
		}

		for (Map.Entry<K, V> entry : map.entrySet())
		{
			keySerializer.write(this, entry.getKey());
			valueSerializer.write(this, entry.getValue());
		}
	}

	public void writeItemStack(ItemStack stack)
	{
		if (stack.isEmpty() || stack.getItem().getRegistryName() == null)
		{
			writeInt(0);
			return;
		}

		writeInt(Item.getIdFromItem(stack.getItem()));
		writeByte(stack.getCount());
		writeShort(stack.getMetadata());
		writeNBT(stack.getItem().isDamageable() || stack.getItem().getShareTag() ? stack.getItem().getNBTShareTag(stack) : null);
	}

	public void writeNBT(@Nullable NBTTagCompound nbt)
	{
		if (nbt == null)
		{
			this.writeByte(0);
		}
		else
		{
			try
			{
				CompressedStreamTools.write(nbt, new ByteBufOutputStream(byteBuf));
			}
			catch (IOException ex)
			{
				throw new EncoderException(ex);
			}
		}
	}

	public void writeNBTBase(@Nullable NBTBase nbt)
	{
		if (nbt == null)
		{
			writeByte(0);
		}
		else if (nbt instanceof NBTTagCompound)
		{
			writeByte(1);
			writeNBT((NBTTagCompound) nbt);
		}
		else
		{
			writeByte(2);
			NBTTagCompound nbt1 = new NBTTagCompound();
			nbt1.setTag("_", nbt);
			writeNBT(nbt1);
		}
	}

	public void writeResourceLocation(ResourceLocation r)
	{
		writeString(r.toString());
	}

	public int writeJson(@Nullable JsonElement element)
	{
		if (JsonUtils.isNull(element))
		{
			writeByte(0);
			return 0;
		}
		else if (element.isJsonObject())
		{
			writeByte(1);

			Set<Map.Entry<String, JsonElement>> set = element.getAsJsonObject().entrySet();
			Map<String, JsonElement> map = new LinkedHashMap<>(set.size());

			for (Map.Entry<String, JsonElement> entry : set)
			{
				map.put(entry.getKey(), entry.getValue());
			}

			writeMap(map, STRING, JSON);
			return 1;
		}
		else if (element.isJsonArray())
		{
			writeByte(2);

			JsonArray json = element.getAsJsonArray();
			Collection<JsonElement> collection = new ArrayList<>(json.size());

			for (JsonElement json1 : json)
			{
				collection.add(json1);
			}

			writeCollection(collection, JSON);
			return 2;
		}

		JsonPrimitive primitive = element.getAsJsonPrimitive();

		if (primitive.isBoolean())
		{
			if (primitive.getAsBoolean())
			{
				writeByte(11);
				return 11;
			}
			else
			{
				writeByte(12);
				return 12;
			}
		}
		else if (primitive.isNumber())
		{
			if (primitive == JsonUtils.JSON_ZERO)
			{
				writeByte(4);
				return 4;
			}

			Number number = primitive.getAsNumber();

			if (number.doubleValue() == 0D)
			{
				writeByte(4);
				return 4;
			}

			Class<? extends Number> n = number.getClass();

			if (n == Integer.class)
			{
				writeByte(7);
				writeInt(primitive.getAsInt());
				return 7;
			}
			else if (n == Byte.class)
			{
				writeByte(5);
				writeByte(primitive.getAsByte());
				return 5;
			}
			else if (n == Short.class)
			{
				writeByte(6);
				writeShort(primitive.getAsShort());
			}
			else if (n == Long.class)
			{
				writeByte(8);
				writeLong(primitive.getAsLong());
				return 8;
			}
			else if (n == Float.class)
			{
				writeByte(9);
				writeFloat(primitive.getAsFloat());
				return 9;
			}
			else if (n == Double.class)
			{
				writeByte(4);
				writeDouble(primitive.getAsDouble());
				return 4;
			}
		}

		String string = primitive.getAsString();

		if (string.isEmpty())
		{
			writeByte(13);
			return 13;
		}

		writeByte(3);
		writeString(string);
		return 3;
	}

	public void writeTextComponent(@Nullable ITextComponent component)
	{
		writeJson(JsonUtils.serializeTextComponent(component));
	}

	public void writeProfile(GameProfile profile)
	{
		writeUUID(profile.getId());
		writeString(profile.getName());
	}

	public void writeBlockState(IBlockState state)
	{
		writeInt(state == BlockUtils.AIR_STATE ? 0 : Block.getStateId(state));
	}

	public void writeIcon(@Nullable Icon icon)
	{
		writeJson((icon == null ? Icon.EMPTY : icon).getJson());
	}

	public <E> void write(E object, Serializer<E> serializer)
	{
		serializer.write(this, object);
	}
}