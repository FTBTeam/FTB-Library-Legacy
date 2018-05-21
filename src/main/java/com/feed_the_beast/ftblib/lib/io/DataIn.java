package com.feed_the_beast.ftblib.lib.io;

import com.feed_the_beast.ftblib.lib.icon.Icon;
import com.feed_the_beast.ftblib.lib.math.BlockDimPos;
import com.feed_the_beast.ftblib.lib.util.CommonUtils;
import com.feed_the_beast.ftblib.lib.util.JsonUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.handler.codec.EncoderException;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author LatvianModder
 */
public class DataIn
{
	public interface Deserializer<T>
	{
		T read(DataIn data);
	}

	public static final Deserializer<String> STRING = DataIn::readString;
	public static final Deserializer<Integer> INT = DataIn::readInt;
	public static final Deserializer<Boolean> BOOLEAN = DataIn::readBoolean;
	public static final Deserializer<UUID> UUID = DataIn::readUUID;
	public static final Deserializer<BlockPos> BLOCK_POS = DataIn::readPos;
	public static final Deserializer<BlockDimPos> BLOCK_DIM_POS = DataIn::readDimPos;
	public static final Deserializer<JsonElement> JSON = DataIn::readJson;
	public static final Deserializer<ITextComponent> TEXT_COMPONENT = DataIn::readTextComponent;
	public static final Deserializer<ResourceLocation> RESOURCE_LOCATION = DataIn::readResourceLocation;

	public static final DataIn.Deserializer<ChunkPos> CHUNK_POS = data ->
	{
		int x = data.readInt();
		int z = data.readInt();
		return new ChunkPos(x, z);
	};

	private final ByteBuf byteBuf;

	public DataIn(ByteBuf io)
	{
		byteBuf = io;
	}

	public boolean readBoolean()
	{
		return byteBuf.readBoolean();
	}

	public byte readByte()
	{
		return byteBuf.readByte();
	}

	public void readBytes(byte[] bytes, int off, int len)
	{
		byteBuf.readBytes(bytes, off, len);
	}

	public void readBytes(byte[] bytes)
	{
		readBytes(bytes, 0, bytes.length);
	}

	public short readUnsignedByte()
	{
		return byteBuf.readUnsignedByte();
	}

	public short readShort()
	{
		return byteBuf.readShort();
	}

	public int readUnsignedShort()
	{
		return byteBuf.readUnsignedShort();
	}

	public int readInt()
	{
		return byteBuf.readInt();
	}

	public long readLong()
	{
		return byteBuf.readLong();
	}

	public float readFloat()
	{
		return byteBuf.readFloat();
	}

	public double readDouble()
	{
		return byteBuf.readDouble();
	}

	public String readString()
	{
		return ByteBufUtils.readUTF8String(byteBuf);
	}

	public <T> Collection<T> readCollection(@Nullable Collection<T> collection, Deserializer<T> deserializer)
	{
		if (collection != null)
		{
			collection.clear();
		}

		int id = readUnsignedByte();

		if (id == 6)
		{
			return collection == null ? Collections.emptyList() : collection;
		}

		int size;

		switch (id % 3)
		{
			case 0:
				size = readUnsignedByte();
				break;
			case 1:
				size = readUnsignedShort();
				break;
			default:
				size = readInt();
		}

		if (collection == null)
		{
			if (size == 1)
			{
				return (id / 3 == 0) ? Collections.singletonList(deserializer.read(this)) : Collections.singleton(deserializer.read(this));
			}

			collection = (id / 3 == 0) ? new ArrayList<>(size) : new HashSet<>(size);
		}

		while (--size >= 0)
		{
			collection.add(deserializer.read(this));
		}

		return collection;
	}

	public <T> Collection<T> readCollection(Deserializer<T> deserializer)
	{
		return readCollection(null, deserializer);
	}

	public <K, V> Map<K, V> readMap(@Nullable Map<K, V> map, Deserializer<K> keyDeserializer, Deserializer<V> valueDeserializer)
	{
		if (map != null)
		{
			map.clear();
		}

		int id = readUnsignedByte();

		if (id == 6)
		{
			return map == null ? Collections.emptyMap() : map;
		}

		int size;

		switch (id % 3)
		{
			case 0:
				size = readUnsignedByte();
				break;
			case 1:
				size = readUnsignedShort();
				break;
			default:
				size = readInt();
		}

		if (map == null)
		{
			boolean linked = id >= 3;

			if (keyDeserializer == INT)
			{
				map = CommonUtils.cast(linked ? new Int2ObjectLinkedOpenHashMap<>(size) : new Int2ObjectOpenHashMap<V>(size));
			}
			else
			{
				map = linked ? new LinkedHashMap<>(size) : new HashMap<>(size);
			}
		}

		while (--size >= 0)
		{
			K key = keyDeserializer.read(this);
			V value = valueDeserializer.read(this);
			map.put(key, value);
		}

		return map;
	}

	public <K, V> Map<K, V> readMap(Deserializer<K> keyDeserializer, Deserializer<V> valueDeserializer)
	{
		return readMap(null, keyDeserializer, valueDeserializer);
	}

	public ItemStack readItemStack()
	{
		NBTTagCompound nbt = readNBT();
		return nbt == null ? ItemStack.EMPTY : new ItemStack(nbt);
	}

	@Nullable
	public NBTTagCompound readNBT()
	{
		int i = byteBuf.readerIndex();
		byte b0 = byteBuf.readByte();

		if (b0 == 0)
		{
			return null;
		}

		byteBuf.readerIndex(i);

		try
		{
			return CompressedStreamTools.read(new ByteBufInputStream(byteBuf), NBTSizeTracker.INFINITE);
		}
		catch (IOException ex)
		{
			throw new EncoderException(ex);
		}
	}

	public <T extends IForgeRegistryEntry<T>> T readRegistryEntry(IForgeRegistry<T> registry)
	{
		return ByteBufUtils.readRegistryEntry(byteBuf, registry);
	}

	public <T extends IForgeRegistryEntry<T>> List<T> readRegistryEntries(IForgeRegistry<T> registry)
	{
		return ByteBufUtils.readRegistryEntries(byteBuf, registry);
	}

	public BlockPos readPos()
	{
		int x = readInt();
		int y = readInt();
		int z = readInt();
		return new BlockPos(x, y, z);
	}

	public BlockPos.MutableBlockPos readMutablePos()
	{
		int x = readInt();
		int y = readInt();
		int z = readInt();
		return new BlockPos.MutableBlockPos(x, y, z);
	}

	public BlockDimPos readDimPos()
	{
		int d = readInt();
		int x = readInt();
		int y = readInt();
		int z = readInt();
		return new BlockDimPos(x, y, z, d);
	}

	public UUID readUUID()
	{
		long msb = readLong();
		long lsb = readLong();
		return new UUID(msb, lsb);
	}

	public ResourceLocation readResourceLocation()
	{
		return new ResourceLocation(readString());
	}

	public JsonElement readJson()
	{
		switch (readUnsignedByte())
		{
			case 0:
				return JsonNull.INSTANCE;
			case 1:
			{
				JsonObject json = new JsonObject();

				for (Map.Entry<String, JsonElement> entry : readMap(STRING, JSON).entrySet())
				{
					json.add(entry.getKey(), entry.getValue());
				}

				return json;
			}
			case 2:
			{
				JsonArray json = new JsonArray();

				for (JsonElement json1 : readCollection(JSON))
				{
					json.add(json1);
				}

				return json;
			}
			case 3:
			{
				String s = readString();
				return s.isEmpty() ? JsonUtils.JSON_EMPTY_STRING : new JsonPrimitive(s);
			}
			case 4:
				return readBoolean() ? JsonUtils.JSON_TRUE : JsonUtils.JSON_FALSE;
			case 5:
				return new JsonPrimitive(readByte());
			case 6:
				return new JsonPrimitive(readShort());
			case 7:
				return new JsonPrimitive(readInt());
			case 8:
				return new JsonPrimitive(readLong());
			case 9:
				return new JsonPrimitive(readFloat());
			case 10:
				return new JsonPrimitive(readDouble());
		}

		return JsonNull.INSTANCE;
	}

	@Nullable
	public ITextComponent readTextComponent()
	{
		return JsonUtils.deserializeTextComponent(readJson());
	}

	public GameProfile readProfile()
	{
		UUID id = readUUID();
		return new GameProfile(id, readString());
	}

	public IBlockState readBlockState()
	{
		int id = readInt();
		return id == 0 ? CommonUtils.AIR_STATE : Block.getStateById(id);
	}

	public Icon readIcon()
	{
		return Icon.getIcon(readJson());
	}

	public <E> E read(Deserializer<E> deserializer)
	{
		return deserializer.read(this);
	}
}