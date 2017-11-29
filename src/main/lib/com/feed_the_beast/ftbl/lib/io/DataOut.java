package com.feed_the_beast.ftbl.lib.io;

import com.feed_the_beast.ftbl.lib.icon.Icon;
import com.feed_the_beast.ftbl.lib.math.BlockDimPos;
import com.feed_the_beast.ftbl.lib.util.JsonUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import javax.annotation.Nullable;
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

	public static Serializer<String> STRING = DataOut::writeString;
	public static Serializer<Integer> INT = DataOut::writeInt;
	public static Serializer<Boolean> BOOLEAN = DataOut::writeBoolean;
	public static Serializer<UUID> UUID = DataOut::writeUUID;
	public static Serializer<BlockPos> BLOCK_POS = DataOut::writePos;
	public static Serializer<BlockDimPos> BLOCK_DIM_POS = DataOut::writeDimPos;
	public static Serializer<JsonElement> JSON = DataOut::writeJson;
	public static Serializer<ITextComponent> TEXT_COMPONENT = DataOut::writeTextComponent;
	public static Serializer<ResourceLocation> RESOURCE_LOCATION = DataOut::writeResourceLocation;

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
		ByteBufUtils.writeItemStack(byteBuf, stack);
	}

	public void writeNBT(@Nullable NBTTagCompound nbt)
	{
		ByteBufUtils.writeTag(byteBuf, nbt);
	}

	public void writeResourceLocation(ResourceLocation r)
	{
		writeString(r.toString());
	}

	public void writeJson(@Nullable JsonElement element)
	{
		if (element == null || element.isJsonNull())
		{
			writeByte(0);
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
		}
		else
		{
			JsonPrimitive primitive = element.getAsJsonPrimitive();

			if (primitive.isString())
			{
				writeByte(3);
				writeString(primitive.getAsString());
			}
			else if (primitive.isBoolean())
			{
				writeByte(4);
				writeBoolean(primitive.getAsBoolean());
			}
			else
			{
				Class<? extends Number> n = primitive.getAsNumber().getClass();

				if (n == Integer.class)
				{
					writeByte(7);
					writeInt(primitive.getAsInt());
				}
				else if (n == Byte.class)
				{
					writeByte(5);
					writeByte(primitive.getAsByte());
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
				}
				else if (n == Float.class)
				{
					writeByte(9);
					writeFloat(primitive.getAsFloat());
				}
				else if (n == Double.class)
				{
					writeByte(10);
					writeDouble(primitive.getAsDouble());
				}
				else
				{
					writeByte(0);
				}
			}
		}
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
		writeInt(state == Blocks.AIR.getDefaultState() ? 0 : Block.getStateId(state));
	}

	public void writeIcon(@Nullable Icon icon)
	{
		writeJson((icon == null ? Icon.EMPTY : icon).getJson());
	}

	public <E> void write(Serializer<E> serializer, E object)
	{
		serializer.write(this, object);
	}
}